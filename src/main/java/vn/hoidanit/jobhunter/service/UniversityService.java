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
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UniversityRepository;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public University handleCreateUni(University uni) {
        return this.universityRepository.save(uni);
    }

    public ResultPaginationDTO fetchAllUni(Specification<University> spec, Pageable pageable) {
        Page<University> pageUni = this.universityRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

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
        this.universityRepository.deleteById(id);
    }
}
