package com.superducks.apptemp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView txtDoAm;
    Button btnCapNhat;
    ImageButton imgbutMayBom, imgbutDen;
    JSONArray response_toancuc;
    String doAm;
    int statusMayBom = 0, statusDen = 0;
    int id = 1;
    String device_name, device_status;
    String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ánh xạ
        AnhXa();
        //Code
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadJSON("http://appdodoam.herokuapp.com/NhanTuServer.php");

            }
        });

        imgbutMayBom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusMayBom = 1 - statusMayBom;
                if(statusMayBom == 1){
                    imgbutMayBom.setImageResource(R.drawable.on_switch);
                    device_name = "device1";
                    device_status = "on";
                    action = "onMayBom";
                    UpdateStatusDevices("http://appdodoam.herokuapp.com/UpdateDevicesStatus.php?device_name=device1&device_status=on");
                }
                else{
                    imgbutMayBom.setImageResource(R.drawable.off_switch);
                    device_name = "device1";
                    device_status = "off";
                    action = "offMayBom";
                    UpdateStatusDevices("http://appdodoam.herokuapp.com/UpdateDevicesStatus.php?device_name=device1&device_status=off");
                }
            }
        });

        imgbutDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusDen = 1 - statusDen;
                if(statusDen == 1){
                    imgbutDen.setImageResource(R.drawable.on_switch);
                    device_name = "device2";
                    device_status = "on";
                    action = "onDenLed";
                    UpdateStatusDevices("http://appdodoam.herokuapp.com/UpdateDevicesStatus.php?device_name=device2&device_status=on");

                }
                else{
                    imgbutDen.setImageResource(R.drawable.off_switch);
                    device_name = "device2";
                    device_status = "off";
                    action = "offDenLed";
                    UpdateStatusDevices("http://appdodoam.herokuapp.com/UpdateDevicesStatus.php?device_name=device2&device_status=off");
                }
            }
        });
    }

    public void controlMayBom() {

    }


    private void AnhXa(){
        //Ánh xạ
        imgbutMayBom = (ImageButton) findViewById(R.id.btnMayBom);
        imgbutDen = (ImageButton) findViewById(R.id.btnDenLed);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
        txtDoAm = (TextView) findViewById(R.id.txtDoAm);

    }

    private void ReadJSON(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                response_toancuc = response;
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = response.getJSONObject(0);
                    doAm = object.getString("doam");
                    txtDoAm.setText(doAm +"%");
                    Toast.makeText(MainActivity.this, "Độ ẩm hiện tại là " + doAm + "%", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void UpdateStatusDevices(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    switch (action) {
                        case "onMayBom":
                            Toast.makeText(MainActivity.this, "Đã bật máy bơm", Toast.LENGTH_SHORT).show();
                            break;
                        case "offMayBom":
                            Toast.makeText(MainActivity.this, "Đã tắt máy bơm", Toast.LENGTH_SHORT).show();
                            break;
                        case "onDenLed":
                            Toast.makeText(MainActivity.this, "Đã bật đèn led", Toast.LENGTH_SHORT).show();
                            break;
                        case "offDenLed":
                            Toast.makeText(MainActivity.this, "Đã tắt đèn led", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi Server, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }
}
