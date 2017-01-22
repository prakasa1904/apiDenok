package com.dka.sigmaipb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tambah extends AppCompatActivity{

    // Activity request codes
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public String imagepath = null;

    private Uri fileUri; // file url to store image/video
    private static final String GET_URL = "http://172.20.10.4/SigmaIpb/api/get_lokasi";
    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/add";
    public ArrayList<String> dataForm = new ArrayList<>();

    Spinner sp;
    private TextView textLat, textLon;
    public ImageView imageview;

    // Post data
    public EditText idData, namaData, merkData, tahunData, hargaData, sumberData, latData, longData;
    public Spinner ruangData;

    Exif exif;

    /* Data Spinner */
    JSONArray str_json = null;
    public List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        imageview = (ImageView)findViewById(R.id.imgPreview);

        sp = (Spinner) findViewById(R.id.ruangan);
        Button btnAmbilGambar = (Button) findViewById(R.id.btnAmbilGambar);
        Button saveData = (Button) findViewById(R.id.saveData);

        textLat  = (TextView)findViewById(R.id.latitude);
        //textlat.setEnabled(false);
        textLon  = (TextView)findViewById(R.id.longitude);
        //textLon.setEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.get_list_lokasi(GET_URL);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Tambah.this);
                        alertDialogBuilder.setTitle("Warning !");
                        alertDialogBuilder.setMessage("Anda yakin data sudah benar ?");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        idData = (EditText) findViewById(R.id.kode_barang);
                                        namaData = (EditText) findViewById(R.id.nama_barang);
                                        merkData = (EditText) findViewById(R.id.merek_barang);
                                        tahunData = (EditText) findViewById(R.id.tb_barang);
                                        hargaData = (EditText) findViewById(R.id.harga_satuan_barang);
                                        sumberData = (EditText) findViewById(R.id.sumber_dana);
                                        ruangData = (Spinner) findViewById(R.id.ruangan);
                                        latData = (EditText) findViewById(R.id.latitude);
                                        longData = (EditText) findViewById(R.id.longitude);

                                        /* Data Builder Array List */
                                        String valueId = idData.getText().toString();
                                        dataForm.add(valueId);
                                        String valueNama = namaData.getText().toString();
                                        dataForm.add(valueNama);
                                        String valueMerk = merkData.getText().toString();
                                        dataForm.add(valueMerk);
                                        String valueTahun = tahunData.getText().toString();
                                        dataForm.add(valueTahun);
                                        String valueHarga = hargaData.getText().toString();
                                        dataForm.add(valueHarga);
                                        String valueSumber = sumberData.getText().toString();
                                        dataForm.add(valueSumber);
                                        String valueRuang = ruangData.getSelectedItem().toString();
                                        dataForm.add(valueRuang);
                                        String valueLat = latData.getText().toString();
                                        dataForm.add(valueLat);
                                        String valueLong = longData.getText().toString();
                                        dataForm.add(valueLong);

                                        /* After Submit */
                                        new Thread(new Runnable() {
                                            public void run() {
                                                if (imagepath == null) imagepath = "false";
                                                uploadFileToServer(imagepath, JSON_URL, dataForm);
                                                Context context = Tambah.this;
                                                Intent intent;
                                                intent = new Intent(context, Beranda.class);
                                                (context).startActivity(intent);
                                            }
                                        }).start();
                                        finish();
                                    }
                                });
                        alertDialogBuilder.create().show();
                    }
                });
    }

    // Checking device has camera hardware or not
    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (resultCode == Activity.RESULT_OK) {

            exif = new Exif(imagepath, this);
            if( exif.getLat().length() > 4 ){
                Log.e( "Error Isi : ", String.valueOf(exif.getLat().length()));
                textLat.setText(exif.getLat());
                textLon.setText(exif.getLon());
            }else{
                GPSTracker gpsTracker = new GPSTracker(this);
                if (gpsTracker.getIsGPSTrackingEnabled()){
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(imagepath);
                    } catch (IOException e) {
                        Log.e("File Exif Not Found ", "Error");
                        e.printStackTrace();
                    }
                    int num1Lat = (int)Math.floor(gpsTracker.latitude);
                    int num2Lat = (int)Math.floor((gpsTracker.latitude - num1Lat) * 60);
                    double num3Lat = (gpsTracker.latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

                    int num1Lon = (int)Math.floor(gpsTracker.longitude);
                    int num2Lon = (int)Math.floor((gpsTracker.longitude - num1Lon) * 60);
                    double num3Lon = (gpsTracker.longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

                    if (exif != null) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
                    }
                    if (exif != null) {
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");
                    }


                    if (gpsTracker.latitude > 0) {
                        if (exif != null) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
                        }
                    } else {
                        if (exif != null) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
                        }
                    }

                    if (gpsTracker.longitude > 0) {
                        if (exif != null) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
                        }
                    } else {
                        if (exif != null) {
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
                        }
                    }

                    try {
                        if (exif != null) {
                            exif.saveAttributes();
                        }
                        Log.e("Nulis Exif", "Tapi Gagal");
                    } catch (IOException e) {
                        Log.e("Ga perlu Nulis Exif", e.toString());
                        e.printStackTrace();
                    }
                    String stringLatitude = String.valueOf(gpsTracker.latitude);
                    textLat.setText(stringLatitude);

                    String stringLongitude = String.valueOf(gpsTracker.longitude);
                    textLon.setText(stringLongitude);
                }
            }
        }
    }

    /* Galleries Method */

    private void selectImage() {
        final CharSequence[] items = { "Kamera", "Galeri", "Kembali" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Tambah.this);
        builder.setTitle("Tambah Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Kamera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Galeri")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Kembali")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        imagepath = selectedImagePath;

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 300;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        imageview.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imgFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SIGMAIPB");
        imgFolder.mkdirs();

        File image = new File(imgFolder, "SIGMA_" + timeStamp + ".jpg");

        FileOutputStream fo;
        try {
            image.createNewFile();
            fo = new FileOutputStream(image);
            if (thumbnail != null) {
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            }
            fo.write(bytes.toByteArray());
            fo.close();
            imagepath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageview.setImageBitmap(thumbnail);
    }

    public static String uploadFileToServer(String filename, String targetUrl, ArrayList<String> dataForm) {
        String response;
        FileInputStream fileInputStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024;

        try {
            if(!filename.equals("false"))
                fileInputStream = new FileInputStream(new File(filename));

            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(1024);
            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            ArrayList<String> keyForm = new ArrayList<>();
            keyForm.add("kode_barang");
            keyForm.add("nama_barang");
            keyForm.add("merk_type");
            keyForm.add("tahun");
            keyForm.add("harga_satuan");
            keyForm.add("sumber_dana");
            keyForm.add("kode_lokasi");
            keyForm.add("latitude");
            keyForm.add("longitude");
            for (int i = 0; i < keyForm.size(); i++) {
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + keyForm.get(i) + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                outputStream.writeBytes("Content-Length: " + dataForm.get(i).length() + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(dataForm.get(i) + lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            }

            if (fileInputStream != null) {
                String connstr;
                connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
                        + filename + "\"" + lineEnd;

                outputStream.writeBytes(connstr);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                System.out.println("Image length " + bytesAvailable + "");
                try {
                    while (bytesRead > 0) {
                        try {
                            outputStream.write(buffer, 0, bufferSize);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            response = "outofmemoryerror";
                            return response;
                        }
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                    return response;
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);
            }
            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();

            if (serverResponseCode == 200) response = "true";
            else response = "false";

            if (fileInputStream != null) {
                fileInputStream.close();
            }
            outputStream.flush();

            connection.getInputStream(); java.io.InputStream is = connection.getInputStream();

            int ch;
            StringBuilder b = new StringBuilder();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }


            outputStream.close();

        } catch (Exception ex) {
            // Exception handling
            response = "error";
            ex.printStackTrace();
            Log.e("Ini Lhoh Error ", ex.getMessage());
        }
        return response;
    }

    private void get_list_lokasi(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json);
                    }
                    json = sb.toString().trim();
                    JSONObject jObj = new JSONObject(json);
                    str_json = jObj.getJSONArray("lokasi");
                    for(int i = 0; i < str_json.length(); i++){
                        JSONObject ar = str_json.getJSONObject(i);
                        String id = ar.getString("kode");
                        list.add(id);
                    }
                    con.disconnect();
                }catch(Exception ex){
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Spinner sp = (Spinner) findViewById(R.id.ruangan);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Tambah.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(dataAdapter);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
