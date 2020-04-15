package com.example.first_firebase.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.example.first_firebase.Models.Post;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    Dialog popCreatePost ;
    ImageView popAddImageView ,popCreateImagebtn;
    EditText popupCreateTitleEditText , popupCreateDescEditText ;
    ProgressBar popupProgerssBar;
    private static final int PICK_IMAGE=1;
    int REQUESTCODE = 1 ;
    int PReqCode = 1 ;
    Uri imageUri;



    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);





        //
        auth = FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();

        // popup

        inPopup();
        setUpPopupImageClick();


        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popCreatePost.show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeader();

    }

    private void setUpPopupImageClick() {
        popAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery
                // check that app has access to files
                checkaAndRequestForPermission();



            }
        });

    }
    private void checkaAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(HomeActivity.this,"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }else{
            // If every thing goes allRight the App has permission to access user gallery

            openGallery();
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageUri=data.getData();
            popAddImageView.setImageURI(imageUri);
        }
    }

    private void inPopup() {
        popCreatePost = new Dialog(this) ;
        popCreatePost.setContentView(R.layout.popup_add_post);
        popCreatePost.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        popCreatePost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popCreatePost.getWindow().getAttributes().gravity = Gravity.TOP ;

        // ini popup wid

        popAddImageView = popCreatePost.findViewById(R.id.popupImageView);
        popupCreateTitleEditText= popCreatePost.findViewById(R.id.popup_titleEditText);
        popupCreateDescEditText = popCreatePost.findViewById(R.id.popupDescriptionEditText);
        popupProgerssBar = popCreatePost.findViewById(R.id.popupProgressBar);
        popCreateImagebtn= popCreatePost.findViewById(R.id.popupImageCreateBtn);


        // Create Post On Click Listener
        popCreateImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupProgerssBar.setVisibility(View.VISIBLE);
                popCreateImagebtn.setVisibility(View.INVISIBLE);


                // we need to check the all input field should be filled
                if ( !popupCreateTitleEditText.getText().toString().isEmpty()  && !popupCreateDescEditText.getText().toString().isEmpty() && imageUri != null){

                    // if NO empty field is their
                    // TODO Create Post Object and add It to firebase

                    // First upload image to firebase
                    // access firebase storage

                    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("post_image");
                    final StorageReference imagepath= storageReference.child(imageUri.getLastPathSegment());
                    imagepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(popupCreateTitleEditText.getText().toString(),
                                            popupCreateDescEditText.getText().toString(),
                                            imageDownloadLink,
                                            currentUser.getUid());
                                    // Add Post Object
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something went wrong uploading
                                    Toast.makeText(HomeActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    popupProgerssBar.setVisibility(View.INVISIBLE);
                                    popCreateImagebtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }else {
                    Toast.makeText(HomeActivity.this,"Plz Enter The Correct INput ",Toast.LENGTH_LONG).show();
                    popupProgerssBar.setVisibility(View.INVISIBLE);
                    popCreateImagebtn.setVisibility(View.VISIBLE);




                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique 10 and update post key

        String key = myRef.getKey();
        post.setPostKey(key);

        // add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomeActivity.this,"Post Created Successfully " ,Toast.LENGTH_LONG).show();
                popupProgerssBar.setVisibility(View.INVISIBLE);
                popCreateImagebtn.setVisibility(View.VISIBLE);
                popCreatePost.dismiss();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateNavHeader(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView  = navigationView.getHeaderView(0);
        TextView navUsername =  headerView.findViewById(R.id.nav_user_name);
        TextView navUserEmail =  headerView.findViewById(R.id.nav_user_email);
        ImageView navUserPhoto =  headerView.findViewById(R.id.nav_user_image);


        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());


    }
}
