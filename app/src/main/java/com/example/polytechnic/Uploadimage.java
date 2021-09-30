package com.example.polytechnic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Uploadimage extends AppCompatActivity {


    private Spinner imageCategory;
    private CardView selectImage;
    private Button uploadImage;
    private ImageView galleryImageView;

    private String category;
    private int REQ = 1;
    private Bitmap bitmap;

        ProgressDialog pd;

        private DatabaseReference reference;
        private StorageReference storageReference;
        private String dwonloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);


                imageCategory = (Spinner) findViewById(R.id.image_category);
                selectImage = (CardView) findViewById(R.id.addGellaryImage);
                uploadImage = (Button) findViewById(R.id.UploadImageBtn);
                galleryImageView = (ImageView) findViewById(R.id.GellaryImageView);

                pd = new ProgressDialog(this);

                reference = FirebaseDatabase.getInstance().getReference().child("gallery");
                storageReference = FirebaseStorage.getInstance().getReference().child("gallery");




                 String[] items = new String[]{"Select Category","Convoation","Independenc Day","Other event"};

                 imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items));


                 imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         category = imageCategory.getSelectedItem().toString();
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {

                     }
                 });

                 selectImage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                             openGelarey();
                     }
                 });

                 uploadImage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                                if (bitmap == null)
                                {
                                     Toast.makeText(getApplicationContext(),"Plesce Upload Image",Toast.LENGTH_SHORT).show();
                                }
                                else if(category.equals("Select Category"))
                                {
                                    Toast.makeText(getApplicationContext(),"Plesce select Image category",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    pd.setMessage("Uploading....");

                                    pd.show();

                                    uploadImage();
                                }
                     }
                 });
    }

    private void uploadImage() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[]  finalimg =baos.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);

        uploadTask.addOnCompleteListener(Uploadimage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    dwonloadurl = String.valueOf(uri);

                                    UploadData();
                                    pd.dismiss();

                                }
                            });
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Some Thing Wrong",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void UploadData() {

         reference = reference.child(category);
         final String uniqkey =  reference.push().getKey();

         reference.child(uniqkey).setValue(dwonloadurl).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 pd.dismiss();

                 Toast.makeText(getApplicationContext(),"Image Upload Sucsessfully",Toast.LENGTH_SHORT).show();

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

                 pd.dismiss();

                 Toast.makeText(getApplicationContext(),"Image Upload wrong",Toast.LENGTH_SHORT).show();

             }
         });
    }

    private void openGelarey()
    {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getApplicationContext(),"working5",Toast.LENGTH_LONG).show();

        if(requestCode== REQ &&  resultCode == RESULT_OK)
        {
            Toast.makeText(getApplicationContext(),"working4",Toast.LENGTH_LONG).show();

            Uri uri = data.getData();

            try {
                // Toast.makeText(getApplicationContext(),"working1",Toast.LENGTH_LONG).show();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                galleryImageView.setImageBitmap(bitmap);
                //  Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();

            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();
            }

        }


    }
}