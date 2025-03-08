package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.hoidanit.jobhunter.domain.University;

public interface UniversityRepository extends JpaRepository<University, Long>, JpaSpecificationExecutor<University> {
    List<University> findAll();
}
