package com.gmail.hafid.projekuas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class ReadActivity extends AppCompatActivity {
    private String c_id;
    private Integer pages;
    private String komik_id;
    private String chapter;
    private String title;

    private ArrayList<ReadModel> readModel;
    private ReadAdapter readAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get id
        Intent intent = getIntent();
        c_id = intent.getStringExtra("c_id");
        pages = intent.getIntExtra("pages", 0);
        komik_id = intent.getStringExtra("komik_id");
        chapter = intent.getStringExtra("chapter");
        title = intent.getStringExtra("title");
        // you must assign all objects to avoid nullPointerException
        readModel = new ArrayList<>();
        readAdapter = new ReadAdapter(readModel);

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.chapter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // we can now set adapter to recyclerView;
        recyclerView.setAdapter(readAdapter);
        setTitle(title + " : " + chapter);

        loadData();
    }

    private void loadData() {
        for (int i = 0; i < pages; i++) {
            String page = String.valueOf((i + 1));
            String img = "http://" + AppConfig.IP_SERVER + "/komik/img/" + komik_id + "/" + chapter + "/" + komik_id +
                    "_" + chapter + "_" + (i + 1) + ".jpg";
            Log.v("ORAMETU", img);
            readModel.add(new ReadModel(page, img));
            readAdapter.notifyDataSetChanged();
        }

    }

}
