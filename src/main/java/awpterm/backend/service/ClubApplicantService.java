package awpterm.backend.service;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubApplicant;
import awpterm.backend.domain.Member;
import awpterm.backend.repository.ClubApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubApplicantService {
    private final ClubApplicantRepository clubApplicantRepository;

    public ClubApplicant findByClubAndApplicant(Club club, Member applicant) {
        return clubApplicantRepository.findByClubAndApplicant(club, applicant);
    }

    public void save(ClubApplicant clubApplicant) {
        clubApplicantRepository.save(clubApplicant);
    }

    public void delete(ClubApplicant clubApplicant) {
        clubApplicantRepository.delete(clubApplicant);
    }
}
