package com.gmail.hafid.projekuas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText pwd_text, username_text;
    ProgressBar progressBar;
    private String act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // cek logged in?
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // get i
        Intent i = getIntent();
        if (i.hasExtra("act")) {
            act = i.getStringExtra("act");
        } else {
            act = "";
        }
        Log.d("ORAMETU", act);

        // view
        pwd_text = (EditText) findViewById(R.id.pwd_text);
        username_text = (EditText) findViewById(R.id.username_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        findViewById(R.id.link_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String username = username_text.getText().toString();
        final String password = pwd_text.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            username_text.setError("Please enter your username");
            username_text.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pwd_text.setError("Please enter your password");
            pwd_text.requestFocus();
            return;
        }

        //if everything is fine
        String url = "http://" + AppConfig.IP_SERVER + "/komik/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ORAMETU", response);
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();

                                //creating a new user object
                                UserModel user = new UserModel(
                                        obj.getInt("uid"),
                                        obj.getString("username")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                if (act.equals("fav")) {
                                    startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}

