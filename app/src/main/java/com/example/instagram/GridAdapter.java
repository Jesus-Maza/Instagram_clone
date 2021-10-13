package com.example.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<Post> posts;

    public GridAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Post post = posts.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.card_view, null);
        }

        ImageView ivPhoto = view.findViewById(R.id.ivImage2);
        TextView tvdescription =  view.findViewById(R.id.tvdescription);
        TextView date_iv = view.findViewById(R.id.date_iv);
        TextView username_iv = view.findViewById(R.id.username_iv);

        username_iv.setText(post.getUser().getUsername());
        date_iv.setText(post.getDate());
        tvdescription.setText(post.getDescription());


        ParseFile image = post.getImage();
        if(image != null ){
            Glide.with(context).load(post.getImage().getUrl()).into(ivPhoto);
        }





        return view;
    }
}
