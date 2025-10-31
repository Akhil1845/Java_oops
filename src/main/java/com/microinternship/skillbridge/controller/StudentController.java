package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.Interest;
import com.microinternship.skillbridge.repository.StudentRepository;
import com.microinternship.skillbridge.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*") // allow all origins (adjust if needed)
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InterestRepository interestRepository;

    // ---------- REGISTER ----------
    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Optional<Student> existing = studentRepository.findByEmail(student.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Student saved = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<Student> loginStudent(@RequestBody Student loginRequest) {
        return studentRepository
                .findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // ---------- GET STUDENT ----------
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------- UPDATE PROFILE ----------
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @RequestBody Student updated) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = optional.get();

        // Update simple fields
        if (updated.getName() != null) student.setName(updated.getName());
        if (updated.getEmail() != null) student.setEmail(updated.getEmail());
        if (updated.getClassName() != null) student.setClassName(updated.getClassName());
        if (updated.getStream() != null) student.setStream(updated.getStream());
        if (updated.getPhone() != null) student.setPhone(updated.getPhone());
        if (updated.getAddress() != null) student.setAddress(updated.getAddress());
        if (updated.getProgram() != null) student.setProgram(updated.getProgram());
        if (updated.getSemester() != null) student.setSemester(updated.getSemester());
        if (updated.getSkills() != null) student.setSkills(updated.getSkills());

        // Update interests (expects array of {id: 1}, {id:2})
        if (updated.getInterests() != null) {
            Set<Interest> interests = new HashSet<>();
            for (Interest interest : updated.getInterests()) {
                interestRepository.findById(interest.getId())
                        .ifPresent(interests::add);
            }
            student.setInterests(interests);
        }

        Student saved = studentRepository.save(student);
        return ResponseEntity.ok(saved);
    }

    // ---------- LIST ALL INTERESTS ----------
    @GetMapping("/interests")
    public ResponseEntity<List<Interest>> getAllInterests() {
        return ResponseEntity.ok(interestRepository.findAll());
    }
}
