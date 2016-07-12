package com.stingray.stingrayandroid;

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

    public CardAdapter(ArrayList<Yearbook> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardviewpost, parent, false);

        PostViewHolder dataObjectHolder = new PostViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getSchool().getName());
        holder.displayName.setText(""+mDataset.get(position).getYear());
        holder.content.setText("Rp. "+mDataset.get(position).getPrice());

        if (holder.image != null) {
            new DownLoadImageTask(holder.image).execute("https://stingray-id.herokuapp.com"+mDataset.get(position).getCoverUrl());
        }

        if (holder.avatar != null) {
            new DownLoadImageTask(holder.avatar).execute("http://gravatar.com/avatar/?s=400");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}