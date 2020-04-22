package com.example.first_firebase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.first_firebase.Models.Comment;
import com.example.first_firebase.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context gContext  ;
    List<Comment> gData;

    public CommentAdapter(Context gContext, List<Comment> comment) {
        this.gContext = gContext;
        this.gData = comment;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment,parent,false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.comments.setText(gData.get(position).getContent());
        holder.uname.setText(gData.get(position).getUname());

    }

    @Override
    public int getItemCount() {
        return gData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comments ,uname , date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            uname=itemView.findViewById(R.id.comment_username);

            comments=itemView.findViewById(R.id.comment_textView);

        }
    }
}
