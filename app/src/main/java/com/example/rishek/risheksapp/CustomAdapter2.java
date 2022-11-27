package com.example.rishek.risheksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter2 extends BaseAdapter {
    Context context;
String[] languages;
    LayoutInflater inflter;
    public CustomAdapter2(Context applicationContext,String[]languages) {
        this.context = applicationContext;
        this.languages = languages;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return languages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinnerlist2, null);
        TextView names = view.findViewById(R.id.spinnerTextView2);
        names.setText(languages[i]);
        return view;
    }
}
