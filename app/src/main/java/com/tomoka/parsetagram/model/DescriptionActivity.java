package com.tomoka.parsetagram.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tomoka.parsetagram.R;
import com.tomoka.parsetagram.TimelineActivity;

import java.io.File;

public class DescriptionActivity extends AppCompatActivity {

    TextView text_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        text_et = findViewById(R.id.text_et);
        //finish();
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                    Intent intent = new Intent(DescriptionActivity.this, TimelineActivity.class);
                    //intent.putExtra("id", post.getObjectId());
                    startActivity(intent);
                } else {
                    Log.e("HomeActivity", "Create post failed");
                    e.printStackTrace();
                }
            }
        });
        //return newPost;
    }

    public void onclick(View v) {
        String address = getIntent().getStringExtra("photofile");
        File file = new File(address);
        ParseFile photofile = new ParseFile(file);
        createPost(text_et.getText().toString(), photofile, ParseUser.getCurrentUser());
    }
}
