package com.hellosudo.product.service;

import com.hellosudo.product.entity.Student;

import java.util.List;


public interface StudentService {
    public long createStudent(Student student);

    List<Student> getAllStudents();

    Student getStudent(Long id);

    void deleteStudent(Long id);

    Student updateStudent(Student student);
}
