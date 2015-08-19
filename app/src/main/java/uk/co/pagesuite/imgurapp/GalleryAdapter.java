package uk.co.pagesuite.imgurapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

/*
    My custom BaseAdapter for a GridView. It takes the data provided and processes it appropriately. See methods for explanation.
 */
public class GalleryAdapter extends BaseAdapter {
    /*
        Initialise global variables.
            context     The context to be used for views etc.
            posts       The data to be used for populating the GridView. All Sub objects.
            inflater    A globally available inflater service.
     */
    private Context context;
    private ArrayList<Sub> posts;
    private static LayoutInflater inflater;

    /*
        Standard constructor, taking the context provided and storing it as well as storing the data taken.
            @params
                con     The context to be used for objects.
                data    The passed ArrayList of Sub objects for population of the GridView.
     */
    public GalleryAdapter(Context con, ArrayList<Sub> data) {
        //Set all the local variables, fetching an inflater service for processing layouts.
        context = con;
        posts = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return posts.size();
    }

    public Object getItem(int position) {
        return posts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /*
        A DisplayHolder, used for quick access and less performance hits during data processing. Also has inner class methods to solve issues, such as making sure each instance handles it's own data appropriately.
     */
    public class DisplayHolder {
        /*
            Class level variables.
                image   The NetworkImageView of this fragment.
                title   The TextView of this fragment.
         */
        NetworkImageView image;
        TextView title;

        /*
            This method is called to use the ImageLoader in ReqQueue to download images and process them. By it being called within an instance, only the instance will be involved. (Otherwise, processes call the method on the wrong displays.
         */
        public void setImage(String urlL) {
            //Set the image URL and fetch the ReqQueue ImageLoader. Also set two images for appropriate error handling.
            try {
                image.setImageUrl(urlL, ReqQueue.getiLoader());
                image.setDefaultImageResId(R.drawable.imgur_no_image);
                image.setErrorImageResId(R.drawable.imgur_no_image);
            } catch(Exception e) {
                Log.e("Error", "Error loading image.");
            }
        }
    }

    /*
        The main method of this adapter, it recycles views or creates them depending on the needs of the GridView.
            @params
                position        An integer representing the position of the view requested.
                convertView     The View to be converted. Can be null.
                parent          The ViewGroup that the View belongs to, used for context.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create a new DisplayHolder local variable and fetch the appropriate data Sub from the data list.
        DisplayHolder dh;
        Sub sub = posts.get(position);

        //If the convertView is null, it hasn't been initialised yet...
        if(convertView == null) {
            //So create a new DisplayHolder instance...
            dh = new DisplayHolder();

            //Inflate the view image_block which loads the appropriate layout. Provides parent for context.
            convertView = inflater.inflate(R.layout.image_block, parent, false);

            //Find the appropriate views for processing within the convertView and set them into the instance of DisplayHolder for easy access next time.
            dh.image = (NetworkImageView) convertView.findViewById(R.id.image);
            dh.title = (TextView) convertView.findViewById(R.id.title);

            //Set the appropriate styles to seperate each individual layout in the GridView, setting size and padding.
            convertView.setLayoutParams(new GridView.LayoutParams(950, 850));
            convertView.setPadding(10, 10, 10, 10);

            //Set the tag for future use.
            convertView.setTag(dh);
        } else {
            //If the convertView is not null, it's a recycled View, so simply give it a new View.
            dh = (DisplayHolder) convertView.getTag();
        }

        //Call setImage on this instance of DisplayHolder, making sure it handles it's own Views.
        dh.setImage(sub.imageUrl);
        //Set the title on the TextView held in this instance of DisplayHolder.
        dh.title.setText(sub.title);

        //Finally, return the View.
        return convertView;
    }
}
