package com.adarsh.recycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<dataitem> objlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter mAdapter;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      prepareMovieData();
        recyclerView =findViewById(R.id.recycler);

        total=findViewById(R.id.Total);

        mAdapter = new Adapter(objlist,getApplicationContext(),total);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }
    private void prepareMovieData()

    {
        dataitem date = new dataitem(1);
        objlist.add(date);
         date = new dataitem(2);
        objlist.add(date);
         date = new dataitem(3);
        objlist.add(date);

    }

}
