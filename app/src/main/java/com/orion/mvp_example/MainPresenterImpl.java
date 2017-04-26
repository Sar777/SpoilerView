package com.orion.mvp_example;

import android.support.annotation.NonNull;

/**
 * Created by orion on 2.4.17.
 */

public class MainPresenterImpl implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenterImpl(@NonNull MainContract.View mView) {

        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadingData() {

    }
}
