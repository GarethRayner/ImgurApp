package uk.co.pagesuite.imgurapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Sub> posts;
    private static LayoutInflater inflater;

    public GalleryAdapter(Context con, ArrayList<Sub> data) {
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

    public class DisplayHolder {
        ImageView image;
        TextView title;
        String url;
        ImageReq imageDown;

        public void setImage(Bitmap response) {
            //String oUrl = imageDown.getUrl();
            //if(oUrl.compareTo(url) == 0) {
                image.setImageBitmap(response);
            //} else {
            //    image.setImageBitmap(response);
           // }
        }

        public void downloadImage(Sub sub) {
            imageDown = new ImageReq(sub.imageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    setImage(response);
                }
            }, 500, 500, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            ReqQueue.getInstance(context).add(imageDown);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayHolder dh;
        Sub sub = posts.get(position);

        if(convertView == null) {
            dh = new DisplayHolder();
            convertView = inflater.inflate(R.layout.image_block, parent, false);
            dh.image = (ImageView) convertView.findViewById(R.id.image);
            dh.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setLayoutParams(new GridView.LayoutParams(950, 850));
            convertView.setPadding(10, 10, 10, 10);

            convertView.setTag(dh);
        } else {
            dh = (DisplayHolder) convertView.getTag();
        }

        dh.url = sub.imageUrl;
        Log.d("URL", dh.url);

        if(dh.url.substring(0, 4).compareTo("http") == 0) {
            dh.downloadImage(sub);
        } else {
            dh.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_0));
        }


        dh.title.setText(sub.title);

        return convertView;
    }
}
