package com.tomoka.parsetagram.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseImageView;
import com.tomoka.parsetagram.R;
import com.tomoka.parsetagram.TimeFormatter;

import org.parceler.Parcels;

import java.text.Format;
import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {

    TextView tvdUser;
    TextView tvdBody;
    ParseImageView ivdProfile;
    TextView favcount_tv;
    TextView tv_timestamp;

    Post post;
    //public PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvdUser = findViewById(R.id.tvdUser);
        tvdBody = findViewById(R.id.tvdBody);
        ivdProfile = findViewById(R.id.ivdProfile);
        favcount_tv = findViewById(R.id.favcount_tv);
        tv_timestamp = findViewById(R.id.tv_timestamp);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        //final int pos = getIntent().getIntExtra("pos",-1);

        try {
            tvdUser.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
            tvdBody.setText(post.getDescription());
            ivdProfile.setParseFile(post.getImage());
            ivdProfile.loadInBackground();
            Format formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            String dateString = formatter.format(post.getCreatedAt());
            String formattedTime = TimeFormatter.getTimeDifference(dateString);
            tv_timestamp.setText(formattedTime);

            if (post.getFavcount() > 0) {
                favcount_tv.setText(String.format("%s",post.getFavcount()));
            }
            else {
                favcount_tv.setText(String.format("%s",0));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
