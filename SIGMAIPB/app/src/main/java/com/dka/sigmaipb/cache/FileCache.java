package com.dka.sigmaipb.cache;

import android.content.Context;

import java.io.File;

// Created by prakasa on 16/01/16.
public class FileCache {
    private File cacheDir;

    public FileCache(Context context){

        //Find the dir at SDCARD to save cached images

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
        {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
            final String dirCache = "/Android/data/" + context.getPackageName() + "/cache/";
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory().getPath() + dirCache);
        }
        else
        {
            // if checking on simulator the create cache dir in your application context
            cacheDir=context.getCacheDir();
        }

        if(!cacheDir.exists()){
            // create cache dir in your application context
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename=String.valueOf(url.hashCode());

        return new File(cacheDir, filename);

    }

}
