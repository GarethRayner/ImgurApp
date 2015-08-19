package uk.co.pagesuite.imgurapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainGalleries extends FragmentActivity {
    /*
        Global variables.
            pageAdapter     The main Feed Adapter for the ViewPager.
            galleries       A list of GalleryBlock fragments for each gallery.
     */
    FeedPageAdapter pageAdapter;
    List<GalleryBlock> galleries;

    /*
        Standard onCreate, also has the ability to create and populate the list of GalleryBlock objects for each gallery, building fragment list and setting the ViewPager adapter.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Let the super handle all unhandled setup methods.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_galleries);

        //Create a new ArrayList of GalleryBlock objects to store in galleries for references.
        galleries = new ArrayList<GalleryBlock>();

        //Create and populate galleries with each instance of GalleryBlock for each feed category.
        galleries.add(GalleryBlock.newInstance("aww"));
        galleries.add(GalleryBlock.newInstance("EarthPorn"));
        galleries.add(GalleryBlock.newInstance("gaming"));
        galleries.add(GalleryBlock.newInstance("funny"));
        galleries.add(GalleryBlock.newInstance("gadgets"));
        galleries.add(GalleryBlock.newInstance("GetMotivated"));
        galleries.add(GalleryBlock.newInstance("space"));

        //Initialise the temporary storage of fragments.
        List<Fragment> galleryList = new ArrayList<Fragment>();

        //For each GalleryBlock instance, assign it into galleryList.
        for(GalleryBlock gallery : galleries) {
            galleryList.add(gallery);
        }

        //Set the FeedPageAdapter for the pageAdapter. This allows the ViewPager to obtain references to each GalleryBlock instance.
        pageAdapter = new FeedPageAdapter(getSupportFragmentManager(), galleryList);

        //Fetch the ViewPager from the view and set the adapter.
        ViewPager galleryDisp = (ViewPager) findViewById(R.id.gallery_slider);
        galleryDisp.setAdapter(pageAdapter);
    }

    /*
        Standard method for onCreate, inflating the menu. The only content should be the manual refresh.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_gallery, menu);
        return true;
    }

    /*
        Standard method for onOptionsSelected Listener. Simply takes appropriate action if selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            //If the user has clicked refresh, call refreshGallery and set the refresher object to refreshing.
            Toast toast = Toast.makeText(this, "Refreshing galleries...", Toast.LENGTH_SHORT);
            toast.show();
            refreshGalleries();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshGalleries() {
        //For each GalleryBlock instance, call refreshGallery on all of them.
        for(GalleryBlock gallery : galleries) {
            gallery.refreshGallery();
        }
    }
}