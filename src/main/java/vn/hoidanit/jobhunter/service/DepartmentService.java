package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.department.ResCreateDepartDTO;
import vn.hoidanit.jobhunter.domain.response.department.ResDepartDTO;
import vn.hoidanit.jobhunter.domain.response.department.ResUpdateDepartDTO;
import vn.hoidanit.jobhunter.repository.DepartmentRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final UniversityService universityService;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository,
            UniversityService universityService) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.universityService = universityService;
    }

    public Department handleCreateDepart(Department depart) {
        if (depart.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(depart.getUniversity().getId());
            depart.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        return this.departmentRepository.save(depart);
    }

    public ResultPaginationDTO fetchAllDepart(Specification<Department> spec, Pageable pageable) {
        Page<Department> pageDepart = this.departmentRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageDepart.getTotalPages());
        mt.setTotal(pageDepart.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResDepartDTO> listDepart = pageDepart.getContent()
                .stream().map(item -> new ResDepartDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResDepartDTO.UniDepart(
                                item.getUniversity() != null ? item.getUniversity().getId() : 0,
                                item.getUniversity() != null ? item.getUniversity().getName() : null)))
                .collect(Collectors.toList());
        rs.setResult(listDepart);
        return rs;
    }

    public Department handleUpdateDepart(Department reqDepart) {
        Optional<Department> departOptional = this.departmentRepository.findById(reqDepart.getId());
        if (departOptional.isPresent()) {
            Department currentDepart = departOptional.get();
            currentDepart.setName(reqDepart.getName());
            currentDepart.setDescription(reqDepart.getDescription());
            // check university
            if (reqDepart.getUniversity() != null) {
                Optional<University> uniOptional = this.universityService.findById(reqDepart.getUniversity().getId());
                reqDepart.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
                currentDepart.setUniversity(reqDepart.getUniversity());
            }
            return this.departmentRepository.save(currentDepart);
        }
        return null;
    }

    public void handleDeleteDepart(long id) {
        Optional<Department> departOptional = this.departmentRepository.findById(id);
        if (departOptional.isPresent()) {
            Department depart = departOptional.get();

            List<User> users = this.userRepository.findByDepartment(depart);
            this.userRepository.deleteAll(users);
        }
        this.departmentRepository.deleteById(id);
    }

    public Optional<Department> findById(long id) {
        return this.departmentRepository.findById(id);
    }

    public ResCreateDepartDTO convertToResCreateDepartDTO(Department department) {
        ResCreateDepartDTO res = new ResCreateDepartDTO();
        ResCreateDepartDTO.UniDepart uni = new ResCreateDepartDTO.UniDepart();

        if (department.getUniversity() != null) {
            uni.setId(department.getUniversity().getId());
            uni.setName(department.getUniversity().getName());
            res.setUniversity(uni);
        }
        res.setId(department.getId());
        res.setName(department.getName());
        res.setDescription(department.getDescription());
        res.setCreatedAt(department.getCreatedAt());

        return res;
    }

    public ResUpdateDepartDTO convertToResUpdateDepartDTO(Department department) {
        ResUpdateDepartDTO res = new ResUpdateDepartDTO();
        ResUpdateDepartDTO.UniDepart uni = new ResUpdateDepartDTO.UniDepart();

        if (department.getUniversity() != null) {
            uni.setId(department.getUniversity().getId());
            uni.setName(department.getUniversity().getName());
            res.setUniversity(uni);
        }
        res.setId(department.getId());
        res.setName(department.getName());
        res.setDescription(department.getDescription());
        res.setUpdatedAt(department.getUpdatedAt());

        return res;
    }
}
