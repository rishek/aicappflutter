package com.example.rishek.risheksapp;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        LinearLayout l1 = findViewById(R.id.l1);
        l1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity2.class);
                startActivity(intent);
            };

    });
        LinearLayout l2 = findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity3.class);
                startActivity(intent);
            };

        });
        LinearLayout l3 = findViewById(R.id.l3);
        l3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity4.class);
                startActivity(intent);
            };

        });



    }}