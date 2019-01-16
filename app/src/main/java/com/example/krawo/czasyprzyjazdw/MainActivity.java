package com.example.krawo.czasyprzyjazdw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextInnput;
    TextView delayTextView;
    Button button;
    RequestQueue requestQueue;
    Spinner spinner;





    String baseUrl = "http://87.98.237.99:88/delays?stopId=";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.button = (Button) findViewById(R.id.button);
        this.delayTextView = (TextView) findViewById(R.id.delayTextView);
        this.delayTextView.setMovementMethod(new ScrollingMovementMethod());
        this.spinner = (Spinner) findViewById(R.id.spinner);






        requestQueue = Volley.newRequestQueue(this);


    }

    private void clearList() {
        this.delayTextView.setText("");
    }

    private void addtoList(String liniaraw, String tczas, String eczas, String kierunek ) {

        //tu będzie trzeba pobrać linie z drugiego GETa, wrzucić do HashMap, i id linii zamieniać na numer linii ale teraz jebać


        String liniapop;



        if(liniaraw.equals("10605")){
            liniapop = "R";
        }
        else if(liniaraw.length()==1){
            liniapop = liniaraw;
        }
        else if(liniaraw.equals("10602")){
            liniapop = "J";
        }
        else if(liniaraw.equals("10410")){
            liniapop = "N10";
        }
        else if(liniaraw.equals("10430")){
            liniapop = "N30";
        }
        else if(liniaraw.equals("10606")){
            liniapop = "S";
        }
        else if(liniaraw.equals("10607")){
            liniapop = "W";
        }
        else if(liniaraw.equals("10609")){
            liniapop = "Z";
        }
        else if(liniaraw.substring(2,3).equals("0")){ //10031 -> 31
            liniapop = liniaraw.substring(3,5);
        }
        else if(liniaraw.substring(1,2).equals("0")){
            liniapop = liniaraw.substring(2,5);
        }
        else{
            liniapop = liniaraw;
        }

        String strRow = "Linia: " + liniapop + " Kierunek: "+  kierunek + " Czas z rozkładu: " + tczas + " Przewidywany czas: " + eczas;
        String currentText = delayTextView.getText().toString();
        this.delayTextView.setText(currentText + "\n\n" + strRow);

    }

    private void setList(String str) {
        this.delayTextView.setText(str);

    }

    private void getList(String przystanek) {
        this.url = this.baseUrl + przystanek;


        JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {

                                JSONArray jsonArray = response.getJSONArray("delay");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject delay = jsonArray.getJSONObject(i);

                                    String liniaraw = delay.get("routeId").toString();
                                    String eczas = delay.get("estimatedTime").toString();
                                    String tczas = delay.get("theoreticalTime").toString();
                                    String kierunek = delay.get("headsign").toString();

                                    addtoList(liniaraw, tczas, eczas, kierunek);

                                }





                            } catch (JSONException e) {
                                // If there is an error then output this to the logs.
                                Log.e("Volley", "Niepoprawne id przystanku");
                            }

                        } else {
                            // The user didn't have any repos.
                            setList("Brak listy dla danego przystanku");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        setList("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }

                }
        );


        requestQueue.add(arrReq);
    }

    public void getListClicked(View v){


        int spinner_pos = spinner.getSelectedItemPosition();
        String[] size_values = getResources().getStringArray(R.array.size_values);
        int size = Integer.valueOf(size_values[spinner_pos]);

        clearList();
        getList(String.valueOf(size));




    }

}