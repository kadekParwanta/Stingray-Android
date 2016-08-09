package com.stingray.stingrayandroid;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stingray.stingrayandroid.Helper.ImageLoader;
import com.stingray.stingrayandroid.Model.Yearbook;
import com.stingray.stingrayandroid.View.CustomDynamicHeightImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.angrydroids.epub3reader.*;

/**
 * Created by Kadek_P on 7/15/2016.
 */
public class YearbookStoreAdapter extends ArrayAdapter<Yearbook> {

    private final ImageLoader imageLoader;
    Activity activity;
    int resource;
    List<Yearbook> datas;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private long enqueue;
    private DownloadManager dm;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public YearbookStoreAdapter(Activity activity, int resource, List<Yearbook> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.datas = objects;
        imageLoader = new ImageLoader(activity);

        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final YearbookHolder holder;
        final Yearbook data = datas.get(position);

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new YearbookHolder();
            holder.image = (CustomDynamicHeightImage)row.findViewById(R.id.image);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.description = (TextView)row.findViewById(R.id.description);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            holder.button = (Button) row.findViewById(R.id.button);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(downloadDir.getAbsolutePath()+"/"+ data.getId()+".epub");
                    if (file.exists()) {
                        Intent intent = new Intent(activity, it.angrydroids.epub3reader.MainActivity.class);
                        intent.putExtra(activity.getString(R.string.bpath),file.getAbsolutePath());
                        activity.startActivity(intent);
                    } else {
                        dm = (DownloadManager)activity.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(Constants.BASE_URL+data.getEpubUrl()));
                        request.setDescription(data.getSchool().getName());
                        request.setTitle("Downloading " + data.getSchool().getName() + " year " + data.getYear());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, data.getId()+".epub");

                        enqueue = dm.enqueue(request);
                    }


                }
            });

            row.setTag(holder);
        }
        else {
            holder = (YearbookHolder) row.getTag();
        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        row.setBackgroundResource(mBackgroundColors.get(backgroundIndex));


        imageLoader.DisplayImage(Constants.BASE_URL + data.getCoverUrl(), holder.image);

        holder.image.setHeightRatio(positionHeight);
        holder.title.setText(data.getSchool().getName());
        holder.description.setText(data.getYear() + "");

        return row;
    }

    static class YearbookHolder {
        CustomDynamicHeightImage image;
        TextView title;
        TextView description;
        ProgressBar progressBar;
        Button button;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("Yearbookstore", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
