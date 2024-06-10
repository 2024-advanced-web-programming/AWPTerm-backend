package awpterm.backend.service;

import awpterm.backend.api.request.admin.AdminLoginRequestDTO;
import awpterm.backend.domain.Admin;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceFacade {
    private final AdminService adminService;
    private final ClubService clubService;

    public boolean isAdmin(String adminId) { //admin인지 확인하기 위해서
        return adminService.isAdmin(adminId);
    }

    public Admin findById(String adminId) {
        return adminService.findById(adminId);
    }

    public boolean isValidLoginRequest(AdminLoginRequestDTO adminLoginRequestDTO) {
        Admin admin = adminLoginRequestDTO.toEntity();
        Admin findAdmin = adminService.findById(admin.getId());
        return findAdmin != null && findAdmin.isSame(admin);
    }
    public List<Club> findByStatus(Status status) {
        return clubService.findByStatus(status);
    }
}
