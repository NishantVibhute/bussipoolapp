package com.bussinesspoolapp.bussipool.Adapter;

/**
 * Created by Nishant on 3/13/2018.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bussinesspoolapp.bussipool.R;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
    private final String[] gridcolor;

    public CustomGrid(Context c,String[] web,int[] Imageid ,String[] gridcolor) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;

        this.gridcolor=gridcolor;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            grid.setBackgroundColor(Color.parseColor(gridcolor[position]));
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
