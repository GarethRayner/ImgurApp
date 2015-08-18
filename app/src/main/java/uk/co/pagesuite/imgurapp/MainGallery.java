package uk.co.pagesuite.imgurapp;

import android.app.Activity;
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

    /*Initalising global variables.
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

    //Custom onCreate. Set
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);

        refresher = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Refresher", "Refreshing images...");
                refreshGallery();
            }
        });

        grid = (GridView) findViewById(R.id.gridView);
        refreshGallery();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void finishedDownload()
    {
        Log.d("Debug", "Re-downloading feed.");

        JSONArray feed;
        posts.clear();

        try {
            feed = load.optJSONObject("data").optJSONArray("children");
            int count = feed.length();
            for(int i = 0; i < count; i++) {
                JSONObject temp = feed.optJSONObject(i);
                JSONObject post = temp.optJSONObject("data");

                JSONObject preview = post.optJSONObject("preview");
                String url;

                if(preview != null) {
                    url = preview.optJSONArray("images").optJSONObject(0).optJSONObject("source").optString("url");
                } else {
                    url = "invalid";
                }

                Sub sub = new Sub(post.optString("id"), post.optString("title"), url);
                posts.add(sub);
            }
        }
        catch (Exception e) {
            Log.e("JSON Loader", "JSON failure.");
        }

        resetAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refreshGallery();
            refresher.setRefreshing(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetAdapter() {
        adapter = new GalleryAdapter(this, posts);
        grid.invalidateViews();
        grid.setAdapter(adapter);
        refresher.setRefreshing(false);
    }

    public void refreshGallery() {
        JsonObjectRequest json = new JsonObjectRequest("https://www.reddit.com/r/gaming/hot.json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                load = response;
                finishedDownload();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        ReqQueue.getInstance(this).add(json);

        resetAdapter();
    }
}
