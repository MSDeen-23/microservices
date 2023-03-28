package com.hellosudo.product.service;

import com.hellosudo.product.entity.Student;
import com.hellosudo.product.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public long createStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        return savedStudent.getId();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public void deleteStudent(Long id) {
         studentRepository.deleteById(id);
    }

    @Override
    public Student updateStudent(Student student) {
        Student student1 = studentRepository.findById(student.getId()).get();;
        student1.setName(student.getName());
        student1.setAge(student.getAge());
        student1.setEmail(student.getEmail());
        studentRepository.save(student1);
        return student1;
    }


}
