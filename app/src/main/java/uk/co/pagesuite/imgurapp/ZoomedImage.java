package uk.co.pagesuite.imgurapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

public class ZoomedImage extends Fragment {


    public ZoomedImage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.zoomed_image, container, false);
        String url = getArguments().getString("url");

        NetworkImageView image = (NetworkImageView) view.findViewById(R.id.image);

        image.setImageUrl(url, ReqQueue.getiLoader());
        image.setDefaultImageResId(R.drawable.imgur_no_image);
        image.setErrorImageResId(R.drawable.imgur_no_image);

        return view;
    }


}
