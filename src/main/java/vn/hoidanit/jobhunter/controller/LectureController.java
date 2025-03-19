package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Lecture;
import vn.hoidanit.jobhunter.domain.Student;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.lecture.ResCreateLectureDTO;
import vn.hoidanit.jobhunter.domain.response.lecture.ResUpdateLectureDTO;
import vn.hoidanit.jobhunter.domain.response.student.ResUpdateStudentDTO;
import vn.hoidanit.jobhunter.repository.LectureRepository;
import vn.hoidanit.jobhunter.service.LectureService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

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
public class LectureController {
    private final LectureRepository lectureRepository;
    private final LectureService lectureService;

    public LectureController(LectureRepository lectureRepository, LectureService lectureService) {
        this.lectureRepository = lectureRepository;
        this.lectureService = lectureService;
    }

    // create new lecture
    @PostMapping("/lectures")
    @ApiMessage("Create a new lecture")
    public ResponseEntity<ResCreateLectureDTO> createLecture(@Valid @RequestBody Lecture lecture) {
        Lecture newLec = this.lectureService.handleCreateLecture(lecture);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.lectureService.convertToResCreateLectureDTO(newLec));
    }

    // fetch all lectures
    @GetMapping("/lectures")
    @ApiMessage("fetch all lectures")
    public ResponseEntity<ResultPaginationDTO> getAllLecture(
            @Filter Specification<Lecture> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.lectureService.fetchAllLecture(spec, pageable));
    }

    // update lecture
    @PutMapping("/lectures")
    public ResponseEntity<ResUpdateLectureDTO> updateLecture(@RequestBody Lecture upLecture) {
        Lecture lectureUpdate = this.lectureService.handleUpdateLecture(upLecture);
        return ResponseEntity.ok().body(this.lectureService.convertToResUpdateLectureDTO(lectureUpdate));
    }

    // delete lecture
    @DeleteMapping("/lectures/{id}")
    public ResponseEntity<String> deleteLecture(@PathVariable("id") long id) {
        this.lectureService.handleDeleteLecture(id);
        return ResponseEntity.ok().body("delete successfully lecture with id= " + id);
    }
}
