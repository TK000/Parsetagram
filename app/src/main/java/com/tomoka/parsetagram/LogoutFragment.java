package com.tomoka.parsetagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class LogoutFragment extends Fragment {
    public Button logout_btn;
    public Button profilepic_btn;
    public ImageView ivPreview;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String profileFileName = "profilephoto.jpg";
    File profileFile;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_home, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ivPreview = (ImageView) view.findViewById(R.id.profilepic_iv);
        if (ParseUser.getCurrentUser().getParseFile("profilepicture") != null) {

            Glide.with(getContext())
                    .load(ParseUser.getCurrentUser().getParseFile("profilepicture").getUrl())
                    .into(ivPreview);
//            ivPreview.loadInBackground();
        }

        logout_btn = view.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        profilepic_btn = view.findViewById(R.id.profilepic_btn);
        profilepic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference to access to future access
                profileFile = getPhotoFileUri(profileFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", profileFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(((fragmentholder)getActivity()).getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(((fragmentholder)getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    ParseFile photoFile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(profileFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ivPreview.setImageBitmap(takenImage);
                //final ParseFile parsefile = new ParseFile(photoFile);
                //Post post = createPost("blah", parsefile, ParseUser.getCurrentUser());
                //Intent i = new Intent(TimelineActivity.this, DescriptionActivity.class);
                //i.putExtra("photofile", photoFile.getAbsolutePath());
                //startActivity(i);
                photoFile = new ParseFile(profileFile);
                ParseUser user = ParseUser.getCurrentUser();
                user.put("profilepicture", photoFile);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Glide.with(getContext())
                                .load(photoFile.getUrl())
                                .into(ivPreview);
                    }
                });

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

