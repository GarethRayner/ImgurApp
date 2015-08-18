package uk.co.pagesuite.imgurapp;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class ImageReq extends ImageRequest {
    public String urlReq;

    public ImageReq(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                    ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        urlReq = url;
    }
}
