package com.example.projects.cloudoverdrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.projects.cloudoverdrive.Adapters.FileAdapter;
import com.example.projects.cloudoverdrive.Models.Files;
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

public class FileActivity extends AppCompatActivity implements FileAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    FileAdapter adapter;
    List<Files> uploads;
    TextView text;
    Toolbar toolbar;
    private DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        text = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();
        adapter = new FileAdapter(FileActivity.this, uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(FileActivity.this);

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

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("files");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Files u = postSnapshot.getValue(Files.class);
                    u.setKey(postSnapshot.getKey());
                    uploads.add(u);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDelete(int position ) {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //final String userId = user.getUid();
        Files selectedItem = uploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseRef.child(selectedKey).removeValue();
                Toast.makeText(FileActivity.this, "File Deleted !!", Toast.LENGTH_SHORT).show();
            }
        });
        /*databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("Users").child(userId).child("uri").child("files").orderByChild("filename").equalTo(a);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(FileActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); */
    }

    @Override
    public void onDownload(int position)  {
        final Files selectedItem = uploads.get(position);
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getUrl());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
                downloadFiles(FileActivity.this, selectedItem.getFilename(), url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void downloadFiles(Context context, String filename,  String url){
        DownloadManager downloadManager= (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
        downloadManager.enqueue(request);
    }
}