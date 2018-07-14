package com.tomoka.parsetagram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.SaveCallback;
import com.tomoka.parsetagram.model.DetailsActivity;
import com.tomoka.parsetagram.model.Post;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.text.Format;
import java.text.SimpleDateFormat;
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

        View postView;
        if (context instanceof GridViewActivity) {
            postView = inflater.inflate(R.layout.grid_post, parent, false);
        } else {
            postView = inflater.inflate(R.layout.item_post, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Post post = mPosts.get(position);

        // populate the views according to this data

        //holder.tvUsername.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
        holder.ivProfileImage.setParseFile(post.getImage());
        holder.ivProfileImage.loadInBackground();

        if (context instanceof fragmentholder) {
            try {
                holder.tvBody.setText(post.getDescription());
                //holder.propic_iv.setPlaceholder(setImageResource(R.drawable.placeholder));
                holder.propic_iv.setParseFile(post.getUser().fetchIfNeeded().getParseFile("profilepicture"));
                holder.propic_iv.loadInBackground();
                Format formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
                String dateString = formatter.format(post.getCreatedAt());
                String formattedTime = TimeFormatter.getTimeDifference(dateString);
                holder.tv_createdAt.setText(formattedTime);
                holder.tvUsername.setText(String.format("%s", post.getUser().fetchIfNeeded().getUsername()));
                Log.d("PROPIC", "SUCCESS!");
            } catch (ParseException e) {
                Log.e("PROPIC", "FAIL");
                e.printStackTrace();
            }
        }

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
        public TextView tvUsername;
        public ParseImageView ivProfileImage;
        public RelativeLayout rLayout;
        public ParseImageView propic_iv;
        public TextView tv_createdAt;
        public ImageView fav_iv;
        public ImageView comment_iv;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            //ButterKnife.bind(this, itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.username_iv);
            ivProfileImage = (ParseImageView) itemView.findViewById(R.id.ivProfileImage);
            rLayout = (RelativeLayout) itemView.findViewById(R.id.rLayout);
            rLayout.setOnClickListener(this);

            if (context instanceof fragmentholder) {
                tvBody = (TextView) itemView.findViewById(R.id.tvBody);
                propic_iv = (ParseImageView) itemView.findViewById(R.id.propic_iv);
                tv_createdAt = itemView.findViewById(R.id.tvCreatedAt);
                fav_iv = itemView.findViewById(R.id.fav_iv);
                comment_iv = itemView.findViewById(R.id.comment_iv);
                propic_iv.setOnClickListener(this);
                tvUsername.setOnClickListener(this);
                fav_iv.setOnClickListener(this);
                comment_iv.setOnClickListener(this);
            }
        }


        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            final int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                final Post post = mPosts.get(position);
                // create intent for the new activity
                if (v == propic_iv || v == tvUsername) {
                    Intent intent = new Intent(context,GridViewActivity.class);
                    intent.putExtra("post",Parcels.wrap(post));
                    context.startActivity(intent);
                } else if (v == fav_iv) {
                    if (post.getFavcount() > 0) {
                        final int currentfav = post.getFavcount();
                        post.setFavcount(currentfav+1);
                        post.saveInBackground();
                    }
                    else {
                        post.setFavcount(1);
                        post.saveInBackground();
                    }
                    notifyItemChanged(position);
                } else if (v == comment_iv) {
                    //TODO
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Write a comment!");
                    //alert.setMessage("Message");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String newcomment = input.getText().toString();
                            JSONArray commentsarray = post.getJSONArray("comments");
                            if (commentsarray == null) {
                                commentsarray = new JSONArray();
                            }
                            commentsarray.put(newcomment);
                            post.put("comments",commentsarray);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    notifyItemChanged(position);
                                }
                            });
                            // Do something with value!
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(context,"Comment cancelled!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    alert.show();
                } else {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra("post", Parcels.wrap(post));
                    //intent.putExtra("pos", position);
                    //intent.putExtra(placeholder, config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath()));
                    // show the activity
                    context.startActivity(intent);
                }
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
