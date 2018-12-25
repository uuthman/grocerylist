package com.example.android.grocerylistitem.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.grocerylistitem.Data.DatabaseHandler;
import com.example.android.grocerylistitem.Model.Grocery;
import com.example.android.grocerylistitem.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button addItem;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        byPassActivity();

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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    startActivity(new Intent(MainActivity.this, MyListActivity.class));
                }catch(Exception ex){
                    Log.d("Error: ", String.valueOf(ex.getMessage()));
                }

            }
        }, 1300);
    }

    public void byPassActivity(){

        try{
            if (db.getGroceryCount() > 0){
                startActivity(new Intent(MainActivity.this, MyListActivity.class));
                finish();
            }
        }catch (Exception ex){
            Log.d("ERROR: ", String.valueOf(ex.getMessage()));
        }

    }
}
