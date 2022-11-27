package com.example.rishek.risheksapp;

import java.util.ArrayList;

public class Countries {
    ArrayList<CountryDetails> Response;

    public class CountryDetails {
        String Name, CurrencyCode;

        public String getName() {
            return Name;
        }

        public String getCurrencyCode() {
            return CurrencyCode;
        }
    }


    public ArrayList<CountryDetails> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<CountryDetails> response) {
        Response = response;
    }
}
