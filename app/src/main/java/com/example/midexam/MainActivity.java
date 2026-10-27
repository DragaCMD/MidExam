package com.example.midexam;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etStudentId;
    private CheckBox cbEnrolled;
    private ListView lvStudents;

    private DatabaseHelper dbHelper;
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<String> rows = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        lvStudents = findViewById(R.id.lvStudents);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnView = findViewById(R.id.btnView);

        dbHelper = new DatabaseHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        lvStudents.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> loadStudents());

        // Tap a row to load it into the fields
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student s = students.get(position);
            etName.setText(s.getName());
            etStudentId.setText(s.getStudentId());
            cbEnrolled.setChecked(s.isEnrolled());
        });

        loadStudents();
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        if (!isInputValid(name, id)) return;

        boolean ok = dbHelper.addStudent(new Student(id, name, cbEnrolled.isChecked()));
        if (ok) {
            toast("Student added");
            clearFields();
            loadStudents();
        } else {
            toast("Failed: Student ID already exists");
        }
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        if (!isInputValid(name, id)) return;

        boolean ok = dbHelper.updateStudent(new Student(id, name, cbEnrolled.isChecked()));
        if (ok) {
            toast("Student updated");
            clearFields();
            loadStudents();
        } else {
            toast("No student found with that ID");
        }
    }

    private void deleteStudent() {
        String id = etStudentId.getText().toString().trim();
        if (id.isEmpty()) {
            toast("Enter the Student ID to delete");
            return;
        }
        if (dbHelper.deleteStudent(id)) {
            toast("Student deleted");
            clearFields();
            loadStudents();
        } else {
            toast("No student found with that ID");
        }
    }

    private void loadStudents() {
        students.clear();
        students.addAll(dbHelper.getAllStudents());
        rows.clear();
        for (Student s : students) {
            rows.add(s.getName() + "\nID: " + s.getStudentId()
                    + "  |  Enrolled: " + (s.isEnrolled() ? "Yes" : "No"));
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isInputValid(String name, String id) {
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (id.isEmpty()) {
            etStudentId.setError("Student ID is required");
            return false;
        }
        return true;
    }

    private void clearFields() {
        etName.setText("");
        etStudentId.setText("");
        cbEnrolled.setChecked(false);
        etName.requestFocus();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}