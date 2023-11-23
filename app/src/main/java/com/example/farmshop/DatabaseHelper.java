package com.example.farmshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FarmAppDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_ITEM_IMAGE = "item_image";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(createTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_IMAGE + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_PRICE + " String " +
                ")";
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Insert a new user into the users table
    public long insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long newRowId = db.insert(TABLE_USERS, null, values);
        db.close();
        return newRowId;
    }

    // Query a user by email and check the entered password
    public User getUserByEmail(String email, String enteredPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

            // Check if the entered password matches the stored password
            if (enteredPassword.equals(storedPassword)) {
                user = new User(userId, email, storedPassword);
            }

            cursor.close();
        }

        db.close();
        return user;
    }

    // Insert cart details
    public long insertCartDetails(String userEmail, String itemName, String itemImage, int itemQuantity, String itemPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ITEM_IMAGE, itemImage);
        values.put(COLUMN_QUANTITY, itemQuantity);
        values.put(COLUMN_PRICE, itemPrice);

        long newRowId = db.insert(TABLE_CART, null, values);
        db.close();
        return newRowId;
    }


    // Method to retrieve cart items by user email
    public List<CartItem> getCartItems(String userEmail) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_USER_EMAIL,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_IMAGE,
                COLUMN_QUANTITY,
                COLUMN_PRICE
        };

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
                String itemImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_IMAGE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));

                // Create a CartItem object
                CartItem cartItem = new CartItem(itemName, itemImage, quantity, price, userEmail);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return cartItems;
    }


    // Update the quantity of an item in the cart
    public int updateCartItemQuantity(String userEmail, String itemImage, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);

        String whereClause = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_ITEM_IMAGE + " = ?";
        String[] whereArgs = {userEmail, itemImage};

        int rowsUpdated = db.update(TABLE_CART, values, whereClause, whereArgs);
        db.close();

        return rowsUpdated;
    }

    public boolean isItemInCart(String userEmail, String itemImage) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_EMAIL, COLUMN_ITEM_IMAGE};
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_ITEM_IMAGE + " = ?";
        String[] selectionArgs = {userEmail, itemImage};
        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null);

        boolean itemExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return itemExists;
    }

    public boolean deleteCartItem(String userEmail, String itemImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_ITEM_IMAGE + " = ?";
        String[] whereArgs = {userEmail, itemImage};
        int rowsDeleted = db.delete(TABLE_CART, whereClause, whereArgs);
        db.close();
        return rowsDeleted > 0; // Return true if any rows were deleted, indicating success
    }




}
