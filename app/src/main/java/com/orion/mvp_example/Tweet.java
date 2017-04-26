package com.orion.mvp_example;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "tweets")
public class Tweet {
    // Annotated fields should have package-level visibility.
    @StorIOSQLiteColumn(name = "_id", key = true)
    long id;

    // Annotated fields should have package-level visibility.
    @StorIOSQLiteColumn(name = "author")
    String author;

    @StorIOSQLiteColumn(name = "content")
    String content;

    // Please leave default constructor with package-level visibility.
    Tweet() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}