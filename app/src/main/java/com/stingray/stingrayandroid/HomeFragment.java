package com.stingray.stingrayandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.stingray.stingrayandroid.Model.Yearbook;
import com.stingray.stingrayandroid.Model.YearbookRepository;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class HomeFragment extends Fragment {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slide1, R.drawable.slide2};
    Button yearbookBtn;
    ArrayList<Yearbook> yearbooks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadYearbooks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yearbookBtn = (Button) view.findViewById(R.id.yearbookBtn);
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(sampleImages.length);
    }



    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void loadYearbooks() {
        StingrayApplication application = (StingrayApplication) getActivity().getApplication();
        RestAdapter restAdapter = application.getLoopBackAdapter();
        YearbookRepository yearbookRepository = restAdapter.createRepository(YearbookRepository.class);
        yearbookRepository.findAll(new ListCallback<Yearbook>() {
            @Override
            public void onSuccess(List<Yearbook> objects) {
                yearbooks.clear();
                yearbooks.addAll(objects);
                for (Yearbook yearbook:yearbooks) {
                    Log.d("Yearbooks","book year = " + yearbook.getYear());
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e("Yearbooks","error = " + t.getLocalizedMessage());
            }
        });
    }
}
