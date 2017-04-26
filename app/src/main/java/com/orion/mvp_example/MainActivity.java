package com.orion.mvp_example;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.pushtorefresh.storio.Queries;
import com.pushtorefresh.storio.contentresolver.queries.Query;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenterImpl(this);
        mPresenter.subscribe();

        mButton = (Button)findViewById(R.id.btn_ok);
        final StorIOSQLite storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(App.getDatabase())
                .addTypeMapping(Tweet.class, SQLiteTypeMapping.<Tweet>builder()
                        .putResolver(new TweetStorIOSQLitePutResolver()) // object that knows how to perform Put Operation (insert or update)
                        .getResolver(new TweetStorIOSQLiteGetResolver()) // object that knows how to perform Get Operation
                        .deleteResolver(new TweetStorIOSQLiteDeleteResolver())  // object that knows how to perform Delete Operation
                        .build())
                .build();

        storIOSQLite.get().listOfObjects(Tweet.class).withQuery(Query.builder().uri(Uri.EMPTY).build()).prepare().asRxObservable().su
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showProgessBar() {

    }

    @Override
    public void hidenProgressBar() {

    }
}
