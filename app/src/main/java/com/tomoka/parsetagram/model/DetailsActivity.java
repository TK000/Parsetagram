package com.tomoka.parsetagram.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.tomoka.parsetagram.R;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    TextView tvdUser;
    TextView tvdBody;
    ImageView ivdProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvdUser = findViewById(R.id.tvdUser);
        tvdBody = findViewById(R.id.tvdBody);
        ivdProfile = findViewById(R.id.ivdProfile);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        try {
            tvdUser.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
            tvdBody.setText(post.getDescription());
            //ivdProfile.setParseFile(post.getImage());
            //ivdProfile.loadInBackground();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
