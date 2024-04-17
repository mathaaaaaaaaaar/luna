package com.example.period1;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.period1.databinding.ActivityPeriodInputBinding;

import java.util.Calendar;

public class PeriodInputActivity extends AppCompatActivity {

    ActivityPeriodInputBinding periodInputBinding;

    private EditText nameEditText;
    private EditText periodDurationEditText;
    private EditText lastPeriodDateEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText cycleWindowEditText;
    private Button saveButton;

    private SharedPreferences sharedPreferences;

    DBHelper dbh;
    boolean insertStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        periodInputBinding = ActivityPeriodInputBinding.inflate(getLayoutInflater());
        View view = periodInputBinding.getRoot();
        setContentView(view);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        periodDurationEditText = findViewById(R.id.periodDurationEditText);
        lastPeriodDateEditText = findViewById(R.id.lastPeriodDateEditText);
        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        cycleWindowEditText = findViewById(R.id.cycleWindowEditText);
        saveButton = findViewById(R.id.saveButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
initWidgets();

dbh = new DBHelper(this);

        // Set onClickListener for Last Period Date EditText
        lastPeriodDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }
    private void initWidgets() {
        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeriodInputActivity.this, WeekViewActivity.class);
                startActivity(intent);
            }
        });

        ImageView calendarIcon = findViewById(R.id.calendar_icon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the PeriodInputActivity
                startActivity(new Intent(PeriodInputActivity.this, MainActivity.class));
            }
        });



    }
    private void saveUserData() {
        UserData usrObj = CreateUser();
        insertStatus = dbh.InsertUser(usrObj);

        if (insertStatus) {
            Toast.makeText(this, "User Added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        // Retrieve data from EditText fields
        String name = nameEditText.getText().toString();
        String periodDurationText = periodDurationEditText.getText().toString();
        String lastPeriodDate = lastPeriodDateEditText.getText().toString();
        String weightText = weightEditText.getText().toString();
        String heightText = heightEditText.getText().toString();
        String cycleWindowText = cycleWindowEditText.getText().toString();

        // Check if any field is empty
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(periodDurationText) ||
                TextUtils.isEmpty(lastPeriodDate) || TextUtils.isEmpty(weightText) ||
                TextUtils.isEmpty(heightText) || TextUtils.isEmpty(cycleWindowText)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse data to appropriate types
        int periodDuration;
        try {
            periodDuration = Integer.parseInt(periodDurationText);
            if (periodDuration < 1 || periodDuration > 14) {
                Toast.makeText(this, "Please enter period duration between 1 and 14", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid period duration", Toast.LENGTH_SHORT).show();
            return;
        }

        int cycleWindow;
        try {
            cycleWindow = Integer.parseInt(cycleWindowText);
            if (cycleWindow < 10 || cycleWindow > 35) {
                Toast.makeText(this, "Please enter cycle window between 10 and 35", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid cycle window", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse other data
        int weight;
        int height;
        try {
            weight = Integer.parseInt(weightText);
            height = Integer.parseInt(heightText);
        } catch (NumberFormatException e) {
            // Handle invalid input for weight and height if necessary
            return;
        }



        // Save data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putInt("PeriodDuration", periodDuration);
        editor.putString("LastPeriodDate", lastPeriodDate);
        editor.putInt("Weight", weight);
        editor.putInt("Height", height);
        editor.putInt("CycleWindow", cycleWindow);
        editor.apply();

        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    public UserData CreateUser() {
        UserData objUsr1 = new UserData();
        objUsr1.setName(periodInputBinding.nameEditText.getText().toString().trim());
        objUsr1.setPeriodDuration(Integer.parseInt(periodInputBinding.periodDurationEditText.getText().toString().trim()));
        objUsr1.setLastPeriodDate(periodInputBinding.lastPeriodDateEditText.getText().toString().trim());
        objUsr1.setHeight(Integer.parseInt(periodInputBinding.heightEditText.getText().toString().trim()));
        objUsr1.setWeight(Integer.parseInt(periodInputBinding.weightEditText.getText().toString().trim()));
        objUsr1.setCycleWindow(Integer.parseInt(periodInputBinding.cycleWindowEditText.getText().toString().trim()));
        return objUsr1;
    }




    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update EditText with selected date
                        lastPeriodDateEditText.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // Custom InputFilter to restrict input to a specific range
    private class InputFilterMinMax implements InputFilter {
        private final int min;
        private final int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                // Allow empty input
                if (TextUtils.isEmpty(source)) return null;

                // Convert input to integer
                int input = Integer.parseInt(dest.toString() + source.toString());

                // Check if input is within the specified range
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException ignored) {
            }
            // Return empty string if input is not within the specified range
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

}


