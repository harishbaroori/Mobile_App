package com.assessment.sdcard.scan.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.assessment.sdcard.scan.model.FileInfo;
import com.assessment.sdcard.scan.R;
import com.assessment.sdcard.scan.ui.adapter.TopFilesListAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by harish on 05/02/2018.
 */
public class TopSizesFragment extends Fragment {


    private final int NUMBER_OF_RESULTS_TO_BE_SHOWN = 10;
    private ArrayList<Data> dataList = new ArrayList<>();
    private ListView topFilesListView;
    public TopSizesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_sizes, container, false);
        topFilesListView = (ListView)view.findViewById(R.id.top_files_listView);
        if(dataList != null){
            setAdapter();
        }
        return view;
    }


    public void setData(ArrayList<FileInfo> data){
        dataList.clear();
        for(int i=0; i<NUMBER_OF_RESULTS_TO_BE_SHOWN&&i<data.size(); i++){
            dataList.add(new Data(data.get(i).getPath(), readableFileSize(data.get(i).getSize())));
        }

        setAdapter();
    }

    private void setAdapter(){
        if(getContext() != null){
            TopFilesListAdapter adapter = new TopFilesListAdapter(getContext(), dataList);
            topFilesListView.setAdapter(adapter);
        }

    }

    private String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }



    public class Data {
        private String filePath;
        private String size;

        Data(String filePath, String size){
            this.filePath = filePath;
            this.size = size;
        }

        public String getFilePath(){
            return filePath;
        }

        public String getSize(){
            return size;
        }
    }
}
