package com.example.rishek.risheksapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String[] countryNames;
    double convrtdvalue;
    Spinner spin, spin2;
    EditText value;
    TextView result;
    String ccode1, ccode2;
    ArrayList<Countries.CountryDetails> countryDetailsList;
    Double valueindouble;
    String allData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        value = findViewById(R.id.input);
        result = findViewById(R.id.output);
        spin = findViewById(R.id.spinner);
        spin2 = findViewById(R.id.spinner2);
        spin.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);

        getCountries();
    }

    private void setSpinnerAdapter() {
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), countryNames);
        spin.setAdapter(customAdapter);
        spin2.setAdapter(customAdapter);
    }

    private void getCountries() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        Call<Countries> call = api.getCountries();
        call.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                final Countries CountriesObject = response.body();
                //Creating an String array for the ListView
                countryDetailsList = CountriesObject.getResponse();
                countryNames = new String[countryDetailsList.size()];
                //looping through all the countries and inserting the names inside the string array
                for (int i = 0; i < countryDetailsList.size(); i++) {
                    countryNames[i] = countryDetailsList.get(i).getName();
                }
                Log.d("mylog", "hiiii");
                setSpinnerAdapter();
            }

            @Override
            public void onFailure(Call<Countries> call, Throwable t) {
                Log.d("mylog", "jpt" + t.getMessage());
            }
        });
    }

    private void getRates(String countryCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL2)
                .addConverterFactory(ScalarsConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.getRates(countryCode.toUpperCase());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                allData = response.body();
                if (allData == null) {
                    result.setText("");
                    Toast.makeText(MainActivity2.this, "Origin currency does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject jsondata = new JSONObject(allData);
                    if(!jsondata.getJSONObject("rates").has(ccode2)){
                        Toast.makeText(MainActivity2.this, "PLEASE ENTER DIFFERENT DESTINATION COUNTRY", Toast.LENGTH_LONG).show();
                        result.setText("");
                        return;
                    }
                    double convRate = jsondata.getJSONObject("rates").getDouble(ccode2);
                    convertCurrency(convRate);
                } catch (Exception e) {
                    result.setText("");
                    Toast.makeText(MainActivity2.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("mylog", "jpt" + t.getMessage());
            }
        });
    }

    private void convertCurrency(double convRate) {
        convrtdvalue = valueindouble * convRate;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String finalvalueinstring = df.format(convrtdvalue);
        result.setText(finalvalueinstring);
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (arg0.getId() == R.id.spinner) {
            ccode1 = countryDetailsList.get(position).getCurrencyCode();
        }
        if (arg0.getId() == R.id.spinner2) {
            ccode2 = countryDetailsList.get(position).getCurrencyCode();
        }

    }

    public void onClick(View view) {
        String valueinstring = value.getText().toString();
        if (valueinstring.equals("")) {
            Toast.makeText(MainActivity2.this, "PLEASE ENTER CURRENCY TO CONVERT", Toast.LENGTH_LONG).show();

        } else {
            valueindouble = Double.parseDouble(valueinstring);
            if (ccode1.equals(ccode2)) {
                result.setText(null);
                Toast.makeText(MainActivity2.this, "PLEASE CHOOSE DIFFERENT COUNTRY TO CONVERT CURRENCY", Toast.LENGTH_LONG).show();
            } else {
                getRates(ccode1);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}



