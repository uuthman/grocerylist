package com.example.android.grocerylistitem.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.android.grocerylistitem.Model.Grocery;
import com.example.android.grocerylistitem.Util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.KEY_ID +
                " INTEGER PRIMARY KEY," + Constants.KEY_GROCERY_ITEM + " TEXT," + Constants.KEY_GROCERY_QUANTITY
                + " TEXT," + Constants.KEY_DATE_TIME + " LONG);";

        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    /**
     *  CRUD FOR GROCERY
     */

    //ADD CATEGORY
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_GROCERY_QUANTITY, grocery.getQuantity());
        values.put(Constants.KEY_DATE_TIME, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME,null, values);

        Log.d("SAVED: ", "SAVED TO DB");
    }

    //GET CATEGORY
    public Grocery getCategory(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM,
                Constants.KEY_GROCERY_QUANTITY, Constants.KEY_DATE_TIME}, Constants.KEY_ID + "?",
                new String[] {String.valueOf(id)},null,null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_QUANTITY)));

            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_TIME)))
            .getTime());

            grocery.setDateItemAdded(formatedDate);


        return grocery;
    }


    //GET ALL CATEGORY
    public List<Grocery> getAllCategory(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM,
        Constants.KEY_GROCERY_QUANTITY, Constants.KEY_DATE_TIME}, null, null, null, null,
                Constants.KEY_DATE_TIME + " Desc");

        if (cursor.moveToFirst()){
            do {

                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_QUANTITY)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_TIME)))
                        .getTime());

                grocery.setDateItemAdded(formatedDate);

                //ADD TO GROCERY LIST
                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }
        return groceryList;
    }


    //UPDATE CATEGORY
    public int updateCategory(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_GROCERY_QUANTITY, grocery.getQuantity());
        values.put(Constants.KEY_DATE_TIME, java.lang.System.currentTimeMillis());

        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(grocery.getId())});
    }


    //DELETE CATEGORY
    public void deleteCategory(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[] {String.valueOf(id)});


    }


    //GET COUNT FOR CATEGORY
    public int getGroceryCount(){

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }




}
