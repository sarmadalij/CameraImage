package com.sarmadali.cameraimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.sarmadali.cameraimage.databinding.ActivityMainBinding;
import android.Manifest;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityMainBinding mainBinding;
    private Uri currentPhotoUri;
    private String currentPhotoPath;
    private int requestCodeImage = 123;
    private Bitmap photoBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        //check for camera permission, if not granted request permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
        !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                   Manifest.permission.CAMERA
            }, REQUEST_IMAGE_CAPTURE);
        } else {
            // Permission already granted, execute the code
//            dispatchTakePictureIntent();
        }

        //open camera button
        mainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    //to take picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(this,
                        "com.sarmadali.cameraimage.fileprovider",
                        photoFile);
                // Grant temporary permissions to other apps
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                startActivityForResult(takePictureIntent, requestCodeImage);
            }
        }
    }

    //create image file
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        //to see the path
        Log.d("Image path", "createImageFile: " + currentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //decoding the stored image and setting image on imageView
        if (requestCode == requestCodeImage && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            mainBinding.imageView.setImageBitmap(bitmap);

            // Generate and display thumbnail
            generateAndDisplayThumbnail();
        }
    }

    //for thumbnail
    private void generateAndDisplayThumbnail() {
        // Decode and display the thumbnail
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 20; // Adjust the thumbnail size as needed
//        Adjusting the inSampleSize allows you to control the trade-off between image
//        quality and memory usage. A higher inSampleSize reduces memory usage but may
//        result in a lower-quality thumbnail, while a lower inSampleSize produces a
//        higher-quality thumbnail but consumes more memory.
        photoBitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
        mainBinding.thumbnail.setImageBitmap(photoBitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the operation
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                mainBinding.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }



}