package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubStatusRequestDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import awpterm.backend.repository.ClubMemberRepository;
import awpterm.backend.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {
    private final ClubRepository clubRepository;

    public Club register(Club club) {
        clubRepository.save(club);
        return clubRepository.findById(club.getId()).orElse(null);
    }

    public Club findById(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    public List<Club> findByStatus(Status status) {
        return clubRepository.findByStatus(status);
    }
    public boolean updateStatus(Long id, String status) {
        Club club = clubRepository.findById(id).orElse(null);
        if(club == null) { //club을 찾을 수 없는 경우
            return false;
        }
        club.setStatus(Status.valueOf(status));
        return true;
    }
}
