package nt.volley.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private RequestQueue requestQueue;

    String REGISTER_URL = "http://10.0.2.2/login/register.php";
    String LOGIN_URL = "http://10.0.2.2/login/login.php";
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        // deklarasi variabel dari kelas volley
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        // Kondisi ketika button diklik, klik register atau login

        if (v == btnRegister){
            // mengambil data dari form
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // memanggil fungsi register dengan mengisi parameter yang dibutuhkan
            register(name, email, password);
        }
        if (v == btnLogin){
            // mengambil data dari form
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // memanggil fungsi login dengan mengisi parameter email dan password
            login(email, password);
        }
    }

    // Fungsi login dengan menggunakan volley library
    private void login(String email, String password) {

        // mengisi parameter
        JSONObject params = null;
        try{
            params = new JSONObject();
            params.put("email", email);
            params.put("password", password);
        }catch (JSONException e){
            e.printStackTrace();
        }

        // handle respon dari eksekusi api
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // kondisi jika ada respon dari api
                try {
                    // parsing data dari hasil eksekusi dalam bentuk json
                    boolean error = response.getBoolean("error");
                    Log.d(TAG, "Error : "+error);
                    // dilakukan pengecekan, jika pesar error false maka berhasil
                    if (!error) {
                        // parsing data json
                        JSONObject user = response.getJSONObject("user");
                        int kode = response.getInt("kode");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        // menampilkan data
                        Toast.makeText(getApplication(), "Selamat login berhasil : name : "+name+" and email : "+email+" and kode : "+kode,Toast.LENGTH_LONG).show();
                    }else{
                        // kondisi respon error
                        String error_message = response.getString("error_msg");
                        Toast.makeText(getApplication(), error_message,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // kondisi tidak ada respon dari api
                Toast.makeText(getApplication(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

        /*pemanggilan fungsi untuk request api, dengan mengisikan method POST, URL, parameter, listener, dan errorListener*/
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,LOGIN_URL, params,listener, error);
        Log.d(TAG, request.getUrl());
        Log.d(TAG, params.toString());
        // memasukan request ke antrian eksekusi
        requestQueue.add(request);
    }

    // fungsi register dengan volley library
    private void register(String name, String email, String password) {
        // pengisian parameter yang akan disimpan pada database
        JSONObject params = null;
        try{
            params = new JSONObject();
            params.put("name", name);
            params.put("email", email);
            params.put("password", password);
        }catch (JSONException e){
            e.printStackTrace();
        }


        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean error = response.getBoolean("error");
                    Log.d(TAG, "Error : "+error);
                    if (!error) {
                        JSONObject user = response.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        Toast.makeText(getApplication(), "Selamat registrasi berhasil : name : "+name+" and email : "+email,Toast.LENGTH_LONG).show();
                    }else{
                        String error_message = response.getString("error_msg");
                        Toast.makeText(getApplication(), error_message,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,REGISTER_URL, params,listener, error);
        Log.d(TAG, request.getUrl());
        Log.d(TAG, params.toString());

        requestQueue.add(request);

    }
}
