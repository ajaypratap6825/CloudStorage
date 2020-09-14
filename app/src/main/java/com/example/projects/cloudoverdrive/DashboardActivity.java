package com.example.projects.cloudoverdrive;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.projects.cloudoverdrive.Adapters.SectionsPageAdapter;
import com.example.projects.cloudoverdrive.Fragments.AudioFragment;
import com.example.projects.cloudoverdrive.Fragments.FileFragment;
import com.example.projects.cloudoverdrive.Fragments.PhotoFragment;
import com.example.projects.cloudoverdrive.Fragments.VideoFragment;
import com.example.projects.cloudoverdrive.Models.Audios;
import com.example.projects.cloudoverdrive.Models.Files;
import com.example.projects.cloudoverdrive.Models.Images;
import com.example.projects.cloudoverdrive.Models.Videos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;
    Uri uri;
    static final int ImageRequest = 1;
    static final int VideoRequest = 2;
    static final int AudioRequest = 3;
    static final int FileRequest = 4;
    FirebaseUser user;
    FloatingActionButton fab, fab_audio, fab_video, fab_image, fab_file;
    Animation fabopen, fabclose, fabRClockwise, fabRAnticlockwise;
    boolean isOpen= false;
    private DatabaseReference mDatabaseRef;
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fab = findViewById(R.id.fab);
        fab_file = findViewById(R.id.fab_file);
        fab_audio = findViewById(R.id.fab_audio);
        fab_video = findViewById(R.id.fab_video);
        fab_image = findViewById(R.id.fab_image);
        fabopen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabclose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabRClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabRAnticlockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        FirebaseMessaging.getInstance().subscribeToTopic("General");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    fab_audio.startAnimation(fabclose);
                    fab_file.startAnimation(fabclose);
                    fab_image.startAnimation(fabclose);
                    fab_video.startAnimation(fabclose);
                    fab.startAnimation(fabRClockwise);

                    fab_audio.setClickable(false);
                    fab_file.setClickable(false);
                    fab_image.setClickable(false);
                    fab_video.setClickable(false);

                    isOpen= false;
                }else{
                    fab_audio.startAnimation(fabopen);
                    fab_file.startAnimation(fabopen);
                    fab_image.startAnimation(fabopen);
                    fab_video.startAnimation(fabopen);
                    fab.startAnimation(fabRAnticlockwise);

                    fab_audio.setClickable(true);
                    fab_file.setClickable(true);
                    fab_image.setClickable(true);
                    fab_video.setClickable(true);

                    isOpen= true;
                }
            }
        });
        fab_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        fab_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
        fab_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAudio();
            }
        });
        fab_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideo();
            }
        });

        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(DashboardActivity.this, "Logged out !!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                Intent in = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.nav_files:
                Intent ia = new Intent(DashboardActivity.this, FileActivity.class);
                startActivity(ia);
                finish();
                break;
            case R.id.nav_images:
                Intent ib = new Intent(DashboardActivity.this, ImageActivity.class);
                startActivity(ib);
                finish();
                break;
            case R.id.nav_notifications:
                Intent i = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_exit:
                Intent ie = new Intent(Intent.ACTION_MAIN);
                ie.addCategory(Intent.CATEGORY_HOME);
                ie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ie);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, ImageRequest);
    }
    private void openVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, VideoRequest);
    }
    private void openAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, AudioRequest);
    }
    private void openFile() {
        Intent intent = new Intent();
        intent.setType("application/msword " + " // .doc & .docx\n" + "\"application/vnd.ms-powerpoint\"," +
                " // .ppt & .pptx\n" + "\"application/vnd.ms-excel\","  + " // .xls & .xlsx\n" + "\"text/plain\",\n" +
                "\"application/pdf\",\n" + "\"application/zip\"");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FileRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == ImageRequest ) {
            uri = data.getData();
            String path = uri.getPath();
            String filename = path.substring(path.lastIndexOf("/")+1);
            uploadImage(filename);
            } else if(requestCode == VideoRequest){
                uri = data.getData();
                String path = uri.getPath();
                String filename = path.substring(path.lastIndexOf("/")+1);
                uploadVideo(filename);
            }else if(requestCode == AudioRequest){
                uri = data.getData();
                String path = uri.getPath();
                String filename = path.substring(path.lastIndexOf("/")+1);
                uploadAudio(filename);
            }else if(requestCode == FileRequest) {
                uri = data.getData();
                String path = uri.getPath();
                String filename = path.substring(path.lastIndexOf("/")+1);
                uploadFile(filename);
            }else{
                Toast.makeText(this, "Invalid selection !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver content = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(content.getType(uri));
    }

    private void uploadImage(final String filename) {
        final String name = filename + "." + getFileExtension(uri);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference file = FirebaseStorage.getInstance().getReference().child("Uploads/" + user.getUid()).child("images").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           // Log.d("Download Url",url);
                            Images up= new Images(uri.toString(), name);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("images");
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(up);
                            pd.dismiss();
                            Toast.makeText(DashboardActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void uploadVideo(final String filename) {
        final String name = filename + "." + getFileExtension(uri);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference file = FirebaseStorage.getInstance().getReference().child("Uploads/" + user.getUid()).child("videos").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Log.d("Download Url",url);
                            Videos up= new Videos(uri.toString(), name);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("videos");
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(up);
                            pd.dismiss();
                            Toast.makeText(DashboardActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void uploadAudio(final String filename) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference file = FirebaseStorage.getInstance().getReference().child("Uploads/" + user.getUid()).child("audios").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Log.d("Download Url",url);
                            Audios up= new Audios(uri.toString(), filename);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("audios");
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(up);
                            pd.dismiss();
                            Toast.makeText(DashboardActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void uploadFile(final String filename) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (uri != null) {
            final StorageReference file = FirebaseStorage.getInstance().getReference().child("Uploads/" + user.getUid()).child("files").child(System.currentTimeMillis() + "." + getFileExtension(uri));
            file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Log.d("Download Url",url);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("uri").child("files");
                            String uploadId = mDatabaseRef.push().getKey();
                            Files up= new Files(uri.toString(), filename);
                            mDatabaseRef.child(uploadId).setValue(up);
                            pd.dismiss();
                            Toast.makeText(DashboardActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FileFragment(), "Files");
        adapter.addFragment(new AudioFragment(), "Audios");
        adapter.addFragment(new VideoFragment(), "Videos");
        adapter.addFragment(new PhotoFragment(), "Photos");
        viewPager.setAdapter(adapter);
    }
}

