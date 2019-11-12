package com.example.recyclerviewandsqllite;

import android.provider.BaseColumns;

public class GroceryContract {
    //This class is used for implementing table parameters
    private GroceryContract() {

    }

    public static final class GroceryEntry implements BaseColumns {
        public static final String TABLE_NAME = "groceryList"; //Table Name
        public static final String COLUMN_NAME = "name"; //First column, will contain names
        public static final String COLUMN_AMOUNT = "amount"; //Second column will contain amounts
        public static final String COLUMN_TIMESTAMP = "timestamp"; //Last column will contain timestamp for later ordering
    }

}
