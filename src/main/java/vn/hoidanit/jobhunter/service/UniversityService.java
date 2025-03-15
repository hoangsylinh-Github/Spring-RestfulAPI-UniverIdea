package vn.hoidanit.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UniversityRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;

    public UniversityService(UniversityRepository universityRepository, UserRepository userRepository) {
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
    }

    public University handleCreateUni(University uni) {
        return this.universityRepository.save(uni);
    }

    public ResultPaginationDTO fetchAllUni(Specification<University> spec, Pageable pageable) {
        Page<University> pageUni = this.universityRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUni.getTotalPages());
        mt.setTotal(pageUni.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUni.getContent());

        return rs;
    }

    public University handleUpdateUni(University reqUni) {
        Optional<University> uniOptional = this.universityRepository.findById(reqUni.getId());
        if (uniOptional.isPresent()) {
            University currentUni = uniOptional.get();
            currentUni.setName(reqUni.getName());
            currentUni.setDescription(reqUni.getDescription());
            currentUni.setLogo(reqUni.getLogo());
            return this.universityRepository.save(currentUni);
        }
        return null;
    }

    public void handleDeleteUni(long id) {
        Optional<University> uniOptional = this.universityRepository.findById(id);
        if (uniOptional.isPresent()) {
            University uni = uniOptional.get();

            List<User> users = this.userRepository.findByUniversity(uni);
            this.userRepository.deleteAll(users);
        }
        this.universityRepository.deleteById(id);
    }

    public Optional<University> findById(long id) {
        return this.universityRepository.findById(id);
    }
}
