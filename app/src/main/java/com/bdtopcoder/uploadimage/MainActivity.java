package com.bdtopcoder.uploadimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*This Application Created By Atikul Software
Name - MD Atikul Islam
Website - https://www.bdtopcoder.xyz/
YouTube - https://www.youtube.com/@AwesomeDesigner*/

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button upload_btn;
    EditText name;
    ProgressBar progressBar;

    int MY_REQUEST_CODE = 1;
    Bitmap bitmap;
    String encodeImage;
    String names;

    String BASE_API_URL = "https://atikulislam.xyz/upload_image/upload_img.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        upload_btn = findViewById(R.id.upload_btn);
        name = findViewById(R.id.name);
        progressBar = findViewById(R.id.progressBar);

        // select image
        imageView.setOnClickListener(v -> {
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Select Image"),MY_REQUEST_CODE);

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        });

        upload_btn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            names = name.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_API_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }){

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> myParams = new HashMap<>();
                    myParams.put("images",encodeImage);
                    myParams.put("name",names);
                    return myParams;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);

        });



    } // OnCreate Method End Here =============

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                ImageStore(bitmap);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ImageStore (Bitmap bitmap){
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageByte = stream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

} // Public Class End Here ====================