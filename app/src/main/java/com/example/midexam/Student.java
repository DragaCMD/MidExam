package com.example.midexam;

public class Student {
    private final String studentId;
    private final String name;
    private final boolean enrolled;

    public Student(String studentId, String name, boolean enrolled) {
        this.studentId = studentId;
        this.name = name;
        this.enrolled = enrolled;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public boolean isEnrolled() { return enrolled; }
}