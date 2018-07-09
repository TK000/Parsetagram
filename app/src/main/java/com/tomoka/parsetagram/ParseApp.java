package com.tomoka.parsetagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.tomoka.parsetagram.model.Post;

public class ParseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("tomokaAppId")
                .clientKey("tomokaMasterKey")
                .server("http://tomoka-parsetagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
