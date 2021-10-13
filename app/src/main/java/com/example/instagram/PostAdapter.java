package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private List<Comments> commentsList;
    private ImageView ivProfile_Picture;

    public PostAdapter(Context context, List<Post> posts, List<Comments> commentsList) {
        this.context = context;
        this.posts = posts;
        this.commentsList =  commentsList;
    }

    public void setNewPhoto(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post  = posts.get(position);

        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LikeButton likeButton;
        private TextView tvUserName;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView Date;

        private TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comment);
            tvUserName = itemView.findViewById(R.id.username);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.post_image);
            Date = itemView.findViewById(R.id.tvDate);
            ivProfile_Picture = itemView.findViewById(R.id.image_profile);
            likeButton = itemView.findViewById(R.id.like_button);

        }

        public void bind(Post post) {

            ParseUser parseUser = ParseUser.getCurrentUser();
            String obejctID = parseUser.getObjectId();

            Comments comments = new Comments();
            comment.setText(comments.getComment());
            Date.setText(post.getDate());
            tvDescription.setText(post.getDescription());
            tvUserName.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null ){
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            ParseFile profile_image =  post.getProfile_picture();
            if(profile_image != null){
                Glide.with(context).load(post.getProfile_picture().getUrl()).circleCrop().into(ivProfile_Picture);
            }

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("User", post.getUser());
                    context.startActivity(i);
                }
            });
            tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("User", post.getUser());
                    context.startActivity(i);
                }
            });
        }
    }
}
