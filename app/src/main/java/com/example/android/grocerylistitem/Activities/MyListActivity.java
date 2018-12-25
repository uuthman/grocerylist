package com.example.android.grocerylistitem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.grocerylistitem.Data.DatabaseHandler;
import com.example.android.grocerylistitem.Model.Grocery;
import com.example.android.grocerylistitem.R;
import com.example.android.grocerylistitem.Ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button addItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopDialog();
            }
        });

        try{

            db = new DatabaseHandler(MyListActivity.this);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(MyListActivity.this));

       //     groceryList = new ArrayList<>();
            listItems = new ArrayList<>();

            groceryList = db.getAllCategory();

            for(Grocery g : groceryList){

                Grocery grocery = new Grocery();
                grocery.setName(g.getName());
                grocery.setQuantity("Qty: " + g.getQuantity());
                grocery.setId(g.getId());
                grocery.setDateItemAdded("Date: " + g.getDateItemAdded());

                listItems.add(grocery);

            }

            recyclerViewAdapter = new RecyclerViewAdapter(MyListActivity.this, listItems);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

        }catch (Exception ex){
            Log.d("Error: ", String.valueOf(ex.getMessage()));
        }


            }

    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.enter_grocery);
        quantity = (EditText) view.findViewById(R.id.qty);
        addItem = (Button) view.findViewById(R.id.add_item);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: SAVE TO DB
                //TODO: MOVE TO NEXT SCREEN

                if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToDb(view);
                }
            }
        });

    }

    private void saveGroceryToDb(View view) {

        Grocery grocery = new Grocery();

        String newGroceryItem = groceryItem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGroceryItem);
        grocery.setQuantity(newGroceryQuantity);

        db.addGrocery(grocery);

        Snackbar.make(view, "Item Saved!", Snackbar.LENGTH_SHORT).show();

        // Log.d("DB Count", String.valueOf(db.getGroceryCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                try{
                    startActivity(new Intent(MyListActivity.this, MyListActivity.class));
                }catch(Exception ex){
                    Log.d("Error: ", String.valueOf(ex.getMessage()));
                }

            }
        }, 1300);
    }

    }


