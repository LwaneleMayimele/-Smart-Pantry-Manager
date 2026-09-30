package com.richfield.smart_pantry_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        ListView listView = findViewById(R.id.list_view_suggestions);
        TextView txtEmptyState = findViewById(R.id.text_empty_state_feedback);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<MatchingEngine.RecipeModel> dynamicMatches = MatchingEngine.getStrictMatches(dbHelper);
        List<String> displayNames = new ArrayList<>();

        for (MatchingEngine.RecipeModel r : dynamicMatches) {
            displayNames.add(r.name + "\nInstructions: " + r.instructions);
        }

        if (displayNames.isEmpty()) {
            txtEmptyState.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            txtEmptyState.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayNames);
            listView.setAdapter(adapter);
        }
    }
}
