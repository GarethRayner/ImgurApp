package uk.co.pagesuite.imgurapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public class FeedPageAdapter extends FragmentPagerAdapter {
    /*
        Global variables.
            feeds   The List of GalleryBlock references to be used for populating the ViewPager in MainGalleries.
     */
    private List<Fragment> feeds;

    /*
        The constructor for this adapter.
            @params
                fm          The FragmentManager for this adapter.
                feedData    The passed references for the created GalleryBlock objects.
     */
    public FeedPageAdapter(FragmentManager fm, List<Fragment> feedData) {
        //Call the super to handle construction...
        super(fm);

        //Store the passed references.
        feeds = feedData;
    }

    @Override
    public Fragment getItem(int position) {
        return feeds.get(position);
    }

    @Override
    public int getCount() {
        return feeds.size();
    }
}
