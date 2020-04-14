package com.example.first_firebase.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView imageView;
    EditText nameEditText , emailEditText,passwordhEditText,confirmpassEditText;
    ProgressBar progressBar;
    Button button;
    private FirebaseAuth auth;

    int REQUESTCODE = 1 ;
    int PReqCode = 1 ;
    Uri pickedImgUri ;


    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        imageView=findViewById(R.id.imageView);
        nameEditText=findViewById(R.id.editText);
        emailEditText =findViewById(R.id.editText2);
        passwordhEditText=findViewById(R.id.editText3);
        confirmpassEditText=findViewById(R.id.editText4);
        button=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        auth=FirebaseAuth.getInstance();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                final String email = emailEditText.getText().toString();
                final String name = nameEditText.getText().toString();
                final String password =passwordhEditText.getText().toString();
                final String confirmpassword = confirmpassEditText.getText().toString();


                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()){
                    showMessage(" Field Is Empty ");
                    if (password != confirmpassword){
                        showMessage("Password Should be same ");
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    // every thing is okay
                    // create user Account
                    createUserAccount(email ,name,password);

                }




            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=22){
                    checkRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });

    }

    private void createUserAccount(String email, final String name, String password) {
        // this method create user Account in firebase with specific email

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage(" Your Account is Successfully Created ");
                            updateUserInfo(name,pickedImgUri,auth.getCurrentUser());
                        }
                        else{
                            // fail
                            showMessage(" Something Went Wrong .. ! Sorry we cannot created account " + task.getException().getMessage().toString());
                            button.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    // update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // upload photo to firebase
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_photo");
        final StorageReference imagePAth = storageReference.child(pickedImgUri.getLastPathSegment());
        imagePAth.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded suceesfully
                // now we can get our image url
                imagePAth.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri cointains user image url

                        UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // user info updated succefully
                                            showMessage("Updated Successfully ");
                                           homeactivity();
                                        }
                                    }
                                });
                    }
                });

            }
        });

    }

    private void homeactivity() {
        Intent homeActivity= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);

        finish();
    }

    private void openGallery() {

        // open gallery intent and wait for user to pick the photo

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this,"Please accept require permission",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }

        }
        else{
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode== REQUESTCODE && data!=null){
            // the user has successfully picked the an image
            // we need to save this image
            pickedImgUri=data.getData();
            imageView.setImageURI(pickedImgUri);
        }
    }
}
