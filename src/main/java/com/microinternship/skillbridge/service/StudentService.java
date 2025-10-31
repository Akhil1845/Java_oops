package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.Interest;
import com.microinternship.skillbridge.repository.StudentRepository;
import com.microinternship.skillbridge.repository.InterestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final InterestRepository interestRepository;

    public StudentService(StudentRepository studentRepository, InterestRepository interestRepository) {
        this.studentRepository = studentRepository;
        this.interestRepository = interestRepository;
    }

    // ================== BASIC CRUD ==================

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Save or update student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // ================== LOGIN ==================
    public Optional<Student> login(String email, String password) {
        return studentRepository.findByEmailAndPassword(email, password);
    }

    // ================== UPDATE PROFILE (field-wise) ==================
    public Optional<Student> updateProfile(Long id, String name, String className, String stream,
                                           String program, String semester, String skills,
                                           String phone, String address) {
        return studentRepository.findById(id).map(student -> {
            if (name != null) student.setName(name);
            if (className != null) student.setClassName(className);
            if (stream != null) student.setStream(stream);
            if (program != null) student.setProgram(program);
            if (semester != null) student.setSemester(semester);
            if (skills != null) student.setSkills(skills);
            if (phone != null) student.setPhone(phone);
            if (address != null) student.setAddress(address);
            return studentRepository.save(student);
        });
    }

    // ================== UPDATE PROFILE (full object) ==================
    public Optional<Student> updateStudentEntity(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            if (updatedStudent.getName() != null) student.setName(updatedStudent.getName());
            if (updatedStudent.getClassName() != null) student.setClassName(updatedStudent.getClassName());
            if (updatedStudent.getStream() != null) student.setStream(updatedStudent.getStream());
            if (updatedStudent.getProgram() != null) student.setProgram(updatedStudent.getProgram());
            if (updatedStudent.getSemester() != null) student.setSemester(updatedStudent.getSemester());
            if (updatedStudent.getSkills() != null) student.setSkills(updatedStudent.getSkills());
            if (updatedStudent.getPhone() != null) student.setPhone(updatedStudent.getPhone());
            if (updatedStudent.getAddress() != null) student.setAddress(updatedStudent.getAddress());

            if (updatedStudent.getInterests() != null && !updatedStudent.getInterests().isEmpty()) {
                student.getInterests().clear();
                for (Interest interest : updatedStudent.getInterests()) {
                    interestRepository.findById(interest.getId())
                            .ifPresent(student.getInterests()::add);
                }
            }

            return studentRepository.save(student);
        });
    }

    // ================== UPDATE INTERESTS ==================
    public Optional<Student> updateInterests(Long studentId, Set<Interest> interests) {
        return studentRepository.findById(studentId).map(student -> {
            student.getInterests().clear();
            for (Interest interest : interests) {
                interestRepository.findById(interest.getId())
                        .ifPresent(student.getInterests()::add);
            }
            return studentRepository.save(student);
        });
    }
}
