package com.example.polytechnic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdfActivity extends AppCompatActivity {

    private CardView addpdf;
    private EditText pdfTitle;
    private Button UploadPdfBtn;
    private TextView selcetedpdf;

    private final int REQ = 1;

    private Uri pdfData;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String DwonloadUrl = "";

   String pdfName,title;


    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);


         addpdf = (CardView)findViewById(R.id.addpdf);
         pdfTitle = (EditText)findViewById(R.id.pdfTitle);
         UploadPdfBtn = (Button)findViewById(R.id.UploadPdfBtn);
        selcetedpdf = (TextView) findViewById(R.id.selcetedpdf);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

          pd = new ProgressDialog(this);


           UploadPdfBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   title = pdfTitle.getText().toString();
                   if (title.isEmpty())
                   {
                       pdfTitle.setError("Empty");
                       pdfTitle.requestFocus();
                   }
                   else if(pdfData == null)
                   {
                       Toast.makeText(UploadPdfActivity.this,"Plesce Upload pdf",Toast.LENGTH_LONG).show();
                   }
                   else

                   {
                       uploadPdf();
                   }

               }

               private void uploadPdf() {


                     pd.setTitle("Plesce wait....");
                     pd.setMessage("Uploading pdf");
                     pd.show();

                      StorageReference reference = storageReference.child("pdf/"+ pdfName+"-"+System.currentTimeMillis()+".pdf");

                      reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                              Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                              Toast.makeText(UploadPdfActivity.this,"Work1",Toast.LENGTH_LONG).show();

                              while (!uriTask.isComplete())
                              {
                                  Uri uri = uriTask.getResult();

                                  uploadData(String.valueOf(uri));
                              }

                          }


                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {


                               Toast.makeText(UploadPdfActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                               pd.dismiss();

                          }
                      });



               }
           });



        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opnenGallery();
                // Toast.makeText(getApplicationContext(),"working3",Toast.LENGTH_LONG).show();
            }


        });

    }

    private void uploadData(String dwonloadUrl) {

         String uniqueKey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();

         data.put("pdftitle",title);
         data.put("pdfurl",dwonloadUrl); // work now line


         databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {




                 Toast.makeText(UploadPdfActivity.this,"Upload Sucsessfull",Toast.LENGTH_LONG).show();
                 pd.dismiss();

                   pdfTitle.setText("");
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

                 pd.dismiss();

                 Toast.makeText(UploadPdfActivity.this,"Failed Upload",Toast.LENGTH_LONG).show();

             }
         });

    }


    private void opnenGallery() {

             


       Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf file"),1);


       /* int requestCode = 100;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);-*/

        // Toast.makeText(getApplicationContext(),"working2",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getApplicationContext(),"working5",Toast.LENGTH_LONG).show();

        if(requestCode== REQ && resultCode==RESULT_OK )
        {
           pdfData = data.getData();

           Toast.makeText(this,""+"selcted pdf"+pdfData,Toast.LENGTH_LONG).show();

            if (pdfData.toString().startsWith("content://"))
            {


                try {
                    Cursor cursor = null;
                    cursor = UploadPdfActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst())
                    {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (pdfData.toString().startsWith("file://"))
            {
                pdfName = new File(pdfData.toString()).getName();
            }


            selcetedpdf.setText(pdfName);

        }


    }
}