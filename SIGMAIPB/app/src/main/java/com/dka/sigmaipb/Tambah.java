package com.dka.sigmaipb;

import android.app.Activity;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dka.sigmaipb.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dka.sigmaipb.R.layout.content_pencarian;
import static com.dka.sigmaipb.R.layout.content_tambah;

public class Tambah extends AppCompatActivity{

    // Activity request codes
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public String imagepath = null;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "SIGMA IPB";

    private Uri fileUri; // file url to store image/video
    private static final String GET_URL = "http://172.20.10.4/SigmaIpb/api/get_lokasi";
    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/add";
    public ArrayList<String> dataForm = new ArrayList<String>();

    Spinner sp;
    private TextView messageText, textLat, textLon;
    public ImageView imageview;
    private Button btnAmbilGambar;
    private Button saveData;

    // Post data
    public EditText idData, namaData, merkData, tahunData, hargaData, sumberData, latData, longData;
    public Spinner ruangData;

    private long fileSize = 0;

    EditText textlat, textlon;
    Exif exif;

    /* Data Spinner */
    JSONArray str_json = null;
    public List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imgPreview);

        sp = (Spinner) findViewById(R.id.ruangan);
        btnAmbilGambar = (Button) findViewById(R.id.btnAmbilGambar);
        saveData = (Button) findViewById(R.id.saveData);

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
                                        dialog = ProgressDialog.show(Tambah.this, "", "Uploading file...", true);
                                        //messageText.setText("uploading started.....");
                                        new Thread(new Runnable() {
                                            public void run() {
                                                if(imagepath != null)
                                                    uploadFileToServer(imagepath, JSON_URL, dataForm);
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
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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
            Uri LatLong = data.getData();
            //Uri Long = data.getData();

            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

            /*exif = new Exif(LatLong, this);
            textLat.setText(exif.getLat());
            textLon.setText(exif.getLon());*/

            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()){
                String stringLatitude = String.valueOf(gpsTracker.latitude);
                textLat.setText(stringLatitude);

                String stringLongitude = String.valueOf(gpsTracker.longitude);
                textLon.setText(stringLongitude);
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
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        messageText.setText("Sukses");
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageview.setImageBitmap(thumbnail);
    }

    public static String uploadFileToServer(String filename, String targetUrl, ArrayList<String> dataForm) {
        String response = "error";
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        String pathToOurFile = filename;
        String urlServer = targetUrl;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

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

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            ArrayList<String> keyForm = new ArrayList<String>();
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
                ;
            }

            /*String taskId = "anyvalue";
            outputStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            outputStream.writeBytes("Content-Length: " + taskId.length() + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(taskId + lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);*/

            String connstr = null;
            connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
                    + pathToOurFile + "\"" + lineEnd;

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

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            System.out.println("Server Response Code " + " " + serverResponseCode);
            System.out.println("Server Response Message "+ serverResponseMessage);

            if (serverResponseCode == 200) {
                response = "true";
            }else
            {
                response = "false";
            }

            fileInputStream.close();
            outputStream.flush();

            connection.getInputStream();
            //for android InputStream is = connection.getInputStream();
            java.io.InputStream is = connection.getInputStream();

            int ch;
            StringBuffer b = new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }

            String responseString = b.toString();
            System.out.println("response string is" + responseString); //Here is the actual output

            outputStream.close();
            outputStream = null;

        } catch (Exception ex) {
            // Exception handling
            response = "error";
            System.out.println("Send file Exception" + ex.getMessage() + "");
            ex.printStackTrace();
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

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json = "";
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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Tambah.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(dataAdapter);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
