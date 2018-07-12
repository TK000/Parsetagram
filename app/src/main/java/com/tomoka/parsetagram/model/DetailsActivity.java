package com.tomoka.parsetagram.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseImageView;
import com.tomoka.parsetagram.R;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    TextView tvdUser;
    TextView tvdBody;
    ParseImageView ivdProfile;
    TextView favcount_tv;
    ImageView fav_iv;

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
        fav_iv = findViewById(R.id.fav_iv);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        //final int pos = getIntent().getIntExtra("pos",-1);

        try {
            tvdUser.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
            tvdBody.setText(post.getDescription());
            ivdProfile.setParseFile(post.getImage());
            ivdProfile.loadInBackground();

            if (post.getFavcount() > 0) {
                favcount_tv.setText(String.format("%s",post.getFavcount()));
            }
            else {
                favcount_tv.setText(String.format("%s",0));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        fav_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.getFavcount() > 0) {
                    final int currentfav = post.getFavcount();
                    post.setFavcount(currentfav+1);
                    favcount_tv.setText(String.format("%s",currentfav+1));
                    post.saveInBackground();
                }
                else {
                    post.setFavcount(1);
                    favcount_tv.setText(String.format("%s",1));
                    post.saveInBackground();
                }
            }
        });
    }
}
