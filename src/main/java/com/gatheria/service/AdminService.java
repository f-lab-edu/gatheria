package com.gatheria.service;

import com.gatheria.common.config.PasswordConfig.PasswordEncoder;
import com.gatheria.domain.Admin;
import com.gatheria.domain.type.AdminRole;
import com.gatheria.dto.request.AdminRegisterRequestDto;
import com.gatheria.dto.response.AdminRegisterResponseDto;
import com.gatheria.mapper.AdminMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  private final AdminMapper adminMapper;
  private final PasswordEncoder passwordEncoder;

  public AdminService(AdminMapper adminMapper, PasswordEncoder passwordEncoder) {
    this.adminMapper = adminMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public AdminRegisterResponseDto register(AdminRegisterRequestDto request) {
    if (adminMapper.existsByUsername(request.getUsername())) {
      throw new RuntimeException("이미 존재하는 관리자 계정입니다.");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());
    Admin newAdmin = new Admin(null, request.getUsername(), encodedPassword, AdminRole.SUPER_ADMIN);

    adminMapper.insertAdmin(newAdmin);

    return AdminRegisterResponseDto.from(newAdmin);
  }

  public boolean login(String username, String password) {
    Admin admin = adminMapper.findByUsername(username);
    return admin != null && passwordEncoder.matches(password, admin.getPassword());
  }
}
