package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.Student;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResCreateStudentDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResStudentDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResUpdateStudentDTO;
import vn.hoidanit.jobhunter.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UniversityService universityService;
    private final DepartmentService departmentService;

    public StudentService(StudentRepository studentRepository, UniversityService universityService,
            DepartmentService departmentService) {
        this.studentRepository = studentRepository;
        this.universityService = universityService;
        this.departmentService = departmentService;
    }

    public Student handleCreateStudent(Student student) {
        if (student.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(student.getUniversity().getId());
            student.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        if (student.getDepartment() != null) {
            Optional<Department> departOptional = this.departmentService.findById(student.getDepartment().getId());
            student.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
        }
        return this.studentRepository.save(student);
    }

    public ResCreateStudentDTO convertToResCreateStudentDTO(Student student) {
        ResCreateStudentDTO resStudent = new ResCreateStudentDTO();
        ResCreateStudentDTO.StudentUni uni = new ResCreateStudentDTO.StudentUni();
        ResCreateStudentDTO.StudentDepart depart = new ResCreateStudentDTO.StudentDepart();

        resStudent.setId(student.getId());
        resStudent.setName(student.getName());
        resStudent.setStudentId(student.getStudentId());
        resStudent.setClassroom(student.getClassroom());
        resStudent.setEmail(student.getEmail());
        resStudent.setPhone(student.getPhone());
        resStudent.setCreatedAt(student.getCreatedAt());

        if (student.getUniversity() != null) {
            uni.setId(student.getUniversity().getId());
            uni.setName(student.getUniversity().getName());
            resStudent.setUniversity(uni);
        }

        if (student.getDepartment() != null) {
            depart.setId(student.getDepartment().getId());
            depart.setName(student.getDepartment().getName());
            resStudent.setDepartment(depart);
        }
        return resStudent;
    }

    public ResultPaginationDTO fetchAllStudent(Specification<Student> spec, Pageable pageable) {
        Page<Student> pageStudent = this.studentRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageStudent.getTotalPages());
        mt.setTotal(pageStudent.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResStudentDTO> listStudent = pageStudent.getContent()
                .stream().map(item -> new ResStudentDTO(
                        item.getId(),
                        item.getName(),
                        item.getStudentId(),
                        item.getClassroom(),
                        item.getEmail(),
                        item.getPhone(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResStudentDTO.StudentUni(
                                item.getUniversity() != null ? item.getUniversity().getId() : 0,
                                item.getUniversity() != null ? item.getUniversity().getName() : null),
                        new ResStudentDTO.StudentDepart(
                                item.getDepartment() != null ? item.getDepartment().getId() : 0,
                                item.getDepartment() != null ? item.getDepartment().getName() : null)))
                .collect(Collectors.toList());
        rs.setResult(listStudent);
        return rs;
    }

    public Student handleUpdateStudent(Student reqStudent) {
        Optional<Student> studentOptional = this.studentRepository.findById(reqStudent.getId());
        if (studentOptional.isPresent()) {
            Student currentStudent = studentOptional.get();
            currentStudent.setName(reqStudent.getName());
            currentStudent.setStudentId(reqStudent.getStudentId());
            currentStudent.setClassroom(reqStudent.getClassroom());
            currentStudent.setEmail(reqStudent.getEmail());
            currentStudent.setPhone(reqStudent.getPhone());

            // check university
            if (reqStudent.getUniversity() != null) {
                Optional<University> uniOptional = this.universityService.findById(reqStudent.getUniversity().getId());
                reqStudent.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
                currentStudent.setUniversity(reqStudent.getUniversity());
            }

            // check department
            if (reqStudent.getDepartment() != null) {
                Optional<Department> departOptional = this.departmentService
                        .findById(reqStudent.getDepartment().getId());
                reqStudent.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
                currentStudent.setDepartment(reqStudent.getDepartment());
            }
            return this.studentRepository.save(currentStudent);
        }
        return null;
    }

    public ResUpdateStudentDTO convertToResUpdateStudentDTO(Student student) {
        ResUpdateStudentDTO resStudent = new ResUpdateStudentDTO();
        ResUpdateStudentDTO.StudentUni uni = new ResUpdateStudentDTO.StudentUni();
        ResUpdateStudentDTO.StudentDepart depart = new ResUpdateStudentDTO.StudentDepart();

        if (student.getUniversity() != null) {
            uni.setId(student.getUniversity().getId());
            uni.setName(student.getUniversity().getName());
            resStudent.setUniversity(uni);
        }

        if (student.getDepartment() != null) {
            depart.setId(student.getDepartment().getId());
            depart.setName(student.getDepartment().getName());
            resStudent.setDepartment(depart);
        }

        resStudent.setId(student.getId());
        resStudent.setName(student.getName());
        resStudent.setStudentId(student.getStudentId());
        resStudent.setClassroom(student.getClassroom());
        resStudent.setEmail(student.getEmail());
        resStudent.setPhone(student.getPhone());
        resStudent.setUpdatedAt(student.getUpdatedAt());
        return resStudent;
    }

    public void handleDeleteStudent(long id) {
        this.studentRepository.deleteById(id);
    }
}
