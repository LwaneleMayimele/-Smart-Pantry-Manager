package com.richfield.smart_pantry_manager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingEngine {

    public static List<RecipeModel> getStrictMatches(DatabaseHelper dbHelper) {
        List<RecipeModel> matches = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Gather all inventory stock inside a map tracking tool
        Map<String, Double> pantryMap = new HashMap<>();
        Cursor pCursor = dbHelper.getAllPantryItems();
        if (pCursor.moveToFirst()) {
            do {
                String name = pCursor.getString(pCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_NAME));
                double qty = pCursor.getDouble(pCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_QTY));
                pantryMap.put(sanitizeString(name), qty);
            } while (pCursor.moveToNext());
        }
        pCursor.close();

        // 2. Scan every single recipe in the database
        Cursor rCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RECIPES, null);
        if (rCursor.moveToFirst()) {
            do {
                int id = rCursor.getInt(rCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = rCursor.getString(rCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_NAME));
                String steps = rCursor.getString(rCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_INSTRUCTIONS));

                if (verifyRecipeIngredients(db, id, pantryMap)) {
                    matches.add(new RecipeModel(id, name, steps));
                }
            } while (rCursor.moveToNext());
        }
        rCursor.close();
        return matches;
    }

    private static boolean verifyRecipeIngredients(SQLiteDatabase db, int recipeId, Map<String, Double> pantryMap) {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RECIPE_INGREDIENTS +
                " WHERE " + DatabaseHelper.COLUMN_RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        boolean qualifies = true;
        if (c.moveToFirst()) {
            do {
                String reqName = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_NAME));
                double reqQty = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_QTY));

                String cleanKey = sanitizeString(reqName);

                // Strict validation condition check
                if (!pantryMap.containsKey(cleanKey) || pantryMap.get(cleanKey) < reqQty) {
                    qualifies = false;
                    break;
                }
            } while (c.moveToNext());
        } else {
            qualifies = false;
        }
        c.close();
        return qualifies;
    }

    private static String sanitizeString(String input) {
        String base = input.trim().toLowerCase();
        if (base.endsWith("es")) return base.substring(0, base.length() - 2);
        if (base.endsWith("s") && !base.endsWith("ss")) return base.substring(0, base.length() - 1);
        return base;
    }

    public static class RecipeModel {
        public int id;
        public String name;
        public String instructions;

        public RecipeModel(int id, String name, String instructions) {
            this.id = id;
            this.name = name;
            this.instructions = instructions;
        }
    }
}