package awpterm.backend.service;

import awpterm.backend.domain.Club;
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

    public List<Club> register(Club club) {
        clubRepository.save(club);
        return clubRepository.findAll();
    }
}
