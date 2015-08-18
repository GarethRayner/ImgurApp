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
    private Context context;
    private RequestQueue downQueue;
    private static ReqQueue instance;
    private ImageLoader iLoader;

    public ReqQueue()
    {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        downQueue = getDownQueue();

        iLoader = new ImageLoader(downQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /*public ReqQueue(Context con) {
        context = con;
        instance = this;
        downQueue = getDownQueue();

        iLoader = new ImageLoader(downQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }*/

    public static synchronized ReqQueue getInstance(Context con)
    {
        return instance;
    }

    public RequestQueue getDownQueue() {
        if(downQueue == null) {
            downQueue = Volley.newRequestQueue(this);
        }
        return downQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag("download");
        getDownQueue().add(req);
    }

    public ImageLoader getiLoader() {
        return iLoader;
    }

    public void cancel() {
        downQueue.cancelAll("download");
    }
}
