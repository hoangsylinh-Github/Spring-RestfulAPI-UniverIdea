package vn.hoidanit.jobhunter.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.response.ResCreateDepartDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateDepartDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.DepartmentService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // create department
    @PostMapping("/departments")
    public ResponseEntity<ResCreateDepartDTO> createDepart(@Valid @RequestBody Department reqDepart) {
        Department newDepart = this.departmentService.handleCreateDepart(reqDepart);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.departmentService.convertToResCreateDepartDTO(newDepart));
    }

    // fetch all department
    @GetMapping("/departments")
    @ApiMessage("fetch all departments")
    public ResponseEntity<ResultPaginationDTO> getAllDepart(
            @Filter Specification<Department> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.departmentService.fetchAllDepart(spec, pageable));
    }

    // update department
    @PutMapping("/departments")
    public ResponseEntity<ResUpdateDepartDTO> updateDepart(@RequestBody Department upDepart) {
        Department departUpdate = this.departmentService.handleUpdateDepart(upDepart);
        return ResponseEntity.ok().body(this.departmentService.convertToResUpdateDepartDTO(departUpdate));
    }

    // delete department
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<String> deleteDepart(@PathVariable("id") long id) {
        this.departmentService.handleDeleteDepart(id);
        return ResponseEntity.ok().body("delete successfully department with id= " + id);
    }
}
