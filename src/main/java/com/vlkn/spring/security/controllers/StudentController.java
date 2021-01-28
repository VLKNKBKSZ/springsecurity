package com.vlkn.spring.security.controllers;

import com.vlkn.spring.security.domain.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"James Bond"),
            new Student(2, "Anna Bond"),
            new Student(3, "Ibrahim Bond"),
            new Student(4, "Yigit Bond")
    );
    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS
                .stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow( () -> new IllegalStateException(String.format("Student withs %s is not found", studentId)));
    }
}
