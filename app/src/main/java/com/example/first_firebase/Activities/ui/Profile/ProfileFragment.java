package com.example.first_firebase.Activities.ui.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

//import com.example.first_firebase.Activities.R;

public class ProfileFragment extends Fragment {

    ImageView image_profile;
    ImageButton cameraButton , galleryButton ;
    EditText name_profile , email_profile ;
    Button updateProfileButton;
    ProgressBar profileProgressBar;

    String ChangeName,ChangeEmail ;

    FirebaseAuth auth;
    FirebaseUser current_User ;

    Context context;

    Uri imageUri;
    int PReqCode = 1 ;
    private static final int PICK_IMAGE2=1;
    private static final int REQUEST_IMAGE_CAPTURE = 101;




    private ProfileViewModel profileViewModel;

    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        name_profile=root.findViewById(R.id.name_profile);
        email_profile=root.findViewById(R.id.email_profile);
        image_profile=root.findViewById(R.id.image_profile);
        cameraButton=root.findViewById(R.id.cameraButton);
        galleryButton=root.findViewById(R.id.galleryButton);
        updateProfileButton=root.findViewById(R.id.updateProfileButton);
        profileProgressBar=root.findViewById(R.id.profile_ProgressBar);

        profileProgressBar.setVisibility(View.INVISIBLE);


        FloatingActionButton fab = root.findViewById(R.id.fab);

// TODO it is not working

//        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//        p.setAnchorId(View.NO_ID);
//        fab.setLayoutParams(p);
//        fab.setVisibility(View.GONE);


        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s); }
        });


        auth=FirebaseAuth.getInstance();
        current_User=auth.getCurrentUser();





        // To Add Image

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkaAndRequestForPermissionForCamera();
            }
        });


        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkaAndRequestForPermissionForGallery();
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeName = name_profile.getText().toString();
                ChangeEmail = email_profile.getText().toString();
                updateProfileButton.setVisibility(View.INVISIBLE);
                profileProgressBar.setVisibility(View.VISIBLE);



                UpdateUserInfo(ChangeName,imageUri,ChangeEmail,auth.getCurrentUser());

            }
        });






        profileDetail();

        return root;
    }







    private void UpdateUserInfo(final String changeName, Uri uri , final String changeEmail, final FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_photo");
        final StorageReference imagePAth = storageReference.child(uri.getLastPathSegment());
        imagePAth.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded suceesfully
                // now we can get our image url
                imagePAth.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri cointains user image url
                        imagePAth.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(changeName)
                                        .setPhotoUri(uri)
                                        .build();

                                currentUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    // user info updated succefully
//                                                    showMessage("Updated Successfully ");
                                                    Toast.makeText(getActivity()," Update successfully",Toast.LENGTH_LONG).show();
                                                    profileProgressBar.setVisibility(View.INVISIBLE);
                                                    updateProfileButton.setVisibility(View.VISIBLE);


                                                }
                                            }
                                        });



                            }
                        });

                    }
                });

            }
        });




    }




    public void profileDetail(){
        name_profile.setText(current_User.getDisplayName());
        email_profile.setText(current_User.getEmail());
        //image_profile.setImageURI(current_User.getPhotoUrl());

        // use glide
        Glide.with(this).load(current_User.getPhotoUrl()).into(image_profile);

    }

    private void checkaAndRequestForPermissionForCamera() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){
                Toast.makeText(getActivity(),"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},PReqCode);
            }
        }else{
            // If every thing goes allRight the App has permission to access user camera
            openCamera();
        }

    }

    private void checkaAndRequestForPermissionForGallery(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity(),"Please accept for required permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }else{
            // If every thing goes allRight the App has permission to access user gallery
            openGallery();
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE2);
    }
    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap cameraBitmap = (Bitmap) extras.get("data");
            image_profile.setImageBitmap(cameraBitmap);


        }else if(requestCode==PICK_IMAGE2 && resultCode == RESULT_OK){
            imageUri = data.getData();
            image_profile.setImageURI(imageUri);
        }
    }


}