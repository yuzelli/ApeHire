package com.example.buiderdream.apehire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.entity.Teachnology;

import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/2/26.
 */

public class TeachListAdapter extends BaseAdapter {
    private  List<Teachnology> list = new ArrayList<Teachnology>();
    private Context context;
    private LayoutInflater inflater;

    public  TeachListAdapter(Context context){
        super();

        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public List<Teachnology> getList() {
        return list;
    }

    public void setList(List<Teachnology> list) {
        this.list = list;
    }
    public void addList(List<Teachnology> list){
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder vh = new ViewHolder();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_tech_arti,null);

            //vh.title_tv = (TextView) convertView.findViewById(R.id.item_tech_title_tv);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.title_tv.setText(list.get(position).getTitle());
        return convertView;
    }
    class ViewHolder{
         TextView title_tv;
    }
}
