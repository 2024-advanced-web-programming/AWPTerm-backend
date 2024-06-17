package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubBasicInfoDTO;
import awpterm.backend.api.request.club.ClubStatusRequestDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubDetail;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import awpterm.backend.repository.ClubMemberRepository;
import awpterm.backend.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public Club updateBasicInfo(ClubBasicInfoDTO clubBasicInfoDTO, MultipartFile representativePicture) {
        Club club = clubRepository.findByName(clubBasicInfoDTO.getName());

        if (club == null) {
            return null;
        }
        if (representativePicture == null) {
            club.setClubDetail(ClubDetail.builder()
                    .introduction(clubBasicInfoDTO.getIntroduce())
                    .history(clubBasicInfoDTO.getHistory())
                    .representativePicture(null)
                    .regularMeetingTime(clubBasicInfoDTO.getRegularMeetingTime())
                    .build()
            );
        } else {
            club.setClubDetail(ClubDetail.builder()
                    .introduction(clubBasicInfoDTO.getIntroduce())
                    .history(clubBasicInfoDTO.getHistory())
                    .representativePicture(FileProperty.valueOf(representativePicture))
                    .regularMeetingTime(clubBasicInfoDTO.getRegularMeetingTime())
                    .build()
            );
        }

        club.setVicePresident(clubBasicInfoDTO.getVicePresident());
        club.setSecretary(clubBasicInfoDTO.getSecretary());
        club.setMembers(clubBasicInfoDTO.getMembers());

        return club;
    }
}
