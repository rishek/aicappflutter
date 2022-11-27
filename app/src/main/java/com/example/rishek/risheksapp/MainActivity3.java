package com.example.rishek.risheksapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] languages={"ENGLISH","HINDI","GERMAN"};
    TextView text1,text2,text3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Spinner spin3 = findViewById(R.id.spinner3);
        text1=findViewById(R.id.la1);
        text2=findViewById(R.id.la2);
        text3=findViewById(R.id.la3);
        spin3.setOnItemSelectedListener(this);
        CustomAdapter2 customAdapter2= new CustomAdapter2(getApplicationContext(),languages);
        spin3.setAdapter(customAdapter2);

    }

    @Override
    public void onItemSelected(AdapterView<?>arg0, View arg1, int position, long id) {
        if(position==0) {
            text1.setText("HELLO");
            text2.setText("THANKS");
            text3.setText("YES");
        }
        if(position==1) {
            text1.setText("NAMASTE");
            text2.setText("DHANYAVAAD");
            text3.setText("HAA");
        }
        if (position==2){
            text1.setText("HALLO");
            text2.setText("VALENTINE");
            text3.setText("NIA");
        }
        }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
