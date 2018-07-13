package com.tomoka.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tomoka.parsetagram.model.Post;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity {

    public PostAdapter postAdapter;
    public ArrayList<Post> posts;
    public RecyclerView rvposts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        // find the RecyclerView
        rvposts = (RecyclerView) findViewById(R.id.rvposts);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        postAdapter = new PostAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvposts.setLayoutManager(new GridLayoutManager(this, 4));
        // set the adapter
        rvposts.setAdapter(postAdapter);
        populateTimeline();
    }

    private void populateTimeline() {
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Define our query conditions
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    for (int i = itemList.size()-1; i >= Math.max(0,itemList.size()-20); i--) {
                        // convert each object to a Tweet model
                        // add that Tweet model to our data source
                        // notify the adapter that we've added an item
                        Post post = itemList.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                    //String firstItemId = itemList.get(0).getObjectId();
                    //Toast.makeText(TimelineActivity.this, firstItemId, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}
