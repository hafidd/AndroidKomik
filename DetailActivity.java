package com.gmail.hafid.projekuas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private String c_id, title, img, author;
    private TextView text_title, text_author, text_desc;
    private ImageView image;
    private Button btn_fav;

    // initialize adapter and data structure here
    private ChapterAdapter chapterAdapter;
    private ArrayList<RecyclerModel> recyclerModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get intent
        Intent i = getIntent();
        c_id = i.getStringExtra("c_id");
        title = i.getStringExtra("title");
        img = i.getStringExtra("img");
        author = i.getStringExtra("author");
        // get view
        text_title = (TextView) findViewById(R.id.det_title);
        text_author = (TextView) findViewById(R.id.det_author);
        text_desc = (TextView) findViewById(R.id.det_desc);
        image = (ImageView) findViewById(R.id.det_img);
        btn_fav = (Button) findViewById(R.id.btn_fav);
        // set view
        text_title.setText(title);
        text_author.setText(author);
        // set img
        Context context = getApplicationContext();
        Picasso.with(context)
                .load(img)
                .error(R.drawable.default_img)
                .into(image);
        setDet();
        // title
        this.setTitle("tessss");
        // you must assign all objects to avoid nullPointerException
        recyclerModels = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(recyclerModels);

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //we can now set adapter to recyclerView;
        recyclerView.setAdapter(chapterAdapter);
        // load data
        loadData();

        // add fav
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorite();
            }
        });

        // title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);

    }

    private void setDet() {
        Integer uid = SharedPrefManager.getInstance(this).getUser().getUid();
        String url = "http://" + AppConfig.IP_SERVER + "/komik/load_comic.php?c_id=" + c_id + "&uid=" + uid;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    text_desc.setText(jsonObject.getString("desc"));
                    Boolean fav = (1 == jsonObject.getInt("fav"));
                    if(fav){
                        btn_fav.setText("Hapus dari favorit");
                        btn_fav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delFromFavorite();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ORAMETU", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);

    }

    private void loadData() {

        String url = "http://" + AppConfig.IP_SERVER + "/komik/load_chapter.php?c_id=" + c_id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() <= 0) {
                    // no data available
                    Toast.makeText(DetailActivity.this, "No chapter available", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String ch_id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("description");
                        String img = "http://" + AppConfig.IP_SERVER + "/" + jsonObject.getString("img");

                        String komik_id = jsonObject.getString("komik_id");
                        String chapter = jsonObject.getString("chapter");
                        Integer pages = Integer.parseInt(jsonObject.getString("pages"));

                        recyclerModels.add(new RecyclerModel(ch_id, komik_id, chapter, pages, title, description, img));
                        chapterAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also here, volley is not processing, unlock it should load more
                Toast.makeText(DetailActivity.this, "network error!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(DetailActivity.this)
                        .setMessage(error.toString())
                        .show();
            }
        });

        Volley.newRequestQueue(this).add(jsonArrayRequest);

    }

    private void addToFavorite() {
        // cek logged in?
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            // finish();
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("act", "det");
            startActivity(i);
            return;
        }
        final Integer uid = SharedPrefManager.getInstance(this).getUser().getUid();
        String url = "http://" + AppConfig.IP_SERVER + "/komik/favorite.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ORAMETU", response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            // sukses
                            if (!obj.getBoolean("error")) {
                                btn_fav.setText("Hapus dari favorit");
                                btn_fav.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        delFromFavorite();
                                    }
                                });
                            }
                            // message
                            Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("uid", String.valueOf(uid));
                params.put("komik_id", c_id);
                params.put("tipe", "add");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void delFromFavorite(){
        final Integer uid = SharedPrefManager.getInstance(this).getUser().getUid();
        String url = "http://" + AppConfig.IP_SERVER + "/komik/favorite.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ORAMETU", response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            // sukses
                            if (!obj.getBoolean("error")) {
                                btn_fav.setText("Tambah ke favorit");
                                btn_fav.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addToFavorite();
                                    }
                                });
                            }
                            // message
                            Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("uid", String.valueOf(uid));
                params.put("komik_id", c_id);
                params.put("tipe", "del");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}