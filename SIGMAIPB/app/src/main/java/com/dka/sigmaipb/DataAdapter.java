package com.dka.sigmaipb;

import java.io.InputStream;
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
        //holder.imageview.setImageResource(R.drawable.img_default);
        new DownloadImageTask(holder.imageview).execute(dataList.get(position).getImage());
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

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Sigma ", e.getMessage());
                e.printStackTrace();
                return null;
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if( result != null )
                bmImage.setImageBitmap(result);
            else{
                bmImage.setImageResource(R.drawable.img_default);
            }
        }

    }
}
