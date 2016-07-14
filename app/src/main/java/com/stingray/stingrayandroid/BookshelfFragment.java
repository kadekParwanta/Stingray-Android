package com.stingray.stingrayandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stingray.stingrayandroid.bookshelf.activity.CommonUtils;
import com.stingray.stingrayandroid.bookshelf.view.BookshelfView;
import com.stingray.stingrayandroid.bookshelf.xml.DownloadBooksTask;

/**
 * Created by Kadek_P on 7/14/2016.
 */
public class BookshelfFragment extends Fragment {

    private BookshelfView bsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bookshelf, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommonUtils.m_context = getActivity();
        bsView = (BookshelfView) view.findViewById(R.id.gridView1);
        initGrid(getResources().getConfiguration());

        (new DownloadBooksTask(CommonUtils.m_context, bsView)).execute();
    }

    protected void initGrid(Configuration config) {
        // Checks the orientation of the screen
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bsView.setNumColumns(4);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bsView.setNumColumns(3);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initGrid(newConfig);
    }
}
