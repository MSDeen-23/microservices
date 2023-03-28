package com.hellosudo.product.controller;

import com.hellosudo.product.entity.Student;
import com.hellosudo.product.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public long createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

    @GetMapping
    public List<Student> getStudents(){
        return  studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudents(@PathVariable Long id){
        return  studentService.getStudent(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id){
          studentService.deleteStudent(id);

    }

    @PutMapping
    public void updateStudent(@RequestBody Student student){
        studentService.updateStudent(student);

    }

}
