package com.example.first_firebase.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    ImageView imageView;
    EditText nameEditText , emailEditText,passwordhEditText,confirmpassEditText;
    ProgressBar progressBar;
    Button button;
    private FirebaseAuth auth;



    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        imageView=findViewById(R.id.nav_user_image);
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

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT>=22){
//                    checkRequestForPermission();
//                }
//                else{
//                    openGallery();
//                }
//            }
//        });

    }

    private void createUserAccount(String email, final String name, String password) {
        // this method create user Account in firebase with specific email

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage(" Your Account is Successfully Created ");
                            updateUserInfo(name,auth.getCurrentUser());
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
    private void updateUserInfo(final String name,  final FirebaseUser currentUser) {
        // upload photo to firebase


        UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
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
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_photo");
//        final StorageReference imagePAth = storageReference.child(pickedImgUri.getLastPathSegment());
//        imagePAth.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // image uploaded suceesfully
//                // now we can get our image url
//                imagePAth.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        // uri cointains user image url
//
//
//                    }
//                });
//
//            }
//        });

    }

    private void homeactivity() {
        Intent homeActivity= new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(homeActivity);

        finish();
    }


}
