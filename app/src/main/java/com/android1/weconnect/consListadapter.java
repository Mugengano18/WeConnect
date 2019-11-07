package com.android1.weconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class consListadapter extends ArrayAdapter<Counsellor> {
    Context context;
    private List<Counsellor>counselist;


    public consListadapter(@NonNull Context context,List<Counsellor> counselist) {
        super(context,R.layout.saved_list,counselist);
        this.context = context;
        this.counselist = counselist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.saved_list,null);
        return super.getView(position, convertView, parent);
    }
}
