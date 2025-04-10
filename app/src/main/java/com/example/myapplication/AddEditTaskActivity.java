package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.taskmanager.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.taskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.taskmanager.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.example.taskmanager.EXTRA_DUE_DATE";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDueDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        calendar = Calendar.getInstance();

        editTextDueDate.setOnClickListener(v -> showDatePickerDialog());

        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));

            long dueDateMillis = getIntent().getLongExtra(EXTRA_DUE_DATE, 0);
            if (dueDateMillis > 0) {
                calendar.setTimeInMillis(dueDateMillis);
                updateDueDateText();
            }
        } else {
            setTitle("Add Task");
        }
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDueDateText();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDueDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        editTextDueDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        Date dueDate = calendar.getTime();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            editTextTitle.requestFocus();
            return;
        }

        if (dueDate.before(new Date())) {
            editTextDueDate.setError("Due date must be in the future");
            editTextDueDate.requestFocus();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DUE_DATE, dueDate.getTime());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_task) {
            saveTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}