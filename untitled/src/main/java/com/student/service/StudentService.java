package com.student.service;

import com.student.model.Student;
import com.student.util.DBConnection;
import com.student.service.InvalidMarksException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentService {

    public void addStudent(Student s) throws InvalidMarksException {

        if (s.getMarks() > 100 || s.getMarks() < 0) {
            throw new InvalidMarksException("Marks must be between 0 and 100");
        }

        String studentQuery = "INSERT INTO Student (student_id, name) VALUES (?, ?)";
        String resultQuery = "INSERT INTO Result (result_id, student_id, subject_id, marks) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement(studentQuery);
            ps1.setInt(1, s.getStudentId());
            ps1.setString(2, s.getName());
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(resultQuery);
            ps2.setInt(1, s.getStudentId());
            ps2.setInt(2, s.getStudentId());
            ps2.setInt(3, 1);
            ps2.setDouble(4, s.getMarks());
            ps2.executeUpdate();

            System.out.println("Student + Marks added to database.");

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Student ID already exists. Try a different ID.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayStudents() {

        String query =
                "SELECT s.name, sub.subject_name, r.marks " +
                        "FROM Student s " +
                        "JOIN Result r ON s.student_id = r.student_id " +
                        "JOIN Subject sub ON r.subject_id = sub.subject_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("Student Records (with Subject & Marks):");

            while (rs.next()) {
                System.out.println(
                        "Name: " + rs.getString("name") +
                                " Subject: " + rs.getString("subject_name") +
                                " Marks: " + rs.getInt("marks")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}