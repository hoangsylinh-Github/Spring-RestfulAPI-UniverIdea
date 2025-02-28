package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.service.UniversityService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
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

    // get all uni
    @GetMapping("/universities")
    public ResponseEntity<List<University>> getAllUni() {
        return ResponseEntity.status(HttpStatus.OK).body(this.universityService.fetchAllUni());
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
