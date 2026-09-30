package com.richfield.smart_pantry_manager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_PANTRY_NAME = "ingredient_name";
    public static final String COLUMN_PANTRY_QTY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    public static final String COLUMN_RI_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RI_NAME = "ingredient_name";
    public static final String COLUMN_RI_QTY = "required_quantity";
    public static final String COLUMN_RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Pantry Table
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PANTRY_NAME + " TEXT UNIQUE, " +
                COLUMN_PANTRY_QTY + " REAL, " +
                COLUMN_PANTRY_UNIT + " TEXT, " +
                COLUMN_PANTRY_EXPIRY + " TEXT);");

        // Create Recipes Table
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RECIPE_INSTRUCTIONS + " TEXT);");

        // Create Recipe Ingredients Table
        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RI_RECIPE_ID + " INTEGER, " +
                COLUMN_RI_NAME + " TEXT, " +
                COLUMN_RI_QTY + " REAL, " +
                COLUMN_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_ID + "));");

        seedInitialRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public boolean addPantryItem(String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, qty);
        values.put(COLUMN_PANTRY_UNIT, unit.trim().toLowerCase());
        values.put(COLUMN_PANTRY_EXPIRY, expiry);

        long result = db.insertWithOnConflict(TABLE_PANTRY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COLUMN_PANTRY_NAME + " ASC", null);
    }

    public boolean updatePantryItem(int id, String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, qty);
        values.put(COLUMN_PANTRY_UNIT, unit.trim().toLowerCase());
        values.put(COLUMN_PANTRY_EXPIRY, expiry);

        int result = db.update(TABLE_PANTRY, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PANTRY, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    private void seedInitialRecipes(SQLiteDatabase db) {
        long r1 = insertRecipe(db, "Scrambled Eggs", "Whisk eggs with salt. Melt butter in a pan over medium heat. Pour in eggs and stir gently until soft curds form.");
        insertRecipeIngredient(db, r1, "egg", 2, "pcs");
        insertRecipeIngredient(db, r1, "butter", 1, "tbsp");
        insertRecipeIngredient(db, r1, "salt", 0.25, "tsp");

        long r2 = insertRecipe(db, "Simple Tomato Pasta", "Boil pasta. In a separate pan, cook crushed tomatoes with olive oil and garlic. Mix together.");
        insertRecipeIngredient(db, r2, "pasta", 200, "g");
        insertRecipeIngredient(db, r2, "tomato", 2, "pcs");
        insertRecipeIngredient(db, r2, "garlic", 1, "clove");
        insertRecipeIngredient(db, r2, "olive oil", 1, "tbsp");

    }

    private long insertRecipe(SQLiteDatabase db, String name, String instructions) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_RECIPE_NAME, name);
        v.put(COLUMN_RECIPE_INSTRUCTIONS, instructions);
        return db.insert(TABLE_RECIPES, null, v);
    }

    private void insertRecipeIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_RI_RECIPE_ID, recipeId);
        v.put(COLUMN_RI_NAME, name.trim().toLowerCase());
        v.put(COLUMN_RI_QTY, qty);
        v.put(COLUMN_RI_UNIT, unit.trim().toLowerCase());
        db.insert(TABLE_RECIPE_INGREDIENTS, null, v);
    }
}