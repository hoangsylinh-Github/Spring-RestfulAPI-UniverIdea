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
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UniversityService universityService;
    private final DepartmentService departmentService;

    public UserService(UserRepository userRepository, UniversityService universityService,
            DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.universityService = universityService;
        this.departmentService = departmentService;
    }

    public User handleCreateUser(User user) {
        // check university
        if (user.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(user.getUniversity().getId());
            user.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        // check department
        if (user.getDepartment() != null) {
            Optional<Department> departOptional = this.departmentService.findById(user.getDepartment().getId());
            user.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
        }
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getPhone(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        new ResUserDTO.UniversityUser(
                                item.getUniversity() != null ? item.getUniversity().getId() : 0,
                                item.getUniversity() != null ? item.getUniversity().getName() : null),
                        new ResUserDTO.DepartmentUser(
                                item.getDepartment() != null ? item.getDepartment().getId() : 0,
                                item.getDepartment() != null ? item.getDepartment().getName() : null)))
                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setPhone(reqUser.getPhone());

            // check university
            if (reqUser.getUniversity() != null) {
                Optional<University> uniOptional = this.universityService.findById(reqUser.getUniversity().getId());
                reqUser.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
                currentUser.setUniversity(reqUser.getUniversity());
            }

            // check department
            if (reqUser.getDepartment() != null) {
                Optional<Department> departOptional = this.departmentService.findById(reqUser.getDepartment().getId());
                reqUser.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
                currentUser.setDepartment(reqUser.getDepartment());
            }

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resUser = new ResCreateUserDTO();
        ResCreateUserDTO.UniversityUser uni = new ResCreateUserDTO.UniversityUser();
        ResCreateUserDTO.DepartmentUser depart = new ResCreateUserDTO.DepartmentUser();

        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setEmail(user.getEmail());
        resUser.setAddress(user.getAddress());
        resUser.setPhone(user.getPhone());
        resUser.setAge(user.getAge());
        resUser.setCreatedAt(user.getCreatedAt());
        resUser.setGender(user.getGender());

        if (user.getUniversity() != null) {
            uni.setId(user.getUniversity().getId());
            uni.setName(user.getUniversity().getName());
            resUser.setUniversity(uni);
        }

        if (user.getDepartment() != null) {
            depart.setId(user.getDepartment().getId());
            depart.setName(user.getDepartment().getName());
            resUser.setDepartment(depart);
        }

        return resUser;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.UniversityUser uni = new ResUserDTO.UniversityUser();
        ResUserDTO.DepartmentUser depart = new ResUserDTO.DepartmentUser();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setPhone(user.getPhone());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());

        if (user.getUniversity() != null) {
            uni.setId(user.getUniversity().getId());
            uni.setName(user.getUniversity().getName());
            res.setUniversity(uni);
        }

        if (user.getDepartment() != null) {
            depart.setId(user.getDepartment().getId());
            depart.setName(user.getDepartment().getName());
            res.setDepartment(depart);
        }
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUser = new ResUpdateUserDTO();
        ResUpdateUserDTO.UniversityUser uni = new ResUpdateUserDTO.UniversityUser();
        ResUpdateUserDTO.DepartmentUser depart = new ResUpdateUserDTO.DepartmentUser();
        if (user.getUniversity() != null) {
            uni.setId(user.getUniversity().getId());
            uni.setName(user.getUniversity().getName());
            resUser.setUniversity(uni);
        }

        if (user.getDepartment() != null) {
            depart.setId(user.getDepartment().getId());
            depart.setName(user.getDepartment().getName());
            resUser.setDepartment(depart);
        }
        resUser.setId(user.getId());
        resUser.setName(user.getName());
        resUser.setAddress(user.getAddress());
        resUser.setPhone(user.getPhone());
        resUser.setAge(user.getAge());
        resUser.setUpdatedAt(user.getUpdatedAt());
        resUser.setGender(user.getGender());
        return resUser;
    }

    public void updateUserToken(String token, String email) {
        User currenUser = this.handleGetUserByUsername(email);
        if (currenUser != null) {
            currenUser.setRefreshToken(token);
            this.userRepository.save(currenUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
