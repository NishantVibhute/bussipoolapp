package com.bussinesspoolapp.bussipool.Adapter;

/**
 * Created by Nishant on 3/10/2018.
 */


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bussinesspoolapp.bussipool.R;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Bitmap[] imgid;
    private final int[] count;

    public CustomListAdapter(Activity context, String[] itemname, Bitmap[] imgid,int[] count) {
        super(context, R.layout.schemelist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.count = count;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.schemelist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView itemCount = (TextView) rowView.findViewById(R.id.itemCount);

        txtTitle.setText(itemname[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        Bitmap bitmap = imgid[position];

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();


        int newWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);

        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        imageView.setImageBitmap(bitmap);
        itemCount.setText(" "+count[position]+"");
        return rowView;

    };
}
