package com.example.polytechnic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.example.polytechnic.Faculty.UpdateFaculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView addNotice,addGellaryImage,addEbook,addFculty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          addNotice = (CardView)findViewById(R.id.addNotice);
        addGellaryImage = (CardView) findViewById(R.id.addGellaryImage);
        addEbook = (CardView)  findViewById(R.id.addEbook);
        addFculty = (CardView) findViewById(R.id.addFculty);

          addNotice.setOnClickListener(this);
          addGellaryImage.setOnClickListener(this);
          addEbook.setOnClickListener(this);
          addFculty.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

             if (v.getId()==R.id.addNotice)
             {
                 Intent intent = new Intent(this,UploadNotice.class);
                 startActivity(intent);
             }
             else if(v.getId() ==R.id.addGellaryImage)
             {
                 Intent intent = new Intent(this,Uploadimage.class);
                 startActivity(intent);
             }
             else if(v.getId()==R.id.addEbook)
             {
                 Intent intent = new Intent(this,UploadPdfActivity.class);
                 startActivity(intent);
             }
             else if(v.getId()==R.id.addFculty)
             {
                 Intent intent = new Intent(this, UpdateFaculty.class);
                 startActivity(intent);

             }
    }
}