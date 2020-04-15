package com.example.first_firebase.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import com.example.first_firebase.R;
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

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    Dialog popCreatePost ;
    ImageView popAddImageView ,popCreateImagebtn;
    EditText popupCreateTitleEditText , popupCreateDescEditText ;
    ProgressBar popupProgerssBar;



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

    private void inPopup() {
        popCreatePost=new Dialog(this) ;
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
