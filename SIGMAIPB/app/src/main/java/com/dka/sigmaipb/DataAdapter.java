package com.dka.sigmaipb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* Internal Libs */
import com.dka.sigmaipb.cache.*;

/**
 * Created by prakasa on 07/01/16.
 */
public class DataAdapter extends ArrayAdapter<Data>{
    ArrayList<Data> dataList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public DataAdapter(Context context, int resource, ArrayList<Data> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        dataList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.icon_images);
            holder.namaBarang = (TextView) v.findViewById(R.id.nama_barang);
            holder.merkType = (TextView) v.findViewById(R.id.merk_barang);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (holder.imageview != null) {
            new DownloadImageTask(holder.imageview).execute(dataList.get(position).getImage());
        }
        holder.namaBarang.setText(dataList.get(position).getName());
        holder.merkType.setText(dataList.get(position).getMerk());

        return v;
    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView namaBarang;
        public TextView merkType;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        FileCache fileCache;
        MemoryCache memoryCache = new MemoryCache();

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            this.fileCache = new FileCache(getContext());
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
                Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
