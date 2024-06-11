package awpterm.backend.service;

import awpterm.backend.domain.ClubMaster;
import awpterm.backend.repository.ClubMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubMasterService {
    private final ClubMasterRepository clubMasterRepository;

    public void save(ClubMaster clubMaster) {
        clubMasterRepository.save(clubMaster);
    }
}
