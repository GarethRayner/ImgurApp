package uk.co.pagesuite.imgurapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainGallery extends Activity {

    /*
        Initalising global variables.
            posts       ArrayList of Sub objects containing parsed JSON data for display.
            adapter     The custom adapter for the GridView.
            grid        Global GridView reference.
            load        Holder for JSON download, temporary variable.
            refresher   SwipeRefreshLayout reference.
     */
    private ArrayList<Sub> posts = new ArrayList<Sub>();
    private GalleryAdapter adapter;
    private GridView grid;
    private JSONObject load;
    private SwipeRefreshLayout refresher;

    /*
        Custom onCreate. Sets content, then sets onRefreshListener for the SwipeRefresh, before finally setting the OnItemClick listener for the GridView and stating behaviour.
            @params
                    savedInstanceState      The Activity saved instance state information.
      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call the super to handle all create functions not dealt with here...
        super.onCreate(savedInstanceState);
        //Set the content view to be displayed.
        setContentView(R.layout.activity_main_gallery);

        //Obtain a reference to the SwipeRefreshLayout in the layout...
        refresher = (SwipeRefreshLayout) findViewById(R.id.refresh);

        //Set the onRefreshListener for it, creating a new listener.
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //A simple refresh event which outputs to the log that it is refreshing, before calling refreshGallery to reload and refresh.
            @Override
            public void onRefresh() {
                Log.d("Refresher", "Refreshing images...");
                refreshGallery();
            }
        });

        //Obtain a reference to the GridView in the layout...
        grid = (GridView) findViewById(R.id.gridView);

        //Start the initial download and refresh of content...
        refreshGallery();

        //Set the onItemClickListener for the GridView.
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //A simple onItemClick event, which creates a new intent, provides the arguments of image URL and title, before starting the PostViewMain activity.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create a new intent for the PostViewMain activity.
                Intent intent = new Intent(getApplicationContext(), PostViewMain.class);

                //Set arguments...
                intent.putExtra("image", posts.get(position).imageUrl);
                intent.putExtra("title", posts.get(position).title);

                //Start the activity with the intent.
                startActivity(intent);
            }
        });
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
                Sub sub = new Sub(post.optString("id"), post.optString("title"), url);

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
            refreshGallery();
            refresher.setRefreshing(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        This method simply creates a new GalleryAdapter, passes the new posts data list, invalidates current views and then sets the adapter to reload all views with new content.
     */
    public void resetAdapter() {
        //Create a new GalleryAdapter with the context and data.
        adapter = new GalleryAdapter(this, posts);

        //Invalidate all current views in the GridView and pass the new adapter.
        grid.invalidateViews();
        grid.setAdapter(adapter);

        //Set the refresher object to stop refreshing.
        refresher.setRefreshing(false);
    }

    /*
        When this method is called, it downloads the latest feed directly and then calls finishedDownload once the onRespose listener fires. If an error occurs, it prints a stack trace.
     */
    public void refreshGallery() {
        //Create a new JsonObjectRequest instance, passing in the hardcoded String url. Supply the new listener and the error listener.
        JsonObjectRequest json = new JsonObjectRequest("https://www.reddit.com/r/gaming/hot.json", new Response.Listener<JSONObject>() {
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
                //If an error occurs, simply print the stack trace.
                error.printStackTrace();
            }
        });

        //Fetch the custom ReqQueue to get the Volley RequestQueue and add the request for download.
        ReqQueue.getInstance(this).add(json);

        //Reset the adapter to reload the views.
        resetAdapter();
    }
}
