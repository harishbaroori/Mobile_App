package com.assessment.sdcard.scan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.assessment.sdcard.scan.R;
import com.assessment.sdcard.scan.ui.fragment.FrequentFragment;

import java.util.ArrayList;

/**
 * Created by harish on 05/02/2018.
 */

public class FrequencyListAdapter extends BaseAdapter {

    private ArrayList<FrequentFragment.Data> frequencyData = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public FrequencyListAdapter(Context context, ArrayList<FrequentFragment.Data> frequencyData){
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
            view = inflater.inflate(R.layout.frequency_list_item, viewGroup, false);
        }

        ((TextView)view.findViewById(R.id.frequency_sno)).setText(""+(i+1)+". ");
        ((TextView)view.findViewById(R.id.frequency_extn_name)).setText(frequencyData.get(i).getExtnName());
        ((TextView)view.findViewById(R.id.frequency_count)).setText(frequencyData.get(i).getFrequencyCount());

        return view;
    }
}
