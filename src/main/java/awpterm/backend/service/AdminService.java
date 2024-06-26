package awpterm.backend.service;

import awpterm.backend.domain.Admin;
import awpterm.backend.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    //시스템 관리자의 등록 승인/거절 및 상태 확인 구현 -> 동아리 등록에 대한 승인, 거절
    public boolean isAdmin(String adminId) { //admin인지 확인하기 위해서
        return adminRepository.findById(adminId).orElse(null) != null;
    }

    public Admin findById(String adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//        adminRepository.save(Admin.builder()
//                .id("admin")
//                .name("admin")
//                .password(SHA256.encrypt("1234"))
//                .build());
//    }
}
