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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addImage;

    private final int REQ = 1;
    private Bitmap bitmap;
    private DatabaseReference reference;
    private StorageReference storageReference;

    String DwonloadUrl = "";

    private ImageView NoticeImageView;
    private EditText noticeTitle;
    private Button UploadNoticeBtn;

    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        addImage = (CardView) findViewById(R.id.addImage);
        NoticeImageView = (ImageView) findViewById(R.id.NoticeImageView);
        noticeTitle = (EditText) findViewById(R.id.noticeTitle);
        UploadNoticeBtn = (Button) findViewById(R.id.UploadNoticeBtn);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


          pd = new ProgressDialog(this);

        UploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      if (noticeTitle.getText().toString().isEmpty())
                      {
                          noticeTitle.setError("Empty");
                          noticeTitle.requestFocus();
                      }
                      else if(bitmap == null)
                      {
                          UploadData();
                      }
                      else
                      {
                          UploadImage();
                      }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                         opnenGallery();
               // Toast.makeText(getApplicationContext(),"working3",Toast.LENGTH_LONG).show();
            }


        });


    }

    private void UploadData()
    {
               reference = reference.child("Notice");
               final String uniquekey = reference.push().getKey();

              String  title = noticeTitle.getText().toString();

        Calendar callForDateTime = Calendar.getInstance();
        //date
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
        String date = currentDate.format(callForDateTime.getTime());


         //time
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss");
        String time = currentTime.format(callForDateTime.getTime());

         NoticeData noticeData = new NoticeData(title,DwonloadUrl,date,time,uniquekey);
          if (reference.child(uniquekey).setValue(noticeData).isSuccessful())
          {
               pd.dismiss();
              Toast.makeText(getApplicationContext(),"Uploading",Toast.LENGTH_LONG).show();
          }



    }

    private void UploadImage()
    {
        pd.setMessage("Uploading......");
        pd.show();
       // pd.dismiss();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[]  finalimg =baos.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);

              uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                              DwonloadUrl = String.valueOf(uri);

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
                           Toast.makeText(UploadNotice.this,"Some Thing Wrong",Toast.LENGTH_LONG).show();
                       }

                  }
              });
    }

    private void opnenGallery() {

        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                     startActivityForResult(pickImage,REQ);
       // Toast.makeText(getApplicationContext(),"working2",Toast.LENGTH_LONG).show();
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
                 NoticeImageView.setImageBitmap(bitmap);
               //  Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();

             }
             catch (IOException e)
             {
                 Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();
             }

        }


    }
}