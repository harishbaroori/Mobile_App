package com.assessment.sdcard.scan.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.assessment.sdcard.scan.ui.fragment.AvgFragment;
import com.assessment.sdcard.scan.model.FileInfo;
import com.assessment.sdcard.scan.ui.adapter.FileScanResultsPagerAdapter;
import com.assessment.sdcard.scan.ui.fragment.FrequentFragment;
import com.assessment.sdcard.scan.util.NotificationHelper;
import com.assessment.sdcard.scan.R;
import com.assessment.sdcard.scan.background.ScanAsyncTask;
import com.assessment.sdcard.scan.ui.fragment.TopSizesFragment;
import com.assessment.sdcard.scan.util.RunTimePermissionsHandler;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harish on 05/02/2018.
 */

public class MainActivity extends AppCompatActivity {

    private Button startScanButton;
    private Button stopScanButton;
    private ProgressBar scanProgressBar;
    private TextView scanStatusTextView;
    private ArrayList<FileInfo> allFileList = new ArrayList<>();
    private ScanAsyncTask scanAsyncTask;
    private Menu menu;


    private NotificationHelper notificationHelper;

    private TopSizesFragment topSizesFragment = new TopSizesFragment();
    private AvgFragment avgFragment = new AvgFragment();
    private FrequentFragment frequentFragment = new FrequentFragment();
    private int progressScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setClickListeners();

        RunTimePermissionsHandler.handleReadStoragePermission(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                String shareString = getTopSizeContent() + "\n" + getAvgContent() + "\n" + getFrequencyContentString(getFrequencyContent());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Send"));
        }
        return true;
    }

    private void init(){
        startScanButton = (Button) findViewById(R.id.start_scan_button);
        stopScanButton = (Button) findViewById(R.id.stop_scan_button);
        scanProgressBar = (ProgressBar) findViewById(R.id.scan_progress_bar);
        scanStatusTextView = (TextView) findViewById(R.id.scan_status_textView);

        stopScanButton.setEnabled(false);

        notificationHelper = new NotificationHelper(this);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        FileScanResultsPagerAdapter adapter = new FileScanResultsPagerAdapter(this, getSupportFragmentManager());

        adapter.addFragment(topSizesFragment, getString(R.string.top_file_sizes_tittle));
        adapter.addFragment(avgFragment, getString(R.string.avg_file_sizes_tittle));
        adapter.addFragment(frequentFragment, getString(R.string.frequent_file_extns_tittle));

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setClickListeners(){
        startScanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String rootDirPath = Environment
                        .getExternalStorageDirectory()
                        .getAbsolutePath();
                scanAsyncTask = new ScanAsyncTask(new ScanAsyncTask.CallBack(){
                    @Override
                    public void onDone(ArrayList<FileInfo> capturedData) {
                        allFileList = capturedData;

                        stopScanButton.setEnabled(false);
                        startScanButton.setEnabled(true);
                        scanStatusTextView.setText("Scanning Done...!!");
                        scanProgressBar.setProgress(100);
                        menu.findItem(R.id.share).setVisible(true);
                        notificationHelper.completed();

                        process();

                    }

                    @Override
                    public void onProgress(int progress) {
                        scanProgressBar.setProgress(progress);

                        if (progressScan < progress) {
                            progressScan = progress;
                            scanStatusTextView.setText("Scanning in Progress...!! - "+progress+"%");
                            notificationHelper.progressUpdate(progress);
                        }

                    }

                    @Override
                    public void cancelled() {

                        stopScanButton.setEnabled(false);
                        startScanButton.setEnabled(true);
                        scanStatusTextView.setText("Scanning Cancelled...!!");
                        notificationHelper.completed();
                    }
                });

                scanAsyncTask.execute(rootDirPath);
                scanStatusTextView.setText("Started Scanning...!!");
                startScanButton.setEnabled(false);
                stopScanButton.setEnabled(true);
                menu.findItem(R.id.share).setVisible(false);
                notificationHelper.createNotification();
                progressScan = 0;

            }
        });

        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scanAsyncTask != null){
                    scanAsyncTask.cancel(false);
                }

            }
        });
    }


    private void process(){
        Log.d("All Files: ", "number of files "+allFileList.size());
        sortByFileSize();

        topSizesFragment.setData(allFileList);

        String contentAvg = getAvgContent();
        avgFragment.setData(contentAvg);

        frequentFragment.setData(getFrequencyContent());
    }

    private String getTopSizeContent(){
        String content = "";
        for(int i=0; i< 10 && i<allFileList.size() ; i++){
            Log.d("TAG", allFileList.get(i).getPath()+" --- "+readableFileSize(allFileList.get(i).getSize()));
            content = content + allFileList.get(i).getPath()+" --- "+readableFileSize(allFileList.get(i).getSize()) + "\n";
        }
        return content;
    }

    private String getAvgContent(){

        String contentAvg = readableFileSize((int)calculateAvgFileSize(allFileList));
        Log.d("TAG", "avg size: "+contentAvg);

        return contentAvg;
    }

    private ArrayList<Map.Entry<String, Integer>> getFrequencyContent(){
        Map<String, Integer> extnsGroup = groupByCategoryType(allFileList);

        ArrayList<Map.Entry<String, Integer>> sortedExtnsGroup = (ArrayList<Map.Entry<String, Integer>>) sortDataOnExtnFrequency(extnsGroup);

        return sortedExtnsGroup;
    }


    private String getFrequencyContentString(ArrayList<Map.Entry<String, Integer>> sortedExtnsGroup){
        String content = "";
        int j =0;
        for(Map.Entry<String, Integer> entry:sortedExtnsGroup){
            Log.d("TAG", entry.getKey()+" --- "+entry.getValue());
            content = content + entry.getKey()+" --- "+entry.getValue() + "\n";
            if(++j > 5){
                break;
            }
        }
        return content;
    }


    private void sortByFileSize(){

        Collections.sort(allFileList, new Comparator<FileInfo>() {

            @Override
            public int compare(FileInfo fileInfo1, FileInfo fileInfo2) {
                if(fileInfo1.getSize() > fileInfo2.getSize()){
                    return -1;
                } else if(fileInfo1.getSize() < fileInfo2.getSize()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });

    }


    public String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public Map<String, Integer> groupByCategoryType(List<FileInfo> list) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (FileInfo o : list) {
            Integer count = map.get(o.getExtn());
            if (count == null) {
                count = 0;
            }
            map.put(o.getExtn(), ++count);
        }
        return map;
    }


    private List sortDataOnExtnFrequency(Map unsortedMap) {

        List list = new ArrayList(unsortedMap.entrySet());

        java.util.Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Map.Entry entry1 = (Map.Entry) o1;
                Map.Entry entry2 = (Map.Entry) o2;

                int size1 = (int)entry1.getValue();
                int size2 = (int)entry2.getValue();

                if(size1 > size2){
                    return -1;
                } else if(size1 < size2){
                    return 1;
                } else {
                    return 0;
                }

            }
        });

        return list;
    }


    private double calculateAvgFileSize(ArrayList<FileInfo> fileInfoList){
        double ans=0.0;
        for(int i = 0; i<fileInfoList.size() ; i++){
            ans=ans+fileInfoList.get(i).getSize() / fileInfoList.size();
        }
        return ans;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RunTimePermissionsHandler.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if(scanAsyncTask != null && !scanAsyncTask.isCancelled()){
            scanAsyncTask.cancel(true);
        }
        notificationHelper.completed();
        super.onBackPressed();
    }
}
