package com.gmail.hafid.projekuas;

import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComicActivity extends AppCompatActivity {
    private ComicAdapter comicAdapter;
    private ArrayList<ComicModel> comicModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        comicModels = new ArrayList<>();
        comicAdapter = new ComicAdapter(comicModels);

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.comic_recycler_view);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        // set adapter
        recyclerView.setAdapter(comicAdapter);
        // load data
        loadData();

    }

    private void loadData() {
        String url = "http://" + AppConfig.IP_SERVER + "/komik/load_comic.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response.length() <= 0) {
                    // no data available
                    Toast.makeText(ComicActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String komik_id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String author = jsonObject.getString("author");
                        String img = "http://" + AppConfig.IP_SERVER + "/komik/img/" + komik_id + "/komik_" + komik_id;

                        comicModels.add(new ComicModel(komik_id, title, img, author));
                        comicAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ORAMETU", "exception", e);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also here, volley is not processing, unlock it should load more
                Toast.makeText(ComicActivity.this, "network error!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(ComicActivity.this)
                        .setMessage(error.toString())
                        .show();
            }
        });

        Volley.newRequestQueue(this).add(jsonArrayRequest);

    }

}
