package uk.co.pagesuite.imgurapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;

public class PostView extends Fragment {
    /*
        Initialise global variables.
            image       The image URL for the image to be loaded.
            caption     The caption text for this post.
            upvote      The upvotes for this post.
     */
    String image;
    String caption;
    int upvote;
    private Animator animator;
    private int shortAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //First of all, inflate the view with the layout post_block.
        View view = inflater.inflate(R.layout.post_block, container, false);

        //Find the NetworkImageView and TextView views for access.
        final NetworkImageView img = (NetworkImageView) view.findViewById(R.id.image);
        TextView cap = (TextView) view.findViewById(R.id.caption);

        //Set the url for the NetworkImageView, passing the url and the instance of ReqQueue's ImageLoader. Also set appropriate images for graceful recovery.
        img.setImageUrl(image, ReqQueue.getiLoader());
        img.setDefaultImageResId(R.drawable.imgur_no_image);
        img.setErrorImageResId(R.drawable.imgur_no_image);

        //Set the caption in the TextView along with the Upvotes.
        cap.setText(caption + "\n\n Upvotes: " + String.valueOf(upvote));

        shortAnim = getResources().getInteger(android.R.integer.config_shortAnimTime);

        //Finally, return the created view.
        return view;
    }

    /*
        A simple setter for storing the data passed to this fragment instance.
            @params
                url     The URL which the image resides at.
                cap     The caption for this post.
     */
    public void setContent(String url, String cap, int upvotes) {
        image = url;
        caption = cap;
        upvote = upvotes;
    }
}
