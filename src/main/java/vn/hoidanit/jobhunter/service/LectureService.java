package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.Lecture;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.lecture.ResCreateLectureDTO;
import vn.hoidanit.jobhunter.domain.response.lecture.ResLectureDTO;
import vn.hoidanit.jobhunter.domain.response.lecture.ResUpdateLectureDTO;
import vn.hoidanit.jobhunter.repository.LectureRepository;

@Service
public class LectureService {
    public final LectureRepository lectureRepository;
    private final UniversityService universityService;
    private final DepartmentService departmentService;

    public LectureService(LectureRepository lectureRepository,
            UniversityService universityService,
            DepartmentService departmentService) {
        this.lectureRepository = lectureRepository;
        this.universityService = universityService;
        this.departmentService = departmentService;
    }

    public Lecture handleCreateLecture(Lecture lecture) {
        if (lecture.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(lecture.getUniversity().getId());
            lecture.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        if (lecture.getDepartment() != null) {
            Optional<Department> departOptional = this.departmentService.findById(lecture.getDepartment().getId());
            lecture.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
        }
        return this.lectureRepository.save(lecture);
    }

    public ResCreateLectureDTO convertToResCreateLectureDTO(Lecture lecture) {
        ResCreateLectureDTO resLecture = new ResCreateLectureDTO();
        ResCreateLectureDTO.LectureUni uni = new ResCreateLectureDTO.LectureUni();
        ResCreateLectureDTO.LectureDepart depart = new ResCreateLectureDTO.LectureDepart();

        resLecture.setId(lecture.getId());
        resLecture.setName(lecture.getName());
        resLecture.setEmail(lecture.getEmail());
        resLecture.setPhone(lecture.getPhone());
        resLecture.setCreatedAt(lecture.getCreatedAt());

        if (lecture.getUniversity() != null) {
            uni.setId(lecture.getUniversity().getId());
            uni.setName(lecture.getUniversity().getName());
            resLecture.setUniversity(uni);
        }

        if (lecture.getDepartment() != null) {
            depart.setId(lecture.getDepartment().getId());
            depart.setName(lecture.getDepartment().getName());
            resLecture.setDepartment(depart);
        }
        return resLecture;

    }

    public ResultPaginationDTO fetchAllLecture(Specification<Lecture> spec, Pageable pageable) {
        Page<Lecture> pageLecture = this.lectureRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageLecture.getTotalPages());
        mt.setTotal(pageLecture.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResLectureDTO> listLecture = pageLecture.getContent()
                .stream().map(item -> new ResLectureDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getPhone(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResLectureDTO.LectureUni(
                                item.getUniversity() != null ? item.getUniversity().getId() : 0,
                                item.getUniversity() != null ? item.getUniversity().getName() : null),
                        new ResLectureDTO.LectureDepart(
                                item.getDepartment() != null ? item.getDepartment().getId() : 0,
                                item.getDepartment() != null ? item.getDepartment().getName() : null)))
                .collect(Collectors.toList());
        rs.setResult(listLecture);
        return rs;
    }

    public Lecture handleUpdateLecture(Lecture reqLecture) {
        Optional<Lecture> lectureOptional = this.lectureRepository.findById(reqLecture.getId());
        if (lectureOptional.isPresent()) {
            Lecture currentLecture = lectureOptional.get();
            currentLecture.setName(reqLecture.getName());
            currentLecture.setEmail(reqLecture.getEmail());
            currentLecture.setPhone(reqLecture.getPhone());

            // check university
            if (reqLecture.getUniversity() != null) {
                Optional<University> uniOptional = this.universityService.findById(reqLecture.getUniversity().getId());
                reqLecture.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
                currentLecture.setUniversity(reqLecture.getUniversity());
            }

            // check department
            if (reqLecture.getDepartment() != null) {
                Optional<Department> departOptional = this.departmentService
                        .findById(reqLecture.getDepartment().getId());
                reqLecture.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
                currentLecture.setDepartment(reqLecture.getDepartment());
            }
            return this.lectureRepository.save(currentLecture);
        }
        return null;
    }

    public ResUpdateLectureDTO convertToResUpdateLectureDTO(Lecture lecture) {
        ResUpdateLectureDTO resLecture = new ResUpdateLectureDTO();
        ResUpdateLectureDTO.LectureUni uni = new ResUpdateLectureDTO.LectureUni();
        ResUpdateLectureDTO.LectureDepart depart = new ResUpdateLectureDTO.LectureDepart();

        if (lecture.getUniversity() != null) {
            uni.setId(lecture.getUniversity().getId());
            uni.setName(lecture.getUniversity().getName());
            resLecture.setUniversity(uni);
        }

        if (lecture.getDepartment() != null) {
            depart.setId(lecture.getDepartment().getId());
            depart.setName(lecture.getDepartment().getName());
            resLecture.setDepartment(depart);
        }

        resLecture.setId(lecture.getId());
        resLecture.setName(lecture.getName());
        resLecture.setEmail(lecture.getEmail());
        resLecture.setPhone(lecture.getPhone());
        resLecture.setUpdatedAt(lecture.getUpdatedAt());
        return resLecture;
    }

    public void handleDeleteLecture(long id) {
        this.lectureRepository.deleteById(id);
    }
}
