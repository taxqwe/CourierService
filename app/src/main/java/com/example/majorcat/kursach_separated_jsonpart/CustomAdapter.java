package com.example.majorcat.kursach_separated_jsonpart;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {

    private List<Response.ObjectsBean> ordersList;
    private Context mContext;
    private LayoutInflater inflater;


    public CustomAdapter(List<Response.ObjectsBean> ordersList, Context mContext){
        this.ordersList = ordersList;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.each_list_item, parent,false);

        Response.ObjectsBean order = (Response.ObjectsBean) getItem(position);
        ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
        TextView idOfOrder = (TextView) rowView.findViewById(R.id.idOfOrder);
        TextView addres = (TextView) rowView.findViewById(R.id.addres);
        TextView good = (TextView) rowView.findViewById(R.id.good);
        TextView price = (TextView) rowView.findViewById(R.id.price);

        if (order.getIsdelivered().equals("1")) {
            thumbnail.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources() ,R.drawable.delivered));
        } else thumbnail.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources() ,R.drawable.notdelivered));


        idOfOrder.setText(Integer.toString(order.getId()));
        addres.setText(order.getAdres());
        good.setText(order.getGood());
        price.setText(Integer.toString(order.getPrice()));
        return rowView;
    }
}
