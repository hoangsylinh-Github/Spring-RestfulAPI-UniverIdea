package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.Student;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.department.ResUpdateDepartDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResCreateStudentDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResUpdateStudentDTO;
import vn.hoidanit.jobhunter.repository.StudentRepository;
import vn.hoidanit.jobhunter.service.StudentService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;

    }

    // create new student
    @PostMapping("/students")
    @ApiMessage("Create a new student")
    public ResponseEntity<ResCreateStudentDTO> createStudent(@Valid @RequestBody Student student) {
        Student newStu = this.studentService.handleCreateStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.studentService.convertToResCreateStudentDTO(newStu));
    }

    // fetch all students
    @GetMapping("/students")
    @ApiMessage("fetch all students")
    public ResponseEntity<ResultPaginationDTO> getAllStudent(
            @Filter Specification<Student> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.studentService.fetchAllStudent(spec, pageable));
    }

    // update students
    @PutMapping("/students")
    public ResponseEntity<ResUpdateStudentDTO> updateStudent(@RequestBody Student upStudent) {
        Student studentUpdate = this.studentService.handleUpdateStudent(upStudent);
        return ResponseEntity.ok().body(this.studentService.convertToResUpdateStudentDTO(studentUpdate));
    }

    // delete department
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") long id) {
        this.studentService.handleDeleteStudent(id);
        return ResponseEntity.ok().body("delete successfully student with id= " + id);
    }

}
