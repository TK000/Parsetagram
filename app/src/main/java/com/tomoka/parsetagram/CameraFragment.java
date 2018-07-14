package com.tomoka.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tomoka.parsetagram.model.Post;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    TextView text_et;
    ImageView imageView;
    Button description_btn;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_description, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        description_btn = view.findViewById(R.id.description_btn);
        text_et = view.findViewById(R.id.text_et);
        imageView = view.findViewById(R.id.imageView);
        text_et.setText("");

        description_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //text_et = ((fragmentholder)getActivity()).findViewById(R.id.text_et);
                createPost(text_et.getText().toString(), photofile, ParseUser.getCurrentUser());
            }
        });

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(((fragmentholder)getActivity()).getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
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

    ParseFile photofile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                //ivPreview.setImageBitmap(takenImage);
                //final ParseFile parsefile = new ParseFile(photoFile);
                //Post post = createPost("blah", parsefile, ParseUser.getCurrentUser());
                //Intent i = new Intent(TimelineActivity.this, DescriptionActivity.class);
                //i.putExtra("photofile", photoFile.getAbsolutePath());
                //startActivity(i);
                photofile = new ParseFile(photoFile);
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                imageView.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onclick(View v) {
        text_et = ((fragmentholder)getActivity()).findViewById(R.id.text_et);
        ((fragmentholder)getActivity()).showProgressBar();
        createPost(text_et.getText().toString(), photofile, ParseUser.getCurrentUser());
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
                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    ((fragmentholder)getActivity()).hideProgressBar();
                    text_et.setVisibility(View.INVISIBLE);
                    description_btn.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("HomeActivity", "Create post failed");
                    e.printStackTrace();
                }
            }
        });
        //return newPost;
    }

}
