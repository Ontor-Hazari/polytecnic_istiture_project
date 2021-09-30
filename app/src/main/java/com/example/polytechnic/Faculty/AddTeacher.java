package com.example.polytechnic.Faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.polytechnic.R;

import java.io.IOException;

public class AddTeacher extends AppCompatActivity {

    private int REQ = 1;
    Bitmap bitmap = null;

    ImageView addTeacherImage;
    EditText AddTeacherName,AddTeacherEmail,AddTeacherPost;

    Button Addteacher;
    Spinner Teacher_category;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        addTeacherImage = (ImageView) findViewById(R.id.addTeacherImage);


        AddTeacherName = (EditText)findViewById(R.id.AddTeacherName);
        AddTeacherEmail = (EditText)findViewById(R.id.AddTeacherEmail);
        AddTeacherPost =(EditText)findViewById(R.id.AddTeacherPost);
        Teacher_category = (Spinner) findViewById(R.id.Teacher_category);



        Addteacher = (Button)findViewById(R.id.Addteacher);

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGelarey();
            }
        });



               String[] iteams = {"Computer Science","Cevil","Mechanical","Chemistry","Physics"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,iteams);

          Teacher_category.setAdapter(adapter);

          Teacher_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                  category =  Teacher_category.getSelectedItem().toString();

              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {



              }
          });

    }

    private void openGelarey()
    {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(pickImage,REQ);
    }

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
                addTeacherImage.setImageBitmap(bitmap);
                //  Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();

            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_LONG).show();
            }

        }


    }
}