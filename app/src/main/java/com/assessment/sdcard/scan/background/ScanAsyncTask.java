package com.assessment.sdcard.scan.background;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StatFs;
import android.util.Log;

import com.assessment.sdcard.scan.model.FileInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by harish on 05/02/2018.
 */


public class ScanAsyncTask extends AsyncTask<String, Integer, Void> {

    private ArrayList<FileInfo> nodesList = new ArrayList<>();
    private long totalUsedSize;
    private long scannedSize;
    private CallBack callBack;
    private final String TAG = "ScanAsyncTask";

    public ScanAsyncTask(CallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(this.callBack != null){
            callBack.onProgress(values[0]);
        }
    }

    @Override
    protected Void doInBackground(String... strings) {

        String rootDirPath = strings[0];
        init(rootDirPath);

        File rootDir = new File(rootDirPath);
        walkThrough(rootDir);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(this.callBack != null){
            if(this.isCancelled()){
                this.callBack.cancelled();
            } else {
                this.callBack.onDone(nodesList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(this.callBack != null){
            this.callBack.cancelled();
        }
    }

    private void init(String rootDirPath){
        nodesList.clear();
        totalUsedSize = getTotalSpaceUsed(rootDirPath);
        scannedSize = 0;
    }

    private void walkThrough(File dir){
            File[] children = dir.listFiles();
            for(File child : children){
                if(this.isCancelled()) {
                    break;
                }
                if(child.isDirectory()){
                    walkThrough(child);
                } else {
                    FileInfo fileInfo = new FileInfo(child.getAbsolutePath(), child.length(), child.getName());
                    nodesList.add(fileInfo);
                    sendProgressUpdate(fileInfo.getSize());
                }

            }
    }


    private long getTotalSpaceUsed(String rootDirPath){
        StatFs stat = new StatFs(rootDirPath);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            return (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize();//return value is in bytes
        } else {
            return (stat.getBlockCountLong() - stat.getAvailableBlocksLong()) * stat.getBlockSizeLong();
        }
    }


    private void sendProgressUpdate(long progressBytes){
        scannedSize = scannedSize + progressBytes;
        int progress = (int)((scannedSize / (float)totalUsedSize)*100);
        Log.d(TAG, "totalUsedSize = "+ totalUsedSize +" : scannedSize = "+scannedSize+" : "+"prgress = "+progress);
        publishProgress(progress);
    }


    public interface CallBack{
        void onProgress(int progress);
        void onDone(ArrayList<FileInfo> capturedData);
        void cancelled();
    }


}
