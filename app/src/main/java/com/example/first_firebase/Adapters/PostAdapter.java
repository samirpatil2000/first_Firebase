package com.example.first_firebase.Adapters;




//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.example.first_firebase.Activities.HomeActivity;
//import com.example.first_firebase.Activities.ui.home.HomeFragment;
//import com.example.first_firebase.Models.Post;
//import com.example.first_firebase.R;
//
//import java.security.AccessControlContext;
//import java.sql.Array;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.zip.Inflater;
//
//import static java.security.AccessController.getContext;
//
////public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
////
////    private List<Post> gData ;
////    private Context context;
////
////    public PostAdapter(List<Post> gData, Context context) {
////        this.gData = gData;
////        this.context = context;
////    }
////
////    @NonNull
////    @Override
////    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View row =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post_item, parent, false);
////        MyViewHolder myViewHolder = new MyViewHolder(row);
////        return myViewHolder;
////    }
////    @Override
////    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
////        Glide.with(context).asBitmap();
////
////    }
////
////    @Override
////    public int getItemCount() {
////        return gData.size();
////    }
////
////    public class MyViewHolder extends RecyclerView.ViewHolder{
////
////        TextView postTitle;
////        ImageView imgPost;
////
////        public MyViewHolder(@NonNull View itemView) {
////            super(itemView);
////
////            postTitle=itemView.findViewById(R.id.row_postTitle);
////            imgPost=itemView.findViewById(R.id.row_post_image);
////
////        }
////    }
////
////
////}


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.first_firebase.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "recyclerViewAdapter";

    private ArrayList<String> name= new ArrayList<>();
    private ArrayList<String> images= new ArrayList<>();

    public PostAdapter(ArrayList<String> name, ArrayList<String> images, android.content.Context gContext) {
        this.name = name;
        this.images = images;
        this.gContext = gContext;
    }

    private Context gContext ;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // it will show how muck=h the item in list
        Log.d(TAG, "onBindViewHolder: called. ");

        Glide.with(gContext)
                .asBitmap()
                .load(images.get(position))
                .into(holder.image);
        holder.textView.setText(name.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Toast.makeText(gContext,name.get(position) ,Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView textView;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView=itemView.findViewById(R.i)
            textView = itemView.findViewById(R.id.textView);
            image=itemView.findViewById(R.id.imageView);
            parentLayout=itemView.findViewById(R.id.parent_layout);

        }
    }
}
