package com.dka.sigmaipb;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;


/**
 * Created by Deni Kurnia Aldian on 12/13/2015.
 */
public class Exif {
    private File exifFile;    //It's the file passed from constructor
    private String filepath;  //file in Real Path format
    private Activity parentActivity; //Parent Activity

    private String filepath_withoutext;
    private String ext;

    private ExifInterface exifInterface;
    private Boolean exifValid = false;;

    //Exif TAG
//for API Level 8, Android 2.2
    String LATITUDE = "";
    String LATITUDE_REF = "";
    String LONGITUDE = "";
    String LONGITUDE_REF = "";
    Float Latitude, Longitude;

    //Constructor from path
    Exif(String fileString, Activity parent){
        exifFile = new File(fileString);
        parentActivity = parent;
        filepath = fileString;
        PrepareExif();
    }

    //Constructor from URI
    Exif(Uri fileUri, Activity parent){
        exifFile = new File(fileUri.toString());
        parentActivity = parent;
        filepath = getRealPathFromURI(fileUri);
        PrepareExif();
    }

    private void PrepareExif(){
        int dotposition= filepath.lastIndexOf(".");
        filepath_withoutext = filepath.substring(0,dotposition);
        ext = filepath.substring(dotposition + 1, filepath.length());

        if (ext.equalsIgnoreCase("jpg")){
            try {
                exifInterface = new ExifInterface(filepath);
                ReadExifTag();
                exifValid = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void ReadExifTag() throws IOException {
        LATITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        LATITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        LONGITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        LONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        if((LATITUDE !=null) && (LATITUDE_REF !=null) && (LONGITUDE != null) && (LONGITUDE_REF !=null)) {
            if(LATITUDE_REF.equals("N")){
                Latitude = convertToDegree(LATITUDE);
            }
            else{
                Latitude = 0 - convertToDegree(LATITUDE);
            }
            if(LONGITUDE_REF.equals("E")){
                Longitude = convertToDegree(LONGITUDE);
            }
            else{
                Longitude = 0 - convertToDegree(LONGITUDE);
            }
        }
    }

    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;
    }

    @SuppressWarnings("deprecation")
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = parentActivity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getLat(){
       return ("" + Latitude);
    }

    public String getLon(){
       return ("" + Longitude);
    }
}
