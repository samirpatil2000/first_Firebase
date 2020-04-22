package com.example.first_firebase.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.first_firebase.Adapters.CommentAdapter;
import com.example.first_firebase.Models.Comment;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imagePost;
    TextView textPostDesc,textPostAuthorName,textPostTitle ;
    EditText editTextComment ;
    Button addCommentButton;
    String postKey ;

    RecyclerView recyclerView_comment ;
    CommentAdapter commentAdapter ;
    List<Comment> comments ;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase ;

    static  String COMMENT_KEY ="Comment" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        imagePost = findViewById(R.id.postDetail_img);
        textPostAuthorName=findViewById(R.id.postDetailAuthorName);


        textPostTitle=findViewById(R.id.postDetail_title);
        textPostDesc=findViewById(R.id.postDetail_desc);


        editTextComment = findViewById(R.id.postdetail_comment);
        addCommentButton = findViewById(R.id.postDetail_button);

        recyclerView_comment = findViewById(R.id.recyclerView_comment);



        // let's set status bar to transperant Action bar is invisible
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();




        //User
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // add Comment buttom click listener

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentButton.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();

                Comment comment = new Comment(comment_content,uid,uname);
                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment Added");
                        editTextComment.setText("");
                        addCommentButton.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed To add Comment " + e.getMessage());
                    }
                });

            }
        });


        // Now get here data from PostAdapter
        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imagePost);
        String title = getIntent().getExtras().getString("title");
        textPostTitle.setText(title);
        String postDesc = getIntent().getExtras().getString("postDesc");
        textPostDesc.setText(postDesc);
        postKey = getIntent().getExtras().getString("postKey");

        String timestamp = getIntent().getExtras().getString("postDate");

        // set comment

        // init recycler View
        initRecyclerCommentView();


    }

    private void initRecyclerCommentView() {
//        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
//
        DatabaseReference databaseReference = firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                comments = new ArrayList<>();
//                for(DataSnapshot snap:dataSnapshot.getChildren()){
//                    Comment comment =  snap.getValue(Comment.class);
//                    comments.add(comment);
//                }
//             commentAdapter=new CommentAdapter(getApplicationContext(),comments);
//                recyclerView_comment.setAdapter(commentAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private void showMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();

    }

    private void timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);

        String Date;
        Date=String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", calendar);



    }

}
