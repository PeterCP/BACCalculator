package dev.petercp.baccalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDriverActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static class DatePickerFragment extends DialogFragment {

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR),
                    month = c.get(Calendar.MONTH),
                    day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

    private Date date;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[] { "Hombre", "Mujer" });
        Spinner genderSpinner = (Spinner) findViewById(R.id.input_gender);
        genderSpinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        date = Calendar.getInstance().getTime();
    }

    public void onSetDateButtonPressed(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onSubmitButtonPressed(View view) {
        String licenseStr, name, firstSurname, lastSurname, gender, plate, weightStr, consumedStr;
        int license, weight, consumed;

        licenseStr = ((EditText) findViewById(R.id.input_license)).getText().toString();
        try {
            license = Integer.valueOf(licenseStr);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        name = ((EditText) findViewById(R.id.input_name)).getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre no debe estar vacio.", Toast.LENGTH_SHORT).show();
            return;
        }

        firstSurname = ((EditText) findViewById(R.id.input_first_surname)).getText().toString();
        if (firstSurname.isEmpty()) {
            Toast.makeText(this, "El primer apellido no debe estar vacio.", Toast.LENGTH_SHORT).show();
            return;
        }

        lastSurname = ((EditText) findViewById(R.id.input_last_surname)).getText().toString();
        if (lastSurname.isEmpty()) {
            Toast.makeText(this, "El segundo apellido no debe estar vacio.", Toast.LENGTH_SHORT).show();
            return;
        }

        gender = ((Spinner) findViewById(R.id.input_gender)).getSelectedItem().toString();
        if (gender.equals("Hombre")) {
            gender = "male";
        } else if (gender.equals("Mujer")) {
            gender = "female";
        } else {
            Toast.makeText(this, "El campo Género tiene un valor inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        plate = ((EditText) findViewById(R.id.input_plate)).getText().toString();
        if (plate.isEmpty()) {
            Toast.makeText(this, "La placa no debe estar vacio.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date == null) {
            Toast.makeText(this, "La fecha no debe estar vacio.", Toast.LENGTH_SHORT).show();
            return;
        }

        weightStr = ((EditText) findViewById(R.id.input_weight)).getText().toString();
        try {
            weight = Integer.valueOf(weightStr);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        consumedStr = ((EditText) findViewById(R.id.input_consumed)).getText().toString();
        try {
            consumed = Integer.valueOf(consumedStr);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        double r = gender.equals("male") ? 0.68 : 0.55;
        double bac = (consumed / (weight * r)) * 100;

        DriverEntry driverEntry = new DriverEntry(license, name, firstSurname, lastSurname,
                gender, plate, date, bac);
        DatabaseHelper db = new DatabaseHelper(this);
        db.saveDriverEntry(driverEntry);
        finish();
    }
}
