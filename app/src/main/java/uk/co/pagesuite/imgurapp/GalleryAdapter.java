package uk.co.pagesuite.imgurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;

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
        NetworkImageView image;
        TextView title;
        String url;

        public void setImage(String urlL) {
            image.setImageUrl(urlL, ReqQueue.getiLoader());
            image.setDefaultImageResId(R.drawable.sample_0);
            image.setErrorImageResId(R.drawable.sample_1);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayHolder dh;
        Sub sub = posts.get(position);

        if(convertView == null) {
            dh = new DisplayHolder();
            convertView = inflater.inflate(R.layout.image_block, parent, false);
            dh.image = (NetworkImageView) convertView.findViewById(R.id.image);
            dh.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setLayoutParams(new GridView.LayoutParams(950, 850));
            convertView.setPadding(10, 10, 10, 10);

            convertView.setTag(dh);
        } else {
            dh = (DisplayHolder) convertView.getTag();
        }

        dh.url = sub.imageUrl;
        dh.setImage(sub.imageUrl);
        dh.title.setText(sub.title);

        return convertView;
    }
}
