package com.assessment.sdcard.scan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.assessment.sdcard.scan.R;
import com.assessment.sdcard.scan.ui.fragment.TopSizesFragment;

import java.util.ArrayList;

/**
 * Created by harish on 05/02/2018.
 */

public class TopFilesListAdapter extends BaseAdapter {

    private ArrayList<TopSizesFragment.Data> frequencyData = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public TopFilesListAdapter(Context context, ArrayList<TopSizesFragment.Data> frequencyData){
        this.context = context;
        this.frequencyData = frequencyData;
        inflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return frequencyData.size();
    }

    @Override
    public Object getItem(int i) {
        return frequencyData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.top_files_list_item, viewGroup, false);
        }

        ((TextView)view.findViewById(R.id.top_files_sno)).setText(""+(i+1)+". ");
        ((TextView)view.findViewById(R.id.top_files_name)).setText(frequencyData.get(i).getFilePath());
        ((TextView)view.findViewById(R.id.top_files_size)).setText(frequencyData.get(i).getSize());

        return view;
    }
}
