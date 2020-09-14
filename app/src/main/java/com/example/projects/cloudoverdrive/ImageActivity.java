package com.example.projects.cloudoverdrive;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projects.cloudoverdrive.Adapters.ImageAdapter;
import com.example.projects.cloudoverdrive.Adapters.VideoAdapter;
import com.example.projects.cloudoverdrive.Models.Images;
import com.example.projects.cloudoverdrive.Models.Videos;
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

public class ImageActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener  {
    RecyclerView recyclerView;
    ImageAdapter adapter;
    List<Images> uploads;
    TextView text;
    Toolbar toolbar;
    private DatabaseReference databaseRef;
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        text = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();
        adapter = new ImageAdapter(ImageActivity.this, uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ImageActivity.this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("images");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Images u = postSnapshot.getValue(Images.class);
                    u.setKey(postSnapshot.getKey());
                    uploads.add(u);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDelete(int position ) {
        Images selectedItem = uploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImageActivity.this, "ImageDeleted !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDownload(int position)  {
        final Images selectedItem = uploads.get(position);
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
                downloadFiles(ImageActivity.this, selectedItem.getFilename(), url);
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