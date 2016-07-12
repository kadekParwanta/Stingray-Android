package com.stingray.stingrayandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stingray.stingrayandroid.Helper.ImageLoader;
import com.stingray.stingrayandroid.Model.Yearbook;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class CardAdapter extends  RecyclerView.Adapter<CardAdapter
        .PostViewHolder> {
    private final ArrayList<Yearbook> mDataset;
    private final Activity mActivity;
    private ImageLoader imageLoader;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView displayName, dateTime, label, content;
        ImageView avatar, image;

        public PostViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            displayName = (TextView)itemView.findViewById(R.id.displayName);
            dateTime = (TextView)itemView.findViewById(R.id.dateTime);
            label = (TextView)itemView.findViewById(R.id.label);
            content = (TextView)itemView.findViewById(R.id.content);
            avatar = (ImageView)itemView.findViewById(R.id.avatar);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }

    public CardAdapter(ArrayList<Yearbook> myDataset, Activity activity) {
        mDataset = myDataset;
        mActivity = activity;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardviewpost, parent, false);

        PostViewHolder dataObjectHolder = new PostViewHolder(view);
        imageLoader = new ImageLoader(mActivity);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.displayName.setText(mDataset.get(position).getSchool().getName());
        holder.dateTime.setText(""+mDataset.get(position).getYear());
        holder.label.setText("Rp. "+mDataset.get(position).getPrice());

        if (holder.avatar != null) {
            imageLoader.DisplayImage("https://stingray-id.herokuapp.com"+mDataset.get(position).getCoverUrl(), holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}