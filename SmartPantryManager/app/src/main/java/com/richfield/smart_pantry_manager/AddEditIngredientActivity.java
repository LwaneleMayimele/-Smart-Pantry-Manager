package com.richfield.smart_pantry_manager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
// Explicitly importing the project's generated resource mapping pointer
import com.richfield.smart_pantry_manager.R;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutQty, layoutUnit;
    private TextInputEditText editName, editQty, editUnit, editExpiry;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DatabaseHelper(this);

        // Bind Views
        layoutName = findViewById(R.id.layout_input_name);
        layoutQty = findViewById(R.id.layout_input_qty);
        layoutUnit = findViewById(R.id.layout_input_unit);

        editName = findViewById(R.id.edit_ingredient_name);
        editQty = findViewById(R.id.edit_ingredient_qty);
        editUnit = findViewById(R.id.edit_ingredient_unit);
        editExpiry = findViewById(R.id.edit_ingredient_expiry);
        btnSave = findViewById(R.id.btn_save_ingredient);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveData();

            }
        });
    }

    private void validateAndSaveData() {
        // Clear any previous error states
        layoutName.setError(null);
        layoutQty.setError(null);
        layoutUnit.setError(null);

        // Collect string values from form fields
        String nameInput = editName.getText().toString().trim();
        String qtyInput = editQty.getText().toString().trim();
        String unitInput = editUnit.getText().toString().trim();
        String expiryInput = editExpiry.getText().toString().trim();

        boolean isValid = true;

        // 1. Validate Ingredient Name
        if (TextUtils.isEmpty(nameInput)) {
            layoutName.setError("Ingredient name is required");
            isValid = false;
        } else if (nameInput.length() < 2) {
            layoutName.setError("Name must be at least 2 characters long");
            isValid = false;
        }

        // 2. Validate Quantity
        double cleanQuantity = 0;
        if (TextUtils.isEmpty(qtyInput)) {
            layoutQty.setError("Quantity is required");
            isValid = false;
        } else {
            try {
                cleanQuantity = Double.parseDouble(qtyInput);
                if (cleanQuantity <= 0) {
                    layoutQty.setError("Quantity must be greater than zero");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                layoutQty.setError("Please enter a valid numeric value");
                isValid = false;
            }
        }

        // 3. Validate Unit Measurement
        if (TextUtils.isEmpty(unitInput)) {
            layoutUnit.setError("Unit is required (e.g., g, kg, pcs)");
            isValid = false;
        }

        // Execute save statement if all criteria are satisfied
        if (isValid) {
            boolean isInserted = dbHelper.addPantryItem(
                    nameInput,
                    cleanQuantity,
                    unitInput,
                    TextUtils.isEmpty(expiryInput) ? "N/A" : expiryInput
            );

            if (isInserted) {
                setResult(RESULT_OK);
                finish();
            } else {
                layoutName.setError("Failed to save. Item may already exist.");
            }
        }
    }
}
