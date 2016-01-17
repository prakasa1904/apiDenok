package com.dka.sigmaipb.peta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.dka.sigmaipb.R;
import com.dka.sigmaipb.cache.FileCache;
import com.dka.sigmaipb.cache.MemoryCache;
import com.dka.sigmaipb.cache.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by prakasa on 17/01/16.
 */
public class MyMarker {
    private Context context;
    private ImageView imgFrame;
    private String mLabel;
    private String mLokasi;
    private String mIcon;
    private Double mLatitude;
    private Double mLongitude;
    private Bitmap icon = null;

    public MyMarker(Context context, ImageView markerIcon, String label, String lokasi, String icon, Double latitude, Double longitude)
    {
        this.context = context;
        this.imgFrame = markerIcon;
        this.mLabel = label;
        this.mLokasi = lokasi;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mIcon = icon;
    }

    public String getmLabel()
    {
        return mLabel;
    }

    public void setmLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }

    public String getmLokasi(){
        return mLokasi;
    }

    public void setmLokasi(String mLokasi){
        this.mLokasi = mLokasi;
    }

    public String getmIcon()
    {
        return mIcon;
    }

    public void setmIcon(String icon)
    {
        this.mIcon = icon;
    }

    public void setBitmap(){
        if (this.imgFrame != null) {
            new DownloadImageTask(this.imgFrame).execute(this.mIcon);
        }
    }

    public Double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude)
    {
        this.mLongitude = mLongitude;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        FileCache fileCache;
        MemoryCache memoryCache = new MemoryCache();

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            this.fileCache = new FileCache(context);
        }

        protected Bitmap doInBackground(String... urls) {
            /* Define URL */
            String urldisplay = urls[0];

            if( urldisplay.length() < 1 ){
                return null;
            }else{
                /* Check URL in file exist or NOT ! */
                File f = this.fileCache.getFile(urldisplay);

                Bitmap b = decodeFile(f);
                if(b != null) return b;
                else {
                    try {
                        Bitmap bitmap = null;
                        URL uri = new URL(urldisplay);
                        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                        conn.setConnectTimeout(30000);
                        conn.setReadTimeout(30000);
                        conn.setInstanceFollowRedirects(true);

                        /* Check URL OK ? */
                        int status = conn.getResponseCode();
                        if(status == 200){
                            InputStream is = conn.getInputStream();
                            OutputStream os = new FileOutputStream(f);

                            /* Write Bitmap and URL to Cache */
                            Utils.CopyStream(is, os);

                            bitmap = decodeFile(f);
                            os.close();
                            conn.disconnect();

                            memoryCache.put(urldisplay, bitmap);

                            return bitmap;
                        }else{
                            return null;
                        }
                    } catch (Exception e) {
                        Log.e("Error Sigma Map ", e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        protected void onPostExecute(Bitmap result) {
            if( result != null ) this.bmImage.setImageBitmap(result);
            else this.bmImage.setImageResource(R.drawable.img_default);
        }

        private Bitmap decodeFile(File f){
            try {
                //Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                FileInputStream stream1=new FileInputStream(f);
                BitmapFactory.decodeStream(stream1,null,o);
                stream1.close();

                //Find the correct scale value. It should be the power of 2.

                // Set width/height of recreated image
                final int REQUIRED_SIZE=85;

                int width_tmp=o.outWidth, height_tmp=o.outHeight;
                int scale=1;
                while(true){
                    if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                        break;
                    width_tmp/=2;
                    height_tmp/=2;
                    scale*=2;
                }

                //decode with current scale values
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                FileInputStream stream2=new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                icon = bitmap;
                return icon;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
