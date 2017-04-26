package com.orion.mvp_example;

/**
 * Created by orion on 2.4.17.
 */

public class MainContract {
    interface Presenter extends BasePresenter {
        void loadingData();
    }

    interface View extends BaseView<Presenter> {
        void showProgessBar();
        void hidenProgressBar();
    }
}
