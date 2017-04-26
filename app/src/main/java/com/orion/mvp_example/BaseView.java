package com.orion.mvp_example;

/**
 * Created by orion on 2.4.17.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
