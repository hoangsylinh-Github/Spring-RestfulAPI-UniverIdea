package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.jobhunter.domain.University;

public interface UniversityRepository extends JpaRepository<University, Long> {
    List<University> findAll();
}
