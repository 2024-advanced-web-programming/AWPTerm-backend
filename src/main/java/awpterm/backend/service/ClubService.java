package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubUpdateBasicInfoDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubDetail;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.enums.Status;
import awpterm.backend.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    public boolean updateBasicInfo(ClubUpdateBasicInfoDTO clubUpdateBasicInfoDTO, MultipartFile representativePicture, MultipartFile registerFile) {
        Club club = clubRepository.findById(clubUpdateBasicInfoDTO.getId()).orElse(null);

        if (club == null) { //찾은 게 없다면
            return false;
        }
        if (representativePicture == null) {
            club.setClubDetail(ClubDetail.builder()
                    .introduction(clubUpdateBasicInfoDTO.getIntroduce())
                    .representativePicture(null)
                    .registerFile(FileProperty.valueOf(registerFile))
                    .regularMeetingTime(LocalDateTime.parse(clubUpdateBasicInfoDTO.getRegularMeetingTime()))
                    .build()
            );
        } else {
            club.setClubDetail(ClubDetail.builder()
                    .introduction(clubUpdateBasicInfoDTO.getIntroduce())
                    .representativePicture(FileProperty.valueOf(representativePicture))
                    .registerFile(FileProperty.valueOf(registerFile))
                    .regularMeetingTime(LocalDateTime.parse(clubUpdateBasicInfoDTO.getRegularMeetingTime()))
                    .build()
            );
        }

        club.setVicePresident(clubUpdateBasicInfoDTO.getVicePresident());
        club.setSecretary(clubUpdateBasicInfoDTO.getSecretary());
        club.setMembers(clubUpdateBasicInfoDTO.getMembers());

        return true;
    }
}
