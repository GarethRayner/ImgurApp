package uk.co.pagesuite.imgurapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayHolder dh = new DisplayHolder();
        View gridBlock;

        if(convertView == null) {
            gridBlock = inflater.inflate(R.layout.image_block, parent, false);
            dh.image = (ImageView) gridBlock.findViewById(R.id.image);
            dh.title = (TextView) gridBlock.findViewById(R.id.title);

            dh.title.setText(posts.get(position).title);

            gridBlock.setLayoutParams(new GridView.LayoutParams(950, 850));
            gridBlock.setPadding(10, 10, 10, 10);
        } else {
            gridBlock = convertView;
        }

        //imageHolder.setImageResource(thumbs[position]);
        return gridBlock;
    }
}
