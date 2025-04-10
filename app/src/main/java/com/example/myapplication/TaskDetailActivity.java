package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId == -1) {
            finish();
            return;
        }

        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            for (Task t : tasks) {
                if (t.getId() == taskId) {
                    task = t;
                    updateUI();
                    break;
                }
            }
        });
    }

    private void updateUI() {
        if (task == null) {
            finish();  // Corrected line
            return;
        }

        TextView textViewTitle = findViewById(R.id.text_view_title);
        TextView textViewDescription = findViewById(R.id.text_view_description);
        TextView textViewDueDate = findViewById(R.id.text_view_due_date);
        TextView textViewStatus = findViewById(R.id.text_view_status);

        textViewTitle.setText(task.getTitle());
        textViewDescription.setText(task.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        textViewDueDate.setText(sdf.format(task.getDueDate()));

        textViewStatus.setText(task.isCompleted() ? "Completed" : "Pending");
    }
}