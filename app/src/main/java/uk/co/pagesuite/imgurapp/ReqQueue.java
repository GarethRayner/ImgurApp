package uk.co.pagesuite.imgurapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ReqQueue extends Application {
    /*
        Initialising global variables.
            downQueue   Globally available RequestQueue for downloading content.
            instance    The particular instance for an instantiated ReqQueue object.
            iLoader     Globally available ImageLoader for downloading images.
     */
    private RequestQueue downQueue;
    private static ReqQueue instance;
    private static ImageLoader iLoader;

    /*
        Standard onCreate method, it sets the particular instance of an instantiation, fetches the appropriate downQueue and constructs a new instance of an ImageLoader.
     */
    @Override
    public void onCreate() {
        //Permit the super to deal with unhandled methods.
        super.onCreate();

        //Set the particular instance of this instantiation.
        instance = this;

        //Fetch the RequestQueue for this object and store it in the global variable.
        downQueue = getDownQueue();

        //Create a new ImageLoader, passing it the RequestQueue available, a new ImageCache, complete with custom methods for fetching from the cache.
        iLoader = new ImageLoader(downQueue, new ImageLoader.ImageCache() {
            //Create a new LruCache with String key and Bitmap value. Set it to store 20 objects.
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                //Fetch the cached object using the url as a key.
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                //Set the cached object using the url as a key and a bitmap as a value.
                cache.put(url, bitmap);
            }
        });
    }

    /*
        Very simple method for fetching the particular instance of this ReqQueue.
     */
    public static synchronized ReqQueue getInstance(Context con)
    {
        return instance;
    }

    /*
        This method simply fetches a new RequestQueue or returns the existing one.
     */
    public RequestQueue getDownQueue() {
        if(downQueue == null) {
            downQueue = Volley.newRequestQueue(this);
        }
        return downQueue;
    }

    /*
        An implementation of the add method, setting the appropriate tag and adding it to the RequestQueue available.
            @params
                req     A Request object of type T, permitting the adding of any kind of request to the Volley RequestQueue.
            @return
                <T>     An object of type T that was requested.
     */
    public <T> void add(Request<T> req) {
        req.setTag("download");
        getDownQueue().add(req);
    }

    /*
        A simple method for getting the stored instance of an ImageLoader.
     */
    public static ImageLoader getiLoader() {
        return iLoader;
    }

    /*
        This method, when called, simply cancels all current downloads.
     */
    public void cancel() {
        downQueue.cancelAll("download");
    }
}
