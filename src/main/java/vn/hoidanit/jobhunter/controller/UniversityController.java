package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UniversityService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    // create university
    @PostMapping("/universities")
    public ResponseEntity<University> createUni(@Valid @RequestBody University reqUni) {
        University newUni = this.universityService.handleCreateUni(reqUni);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUni);
    }

    // fetch all uni
    @GetMapping("/universities")
    @ApiMessage("fetch all universities")
    public ResponseEntity<ResultPaginationDTO> getAllUni(
            @Filter Specification<University> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.universityService.fetchAllUni(spec, pageable));
    }

    // update uni
    @PutMapping("/universities")
    public ResponseEntity<University> updateUni(@RequestBody University upUni) {
        University uniUpdate = this.universityService.handleUpdateUni(upUni);
        return ResponseEntity.ok().body(uniUpdate);
    }

    // delete uni
    @DeleteMapping("/universities/{id}")
    public ResponseEntity<String> deleteUni(@PathVariable("id") long id) {
        this.universityService.handleDeleteUni(id);
        return ResponseEntity.ok().body("delete successfully uni with id= " + id);
    }
}
