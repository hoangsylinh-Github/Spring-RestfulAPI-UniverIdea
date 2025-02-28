package vn.hoidanit.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.University;
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

    public List<University> fetchAllUni() {
        return this.universityRepository.findAll();
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
