package com.example.recyclerviewandsqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDataBase;
    private GroceryAdapter mAdapter;
    private EditText mEditTextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;
    public static final String DATABASE_NAME = "groceryList.db";
    public static final int DATABASE_VERSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this,DATABASE_NAME,DATABASE_VERSION);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this,getAllItems());
        recyclerView.setAdapter(mAdapter);

        mEditTextName = findViewById(R.id.edittext_name);
        mTextViewAmount = findViewById(R.id.textview_amount);

        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonMinus = findViewById(R.id.button_minus);
        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonUpdate = findViewById(R.id.button_update);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void increase() {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void decrease() {
        if(mAmount > 0){
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }

    // Write to Database Table
    private void addItem() {
        if(mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) { // Check if user has not written anything yet
            return;
        }

        String name = mEditTextName.getText().toString();
        // Put values to DataBase table
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);
        mDataBase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();
    }

    //Update a row in database
    private void update() {
        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);
        String selection = GroceryContract.GroceryEntry._ID + " LIKE ?";
        String[] selectionArgs = {"1"};
        //mDatabase.update method Updates a row
        // *First element in update() method is TABLE_NAME
        // *Second element in update() method is Row parameters that should be updated, in our case it is item Name and Amount, if other parameters are
        //not specified they will not be updated
        // *Third element in update() method is column name that, it specify in which column is located our item that should be updated in our case it we
        //search item by it's _ID , so we specify _ID column
        // *Forth element in update() method is items name, in our case we say it's 1st ID in _ID column
        // ___In conclusion__ We replace a row where a specific item is located, in our case it's located in column "selection" -  _ID, and it's name is "selectionArgs" - 1
        mDataBase.update(GroceryContract.GroceryEntry.TABLE_NAME, cv, selection, selectionArgs);
        mAdapter.swapCursor(getAllItems());
    }

    //Read from Database Table
    private Cursor getAllItems() {
        return mDataBase.query(GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC");
    }
}
