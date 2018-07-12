package com.tomoka.parsetagram;

import android.arch.paging.DataSource;

import com.tomoka.parsetagram.model.Post;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Post> {

    @Override
    public DataSource<Integer, Post> create() {
        ParsePositionalDataSource source = new ParsePositionalDataSource();
        return source;
    }
}
