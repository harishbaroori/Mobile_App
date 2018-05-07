package com.assessment.sdcard.scan.model;

import android.util.Log;

/**
 * Created by harish on 05/02/2018.
 */

public class FileInfo {

    private String path;
    private long size;
    private String extn;

    public FileInfo(){

    }

    public FileInfo(String path, long size, String fileName){
        this.path = path;
        this.size = size;
        try {
            this.extn = fileName.substring(fileName.lastIndexOf("."));
        }catch (Exception e){
            Log.d("File ", "Error while getting extn for "+path);
            this.extn = "no_extn";
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtn() {
        return extn;
    }

    public void setExtn(String extn) {
        this.extn = extn;
    }
}
