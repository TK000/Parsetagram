package com.tomoka.parsetagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tomoka.parsetagram.model.Post;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    public PostAdapter postAdapter;
    public ArrayList<Post> posts;
    public RecyclerView rvposts;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_timeline, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // find the RecyclerView
        rvposts = (RecyclerView) view.findViewById(R.id.rvposts);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        postAdapter = new PostAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvposts.setLayoutManager(linearLayoutManager);
        // set the adapter
        rvposts.setAdapter(postAdapter);
        populateTimeline();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvposts.addOnScrollListener(scrollListener);

    }

    public void loadNextDataFromApi(int offset) {
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Define our query conditions
        //query.whereEqualTo("user", ParseUser.getCurrentUser());
        final int pos = offset*20;
        // Execute the find asynchronously
        query.orderByAscending("createdAt").findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    ((fragmentholder)getActivity()).showProgressBar();
                    if (itemList.size()-pos-1 > 0){
                        for (int i = itemList.size()-pos-1; i >= Math.max(0,itemList.size()-20-pos); i--) {
                            // convert each object to a Tweet model
                            // add that Tweet model to our data source
                            // notify the adapter that we've added an item
                            Post post = itemList.get(i);
                            posts.add(post);
                            postAdapter.notifyItemInserted(posts.size() - 1);
                        }
                    }
                    ((fragmentholder)getActivity()).hideProgressBar();
                    //String firstItemId = itemList.get(0).getObjectId();
                    //Toast.makeText(TimelineActivity.this, firstItemId, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void populateTimeline() {
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Define our query conditions
        // Execute the find asynchronously
        query.orderByAscending("createdAt").findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    ((fragmentholder)getActivity()).showProgressBar();
                    for (int i = itemList.size()-1; i >= Math.max(0,itemList.size()-20); i--) {
                        // convert each object to a Tweet model
                        // add that Tweet model to our data source
                        // notify the adapter that we've added an item
                        Post post = itemList.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size() - 1);
                    }
                    ((fragmentholder)getActivity()).hideProgressBar();
                    //String firstItemId = itemList.get(0).getObjectId();
                    //Toast.makeText(TimelineActivity.this, firstItemId, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        postAdapter.clear();
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Define our query conditions
        //query.whereEqualTo("user", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        query.orderByAscending("createdAt").findInBackground(new FindCallback<Post>() {
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
                    swipeContainer.setRefreshing(false);
                    //String firstItemId = itemList.get(0).getObjectId();
                    //Toast.makeText(TimelineActivity.this, firstItemId, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}
