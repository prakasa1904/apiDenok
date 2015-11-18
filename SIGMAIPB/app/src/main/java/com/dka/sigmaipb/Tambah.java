package com.dka.sigmaipb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import android.view.View;
import android.view.View.OnClickListener;


public class Tambah extends AppCompatActivity {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "SIGMA IPB";

    private Uri fileUri; // file url to store image/video

    Spinner sp;
    private Button btnAmbilGambar;
    private Button btnGallery;
    private ImageView imgPreview;
    private Button btnSimpan;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        sp = (Spinner) findViewById(R.id.ruangan);
        btnAmbilGambar = (Button) findViewById(R.id.btnAmbilGambar);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        addListenerOnButton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Untuk membuat list ruangan
        List<String> item = new ArrayList<String>();
        item.add("Ruang Baca");
        item.add("Ruang Lab 01");
        item.add("R. Sekretaris Departemen");
        item.add("R. Sidang I");
        item.add("R. Sidang II");
        item.add("R. Tamu");
        item.add("R. Tata Usaha");
        item.add("R. Teras lv.6");
        item.add("Ruang Dosen 1 (AAM)");
        item.add("Ruang Dosen 2 (ADN)");
        item.add("Ruang Dosen 3 (BS)");
        item.add("Ruang Dosen 4 (TM)");
        item.add("Ruang Lab 02");
        item.add("Ruang Dosen 5 (AHW)");
        item.add("Ruang Dosen 6 (AS)");
        item.add("Ruang Dosen 7 (KAN)");
        item.add("Ruang Dosen 8 (BSI)");
        item.add("Ruang Ketua Dept");
        item.add("Ruang Lab. Kecil Dept");
        item.add("Ruang Lab. Komputer");
        item.add("Ruang Perpustakaan Dept");
        item.add("Ruang Sekretariat Dept");
        item.add("Ruang Sekretaris Dept");
        item.add("Ruang Lab CI");
        item.add("Ruang Sidang Kecil");
        item.add("Ruang Sidang");
        item.add("PERPUSTAKAAN BIOLOGI");
        item.add("R. SEKRETARIAT DEPARTEMEN BIOLOGI");
        item.add("RUANG ARSIP ");
        item.add("RUANG ASISTEN & LABORAN (RUMAH PLASTIK)");
        item.add("RUANG ASISTEN & PENANGGUNG JAWAB LAB");
        item.add("RUANG ASISTEN & PENANGGUNG JAWAB LAB.");
        item.add("RUANG ASISTEN");
        item.add("RUANG CETAK & PERLENGKAPAN");
        item.add("Ruang Lab NCC");
        item.add("RUANG HERBARIUM & LAB. PENELITIAN TAKSONOMI TUMBUHAN");
        item.add("RUANG KULIAH BIOLOGI 2");
        item.add("RUANG LAB. PENDIDIKAN BIOLOGI 1");
        item.add("RUANG LAB. PENDIDIKAN BIOLOGI 2");
        item.add("RUANG LAB. PENDIDIKAN BIOLOGI 3");
        item.add("RUANG LAB. PENDIDIKAN BIOLOGI 4");
        item.add("RUANG LAB. PENDIDIKAN BIOLOGI 5");
        item.add("RUANG LAB. PENDIDIKAN KULTUR JARINGAN");
        item.add("RUANG LAB. PENELITIAN FISIOLOGI TUMBUHAN");
        item.add("RUANG LAB. PENELITIAN FUNGSI HEWAN");
        item.add("Ruang Lab SEINS");
        item.add("RUANG LAB. PENELITIAN KULTUR JARINGAN");
        item.add("RUANG LAB. PENELITIAN MIKOLOGI");
        item.add("RUANG LAB. PENELITIAN MIKROBIOLOGI");
        item.add("RUANG LAB. PENELITIAN MIKROBIOLOGI.");
        item.add("RUANG LABORATORIUM TERPADU BIOLOGI");
        item.add("RUANG MAHASISWA PASCA SARJANA BIOLOGI");
        item.add("RUANG MIKROTEKNIK & LAB. PENELITIAN ANATOMI TUMBUHAN");
        item.add("RUANG PERPUSTAKAAN BIOLOGI");
        item.add("RUANG SEKRETARIAT DEPARTEMEN BIOLOGI");
        item.add("RUANG SEKRETARIAT JURNAL HAYATI");
        item.add("Ruang Sekdep");
        item.add("RUANG SIDANG DEPARTEMEN BIOLOGI");
        item.add("RUANG STAF & LAB. PENELITIAN EKOLOGI TUMBUHAN");
        item.add("RUANG STAF BIOLOGI 1");
        item.add("RUANG STAF BIOLOGI 2");
        item.add("RUANG STAF BIOLOGI 3");
        item.add("RUANG STAF BIOLOGI 4");
        item.add("RUANG STAF BIOLOGI 4");
        item.add("RUANG STAF BIOLOGI 5");
        item.add("RUANG STAF BIOLOGI 5");
        item.add("Koridor Lt.2");
        item.add("Ruang Selasar");
        item.add("Koridor Lt.3");
        item.add("Ruang Dosen");
        item.add("Ruang Dosen");
        item.add("Ruang Kadep");
        item.add("Ruang Lab. Komp. B");
        item.add("Ruang Lab. Komp. A");
        item.add("Ruang Perpustakaan");
        item.add("Ruang Sekr.Dept.");
        item.add("Ruang Seminar A");
        item.add("Ruang Seminar B");
        item.add("Ruang Seminar 1");
        item.add("Ruang Seminar C");
        item.add("Ruang Seminar D");
        item.add("Ruang Server");
        item.add("Ruang Sidang");
        item.add("Ruang TU");
        item.add("R. BAGIAN KEUANGAN");
        item.add("R. BENGKEL FISIKA");
        item.add("R. BENGKEL KAYU");
        item.add("R. DOSEN - A");
        item.add("R. DOSEN - B");
        item.add("Ruang Server");
        item.add("R. KA LAB DAN LAB MIKROKONTROLER");
        item.add("R. KA LAB TPB FISIKA");
        item.add("R. KETUA DEPARTEMEN");
        item.add("R. KETUA LAB ELEKTRONIKA");
        item.add("R. KOMDIK");
        item.add("R. KORIDOR");
        item.add("R. KULIAH - A");
        item.add("R. KULIAH - B");
        item.add("R. LAB BIOFISIKA");
        item.add("R. LAB ELEKTRONIKA");
        item.add("Ruang Sidang");
        item.add("R. LAB FISIKA DASAR");
        item.add("R. LAB FISIKA LANJUT");
        item.add("R. LAB FISIKA TEORI");
        item.add("R. LAB JARINGAN DAN SHOFTWARE KOMPUTER");
        item.add("R. LAB KARAKTERISASI");
        item.add("R. Lab Mikroprosessor dan Lab Material");
        item.add("R. RAPAT");
        item.add("R. SEKRETARIS DEPARTEMEN");
        item.add("R. SIDANG");
        item.add("R. TATA USAHA");
        item.add("Ruang Bendahara");
        item.add("Ruang Tamu");
        item.add("R.  ANY HARDIANY");
        item.add("R. Baca(C-17 A)");
        item.add("R. Dimas Andrianto");
        item.add("R. Dr. Hj Anna P Roswiem");
        item.add("R. Dr. I Made Artika");
        item.add("R. Dr. Laksmi Ambarsari");
        item.add("R. Drs. Edy Djauhari PK.");
        item.add("R. Iis Kadarsasih (R.Teknisi)(C-19) ");
        item.add("R. Ir. H A.E Zaenal Hasan");
        item.add("R. KETUA DEPARTEMEN");
        item.add("Ruang Teknisi");
        item.add("R. KOMISI PENDIDIKAN");


        // Untuk membuat adapter list ruangan
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tambah.this,android.R.layout.simple_spinner_dropdown_item,item);

        // Untuk menentukan model adapternya
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Untuk menerapkan adapter pada spinner
        sp.setAdapter(adapter);

        // Capture image button click event
        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    public void addListenerOnButton() {

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // prepare for a progress bar dialog
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("Proses Unggah ...");
                        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                        progressBar.show();

                        //reset progress bar status
                        progressBarStatus = 0;

                        //reset filesize
                        fileSize = 0;

                        new Thread(new Runnable() {
                            public void run() {
                                while (progressBarStatus < 100) {
                                    // process some tasks
                                    progressBarStatus = doSomeTasks();
                                    // your computer is too fast, sleep 1 second
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // Update the progress bar
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressBarStatus);
                                        }
                                    });
                                }

                                // ok, file is downloaded,
                                if (progressBarStatus >= 100) {

                                    // sleep 2 seconds, so that you can see the 100%
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // close the progress bar dialog
                                    progressBar.dismiss();
                                }
                            }
                        }).start();

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

}
