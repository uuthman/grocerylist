package com.example.android.grocerylistitem.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.grocerylistitem.R;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemName = (TextView) findViewById(R.id.nameDet);
        quantity = (TextView) findViewById(R.id.qtyDet);
        dateAdded = (TextView) findViewById(R.id.dateDet);
        Bundle bundle = getIntent().getExtras();

        try{
            if (bundle != null){
                itemName.setText(bundle.getString("name"));
                quantity.setText(bundle.getString("quantity"));
                dateAdded.setText(bundle.getString("date"));
                groceryId =  bundle.getInt("id");

            }
        }catch (Exception ex){

            Log.d("Error: ", String.valueOf(ex.getMessage()));
        }

    }
}
