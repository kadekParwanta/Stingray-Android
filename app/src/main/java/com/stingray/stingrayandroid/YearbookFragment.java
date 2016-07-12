package com.stingray.stingrayandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stingray.stingrayandroid.Model.School;
import com.stingray.stingrayandroid.Model.Yearbook;
import com.stingray.stingrayandroid.Model.YearbookRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class YearbookFragment extends Fragment {

    ArrayList<Yearbook> yearbooks = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.yearbook, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadYearbooks();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.post_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(yearbooks, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadYearbooks() {
        StingrayApplication application = (StingrayApplication) getActivity().getApplication();
        RestAdapter restAdapter = application.getLoopBackAdapter();
        YearbookRepository yearbookRepository = restAdapter.createRepository(YearbookRepository.class);
        yearbookRepository.findAll(new ListCallback<Yearbook>() {
            @Override
            public void onSuccess(List<Yearbook> objects) {
                yearbooks.clear();
                for (Yearbook yearbook : objects) {
                    String coverUrl = yearbook.get("cover_url").toString();
                    yearbook.setCoverUrl(coverUrl);
                    String epubUrl = yearbook.get("epub_url").toString();
                    yearbook.setEpubUrl(epubUrl);

                    HashMap school = (HashMap) yearbook.get("school");
                    School school1 = new School();
                    String id = school.get("id").toString();
                    String phone = school.get("phone").toString();
                    String name = school.get("name").toString();
                    String address = school.get("address").toString();
                    school1.setAddress(address);
                    school1.setId(id);
                    school1.setName(name);
                    school1.setPhone(phone);

                    yearbook.setSchool(school1);
                    yearbooks.add(yearbook);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("Yearbooks", "error = " + t.getLocalizedMessage());
            }
        });
    }
}
