package uk.co.pagesuite.imgurapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
    private Context context;

    public GalleryAdapter(Context con) {
        context = con;
    }

    public int getCount() {
        return thumbs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageHolder;
        if(convertView == null) {
            imageHolder = new ImageView(context);
            imageHolder.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageHolder.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageHolder.setPadding(8 ,8 ,8 ,8);
        } else {
            imageHolder = (ImageView) convertView;
        }

        imageHolder.setImageResource(thumbs[position]);
        return imageHolder;
    }

    private Integer[] thumbs = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}
