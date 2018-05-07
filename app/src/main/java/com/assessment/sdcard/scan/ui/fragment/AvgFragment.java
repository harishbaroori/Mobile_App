package com.assessment.sdcard.scan.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assessment.sdcard.scan.R;

import org.w3c.dom.Text;

/**
 * Created by harish on 05/02/2018.
 */
public class AvgFragment extends Fragment {


    private String data;
    private TextView avgContentTextView;
    public AvgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_avg, container, false);
        avgContentTextView = (TextView) view.findViewById(R.id.avg_content_textView);
        if(data != null){
            setData(data);
        }
        return view;
    }

    public void setData(String content){
        this.data = content;
        avgContentTextView.setText(content);
    }

}
