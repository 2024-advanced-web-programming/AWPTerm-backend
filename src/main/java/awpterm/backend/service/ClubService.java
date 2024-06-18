package awpterm.backend.service;

import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
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

    public ClubResponseDTO register(Club club) {
        clubRepository.save(club);
        return ClubResponseDTO.valueOf(clubRepository.findById(club.getId()).orElse(null));
    }

    public Club findById(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    public List<Club> findByStatus(Status status) {
        return clubRepository.findByStatus(status);
    }

    public boolean updateStatus(Long id, String status, String rejectReason) {
        Club club = clubRepository.findById(id).orElse(null);
        if (club == null) { //club을 찾을 수 없는 경우
            return false;
        }
        club.setStatus(Status.valueOf(status));
        club.setRejectReason(rejectReason);

        return true;
    }

    public List<ClubResponseDTO> findAll() {
        return clubRepository.findAll().stream().map(ClubResponseDTO::valueOf).toList();
    }

    public void updateBasicInfo(Club club) {
        clubRepository.save(club);
    }

    public List<ClubResponseDTO> findClubByCreatedBy(Member member) {
        return clubRepository.findByCreatedBy(member).stream().map(ClubResponseDTO::valueOf).toList();
    }

    public List<ClubResponseDTO> findClubByPresident(Member member) {
        return clubRepository.findByPresident(member).stream().map(ClubResponseDTO::valueOf).toList();
    }

    public Club save(Club club) {
        return clubRepository.save(club);
    }
}
