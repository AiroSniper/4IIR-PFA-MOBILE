package com.example.pfatesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    PaintView paintView;
    ImageView image;
    public static EditText annotation;
    private Button takepic;
    private String currentImagePath = null;
    private static final int IMAGE_REQUEST = 1;
    private Bitmap rotatedBitmap;
    private Uri imageUri;
    private String url = "http://192.168.1.107:3000/files";
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private JSONObject jsonObject2;
    private File imageFile = null;
    private String encodedImage;
    private static Boolean IS_SAVED;
    private GpsTracker gpsTracker;
    private double longitude;
    private  double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paint_view);
        image = findViewById(R.id.image);
        paintView.setOnTouchListener(paintView);
        Button init, undo, send, takepic, next, prev, annotate, clearall, clearthis;
        annotate = findViewById(R.id.annotate);
        clearall = findViewById(R.id.clearall);
        clearthis = findViewById(R.id.clearthis);
        annotation = findViewById(R.id.annotation);
        init = findViewById(R.id.init);
        undo = findViewById(R.id.undo);
        send = findViewById(R.id.send);
        takepic = findViewById(R.id.takepic);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        requestQueue = Volley.newRequestQueue(this);
        jsonObject = new JSONObject();
        jsonObject2 = new JSONObject();
      //localisation
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.polygones.clear();
                annotation.setText("");
                paintView.index = -1;
                image.setBackgroundColor(Color.WHITE);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paintView.index > 0) {
                    paintView.index--;
                    Log.d("Prev", paintView.index + "");
                    annotation.setText(paintView.polygones.get(paintView.index).getAnnotation());
                }


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paintView.index < paintView.polygones.size() - 1) {
                    paintView.index++;
                    Log.d("Next", paintView.index + "");
                    annotation.setText(paintView.polygones.get(paintView.index).getAnnotation());
                }


            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paintView.pointes.size() >= 1) {
                    paintView.points_counter--;
                    if (paintView.lines.size() >= 1) {
                        paintView.lines.remove(paintView.lines.size() - 1);
                    }
                    paintView.pointes.remove(paintView.pointes.size() - 1);
                }
            }
        });
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.points_counter = -1;
                paintView.lines.clear();
                paintView.pointes.clear();
            }
        });
        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (null != cameraIntent.resolveActivity(getPackageManager())) {
                   /* try {
                        //imageFile = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    if (imageFile != null) {
                        imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.android.fileprovider", imageFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, IMAGE_REQUEST);
                    }

                }

            }

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath).copy(Bitmap.Config.ARGB_8888, true);
           // rotatedBitmap = getImageOrientation(bitmap);
           // getLocation();
            image.setImageBitmap(rotatedBitmap);

            IS_SAVED =false;
        } else {
            Log.d("erreuur", "smtg wrong");
        }

    }

    //image upload






    public void uploadImage()   {
        if(imageFile==null) {
            Log.d("ImageFile",null);

        }
        Ion.getDefault(MainActivity.this).getConscryptMiddleware().enable(false);
        Ion.with(MainActivity.this)
                .load(url+"/upload")
                .setMultipartFile("image", "image/jpeg", imageFile)
                .asJsonObject()
                .withResponse()
                .setCallback((e, result) -> {

                    if(e != null) {
                        Toast.makeText(this, "Error is: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        switch (result.getHeaders().code()) {
                            case 500:
                                Toast.makeText(this, "Image Uploading Failed. Unknown Server Error!", Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                                Toast.makeText(this, "Image Successfully Uploaded!", Toast.LENGTH_SHORT).show();

                                break;
                        }
                    }

                });


    }
}
