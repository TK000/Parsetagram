package com.tomoka.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.tomoka.parsetagram.model.DetailsActivity;
import com.tomoka.parsetagram.model.Post;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public List<Post> mPosts;
    Context context;
    // pass in the Tweets array in the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Post post = mPosts.get(position);

        // populate the views according to this data

        //holder.tvUsername.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
        holder.tvBody.setText(post.getDescription());
        holder.ivProfileImage.setParseFile(post.getImage());
        holder.ivProfileImage.loadInBackground();

        //Log.i("TAG", String.format("%s", post.getMedia().getUrl()));
        //holder.tvTimestamp.setText(tweet.createdAt);

        //Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        //Glide.with(context).load(tweet.mediaUrl).into(holder.tvImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        @BindView(R.id.ivProfileImage) public ImageView ivProfileImage;
//        @BindView(R.id.tvUserName) public TextView tvUsername;
//        @BindView(R.id.tvBody) public TextView tvBody;
//        @BindView(R.id.tvTimestamp) public TextView tvTimestamp;
//        @BindView(R.id.tvImage) public ImageView tvImage;
//        @BindView(R.id.rLayout) public RelativeLayout rLayout;
//        @BindView(R.id.ivReply) public ImageView ivReply;

        public TextView tvBody;
        //public TextView tvUsername;
        public ParseImageView ivProfileImage;
        public RelativeLayout rLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            //ButterKnife.bind(this, itemView);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            //tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            ivProfileImage = (ParseImageView) itemView.findViewById(R.id.ivProfileImage);
            rLayout = (RelativeLayout) itemView.findViewById(R.id.rLayout);

            rLayout.setOnClickListener(this);
            //ivReply.setOnClickListener(this);
        }


        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra("post", Parcels.wrap(post));
                //intent.putExtra(placeholder, config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath()));
                // show the activity
                context.startActivity(intent);
                }
            }

        }


    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
