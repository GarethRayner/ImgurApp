package uk.co.pagesuite.imgurapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class PostViewMain extends FragmentActivity {

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
        String imUrl = intent.getStringExtra("image");
        String caption = intent.getStringExtra("title");
        int upvotes = intent.getIntExtra("upvotes", 0);

        //Create a new PostView fragment instance.
        PostView post = new PostView();

        //Set the content by passing both data variables.
        post.setContent(imUrl, caption, upvotes);

        //Finally, find the FrameLayout placeholder and replace it with the new instance of a fragment.
        postLoader.replace(R.id.post_container, post);
        postLoader.commit();
    }
}
