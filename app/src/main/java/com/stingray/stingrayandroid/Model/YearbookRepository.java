package com.stingray.stingrayandroid.Model;

import com.strongloop.android.loopback.ModelRepository;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class YearbookRepository extends ModelRepository<Yearbook> {
    public YearbookRepository() {
        super("Yearbook", Yearbook.class);
    }
}
