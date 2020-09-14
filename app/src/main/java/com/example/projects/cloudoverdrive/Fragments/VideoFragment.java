package com.example.projects.cloudoverdrive.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projects.cloudoverdrive.Adapters.VideoAdapter;
import com.example.projects.cloudoverdrive.Models.Videos;
import com.example.projects.cloudoverdrive.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class VideoFragment extends Fragment implements VideoAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    VideoAdapter adapter;
    List<Videos> uploads;
    private DatabaseReference databaseRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uploads = new ArrayList<>();
        adapter = new VideoAdapter(getContext(), uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(VideoFragment.this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("videos");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Videos u = postSnapshot.getValue(Videos.class);
                    u.setKey(postSnapshot.getKey());
                    uploads.add(u);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onDelete(int position ) {
        Videos selectedItem = uploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseRef.child(selectedKey).removeValue();
                Toast.makeText(getContext(), "Video Deleted !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDownload(int position)  {
        final Videos selectedItem = uploads.get(position);
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
                downloadFiles(getContext(), selectedItem.getFilename(), url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void downloadFiles(Context context, String filename, String url){
        DownloadManager downloadManager= (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
        downloadManager.enqueue(request);
    }
}