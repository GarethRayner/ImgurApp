package uk.co.pagesuite.imgurapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

public class PostViewMain extends FragmentActivity {
    private String image;
    private String url;
    private boolean zoomed;

    /*
        Standard onCreate event constructor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call the super to handle unhandled methods...
        super.onCreate(savedInstanceState);

        //Set the content view.
        setContentView(R.layout.activity_post_view_main);

        //Get a FragmentManager to handle operations.
        FragmentManager fm = getSupportFragmentManager();

        //Begin a transaction via the FragmentManager instance.
        FragmentTransaction postLoader = fm.beginTransaction();

        //Fetch the intent used to start this activity and fetch the stored data variables in Extra.
        Intent intent = getIntent();
        image = intent.getStringExtra("image");
        String caption = intent.getStringExtra("title");
        int upvotes = intent.getIntExtra("upvotes", 0);
        url = intent.getStringExtra("url");

        //Create a new PostView fragment instance.
        PostView post = new PostView();

        //Set the content by passing data variables.
        post.setContent(image, caption, upvotes);

        //Finally, find the FrameLayout placeholder and replace it with the new instance of a fragment.
        postLoader.replace(R.id.post_container, post);
        postLoader.commit();

        RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.view);

        wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(zoomed) {
            zoomed = false;
        }
    }

    public void zoom() {
        if(!zoomed) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            Fragment zoom = new ZoomedImage();
            Bundle args = new Bundle(1);
            args.putString("url", image);
            zoom.setArguments(args);

            transaction.addToBackStack("zoom");
            transaction.replace(R.id.post_container, zoom).commit();
            zoomed = true;
        }
    }

    public void viewB(View v) {
        Uri ur = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, ur);
        startActivity(intent);
    }
}
