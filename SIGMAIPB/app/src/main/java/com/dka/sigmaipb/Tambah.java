package com.dka.sigmaipb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import android.view.View;

import org.json.JSONObject;

public class Tambah extends AppCompatActivity{

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "SIGMA IPB";

    private Uri fileUri; // file url to store image/video
    private static final String JSON_URL = "http://192.168.43.173/info.php";
    public ArrayList<String> dataForm = new ArrayList<String>();

    Spinner sp;
    private ImageView imgPreview;
    private Button btnAmbilGambar;
    private Button btnGallery;
    private Button saveData;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    // Post data
    public EditText idData, namaData;

    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        sp = (Spinner) findViewById(R.id.ruangan);
        btnAmbilGambar = (Button) findViewById(R.id.btnAmbilGambar);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        saveData = (Button) findViewById(R.id.saveData);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Untuk membuat list ruangan
        List<String> item = new ArrayList<String>();
        item.add("Ruang Baca");
        item.add("Ruang Lab 01");

        // Untuk membuat adapter list ruangan
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tambah.this,android.R.layout.simple_spinner_dropdown_item,item);

        // Untuk menentukan model adapternya
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Untuk menerapkan adapter pada spinner
        sp.setAdapter(adapter);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        saveData.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Tambah.this);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    idData = (EditText) findViewById(R.id.kode_barang);
                                    namaData = (EditText) findViewById(R.id.nama_barang);

                                    String valueId = idData.getText().toString();
                                    dataForm.add(valueId);
                                    String valueNama = namaData.getText().toString();
                                    dataForm.add(valueNama);
                                    addData(JSON_URL, dataForm);
                                }
                            });
                        alertDialogBuilder.create().show();
                    }
                });
    }

    // load image from gallery
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    // file download simulator... a really simple
    public int doSomeTasks() {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
            // ...add your own

        }

        return 100;

    }

    // Checking device has camera hardware or not
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    // Capturing Camera Image will lauch camera app requrest image capture
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    // Here we store the file url as it will be null after returning from camera app
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    // Receiving activity result method will be called after closing the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgPreview = (ImageView) findViewById(R.id.imgPreview);
                // Set the Image in ImageView after decoding the String
                imgPreview.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // Display image from a path to ImageView
    private void previewCapturedImage() {
        try {
            imgPreview.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 3;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imgPreview.setImageBitmap(bitmap);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // ------------ Helper Methods ----------------------
    //Creating file uri to store image/video
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // returning image
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else {
            return null;
        }
        return mediaFile;
    }

    private void addData(String url, final ArrayList arrayList) {
        class addData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            public ArrayList<String> arr = new ArrayList<String>();
            public addData(ArrayList arrayList) {
                arr = arrayList;
            }

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tambah.this, "Harap Tunggu...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setUseCaches(false);
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    StringBuffer requestParams = new StringBuffer();
                    for (int i = 0; i < arr.size(); i++) {
                        requestParams.append(URLEncoder.encode(String.valueOf(i), "UTF-8"));
                        requestParams.append("=").append(URLEncoder.encode(arr.get(i), "UTF-8"));
                        requestParams.append("&");
                    }

                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                    writer.write(requestParams.toString());
                    writer.flush();
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
                    con.disconnect();
                } catch (Exception ex) {
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }
        }
        addData add = new addData(arrayList);
        add.execute(url);
    }
}
