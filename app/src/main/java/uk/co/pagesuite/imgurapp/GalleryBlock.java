package uk.co.pagesuite.imgurapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class GalleryBlock extends Fragment {
    /*
        Initalising global variables.
            posts       ArrayList of Sub objects containing parsed JSON data for display.
            adapter     The custom adapter for the GridView.
            grid        Global GridView reference.
            load        Holder for JSON download, temporary variable.
     */
    private ArrayList<Sub> posts = new ArrayList<Sub>();
    private GalleryAdapter adapter;
    private GridView grid;
    private JSONObject load;

    private static String cat;

    public GalleryBlock() {
        // Required empty public constructor
    }

    /*
        Intended to act as a non-default constructor, this method stores the category of this instance and assigns arguments for storing when initialised.
            @params
                category    The String category (case-sensitive) for this GalleryBlock instance.
     */
    public static final GalleryBlock newInstance(String category) {
        //Create a new GalleryBlock instance...
        GalleryBlock block = new GalleryBlock();

        //Create a new Bundle for arguments. It will only take 1.
        Bundle args = new Bundle(1);

        //Store the category as an argument and set the GalleryBlock to those arguments.
        args.putString("category", category);
        block.setArguments(args);

        //Return the new instance of the GalleryBlock.
        return block;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_block, container, false);

        //Fetch the stored category and store it in the global variable.
        cat = getArguments().getString("category");

        //Obtain a reference to the GridView in the layout...
        grid = (GridView) view.findViewById(R.id.gridView);

        //Start the initial download and refresh of content...
        refreshGallery();

        //Set the onItemClickListener for the GridView.
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //A simple onItemClick event, which creates a new intent, provides the arguments of image URL and title, before starting the PostViewMain activity.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create a new intent for the PostViewMain activity.
                Intent intent = new Intent(getActivity().getBaseContext(), PostViewMain.class);

                //Set arguments...
                intent.putExtra("image", posts.get(position).imageUrl);
                intent.putExtra("title", posts.get(position).title);
                intent.putExtra("upvotes", posts.get(position).upvotes);
                intent.putExtra("url", posts.get(position).url);

                //Start the activity with the intent.
                startActivity(intent);
            }
        });

        return view;
    }

    /*
            A method that is triggered when the download has finished, storing the JSON object returned, clearing the ArrayList of current posts and repopulating it with new Sub objects containing data from the new JSON.
         */
    private void finishedDownload()
    {
        //Log that the feed is being re-downloaded.
        Log.d("Debug", "Re-downloading feed.");

        //Initialise the temporary JSONArray feed and clear the posts ArrayList.
        JSONArray feed;
        posts.clear();

        //Attempt to load the JSON.
        try {
            //Fetch the data we're interested in from the JSON downloaded via opt (returns null if not found).
            feed = load.optJSONObject("data").optJSONArray("children");

            //Count how many entries there are.
            int count = feed.length();

            //Attempt to process and parse the data.
            for(int i = 0; i < count; i++) {
                //Store the current JSON object to be processed by fetching the object at position i and obtain the data of that child.
                JSONObject post = feed.optJSONObject(i).optJSONObject("data");

                //Store the preview content, which contains all the images.
                JSONObject preview = post.optJSONObject("preview");

                //Initialise empty url local variable.
                String url;

                //Check to see if the preview object is set to null. If so, the post has no image attached.
                if(preview != null) {
                    //If it isn't, then store the url of the source image.
                    url = preview.optJSONArray("images").optJSONObject(0).optJSONObject("source").optString("url");
                } else {
                    //So recover gracefully by setting the String to invalid (empty string causes exception on compareTo test later.)
                    url = "invalid";
                }

                //Create a new Sub instance, passing the obtained and parsed JSON data, such as ID, Title and Url of the image.
                Sub sub = new Sub(post.optString("id"), post.optString("title"), url, post.optInt("ups"), post.optString("url"));

                //Store the created Sub instance.
                posts.add(sub);
            }
        }
        catch (Exception e) {
            //If any exception is thrown, report to the log.
            Log.e("JSON Loader", "JSON failure.");
        }

        //Finally, reset the adapter to reload all available content.
        resetAdapter();
    }

    /*
        This method simply creates a new GalleryAdapter, passes the new posts data list, invalidates current views and then sets the adapter to reload all views with new content.
     */
    public void resetAdapter() {
        //Create a new GalleryAdapter with the context and data.
        if(isAdded()) {
            adapter = new GalleryAdapter(getActivity().getBaseContext(), posts);

            //Invalidate all current views in the GridView and pass the new adapter.
            grid.invalidateViews();
            grid.setAdapter(adapter);
        }
    }

    /*
        When this method is called, it downloads the latest feed directly and then calls finishedDownload once the onRespose listener fires. If an error occurs, it prints a stack trace.
     */
    public void refreshGallery() {
        //Create a new JsonObjectRequest instance, passing in the hardcoded String url and the case-sensitive category String. Supply the new listener and the error listener.
        Log.d("Debug", cat);

        JsonObjectRequest json = new JsonObjectRequest("https://www.reddit.com/r/" + cat + "/hot.json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //When a response is retrieved, fill load with the response data.
                load = response;

                //Call this method to take appropriate action to refresh content.
                finishedDownload();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Network Error", "Feed Load Failure");
            }
        });

        if(isAdded()) {
            //Fetch the custom ReqQueue to get the Volley RequestQueue and add the request for download.
            ReqQueue.getInstance(getActivity().getApplicationContext()).add(json);
        }

        //Reset the adapter to reload the views.
        resetAdapter();
    }
}
