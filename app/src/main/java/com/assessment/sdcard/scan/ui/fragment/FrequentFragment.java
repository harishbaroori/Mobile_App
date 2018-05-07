package com.assessment.sdcard.scan.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.assessment.sdcard.scan.ui.adapter.FrequencyListAdapter;
import com.assessment.sdcard.scan.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by harish on 05/02/2018.
 */
public class FrequentFragment extends Fragment {


    private String data;
    private ArrayList<Data> dataArrayList = new ArrayList<>();
    private ListView frequencyListView;

    private final int NUMBER_OF_RESULTS_TO_BE_SHOWN = 5;
    public FrequentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frequent, container, false);
        frequencyListView = (ListView)view.findViewById(R.id.frequency_listView);
        if(dataArrayList != null){
            setAdapter();
        }

        return view;
    }

    public void setData(ArrayList<Map.Entry<String, Integer>> sortedExtnsGroup){
        dataArrayList.clear();
        int j =0;
        for(Map.Entry<String, Integer> entry:sortedExtnsGroup){
            Data data = new Data(entry.getKey(), ""+entry.getValue());
            dataArrayList.add(data);
            if(++j >= NUMBER_OF_RESULTS_TO_BE_SHOWN){
                break;
            }
        }
        setAdapter();
    }

    private void setAdapter(){
        if(getContext() != null) {
            FrequencyListAdapter adapter = new FrequencyListAdapter(getContext(), dataArrayList);
            frequencyListView.setAdapter(adapter);
        }
    }


    public class Data{
        private String extnName;
        private String frequencyCount;

        Data(String extnName, String frequencyCount){
            this.extnName = extnName;
            this.frequencyCount = frequencyCount;
        }

        public String getExtnName(){
            return extnName;
        }

        public String getFrequencyCount(){
            return frequencyCount;
        }
    }
}
