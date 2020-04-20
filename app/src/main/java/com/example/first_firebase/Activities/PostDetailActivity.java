package com.example.first_firebase.Activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.first_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imagePost;
    TextView textPostDesc,textPostAuthorName,textPostTitle ;
    EditText editTextComment ;
    Button addComment;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        imagePost = findViewById(R.id.postDetail_img);
        textPostAuthorName=findViewById(R.id.postDetailAuthorName);


        textPostTitle=findViewById(R.id.postDetail_title);
        textPostDesc=findViewById(R.id.postDetail_desc);


        editTextComment = findViewById(R.id.postdetail_comment);
        addComment = findViewById(R.id.postDetail_button);


        // let's set status bar to transperant Action bar is invisible
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();




        //User
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();


        // Now get here data from PostAdapter
        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imagePost);
        String title = getIntent().getExtras().getString("title");
        textPostTitle.setText(title);
        String postDesc = getIntent().getExtras().getString("postDesc");
        textPostDesc.setText(postDesc);
        String postKey = getIntent().getExtras().getString("postKey");

        String timestamp = getIntent().getExtras().getString("postDate");


        // set comment



    }

    private void timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);

        String Date;
        Date=String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", calendar);



    }

}
