package com.richfield.smart_pantry_manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPantry = findViewById(R.id.btn_open_pantry);
        Button btnSuggestions = findViewById(R.id.btn_open_suggestions);
        Button btnSettings = findViewById(R.id.btn_open_settings);

        btnPantry.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEditIngredientActivity.class))
        );

        btnSuggestions.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class))
        );

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );
    }
}