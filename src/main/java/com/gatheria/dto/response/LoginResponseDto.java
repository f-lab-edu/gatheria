package com.gatheria.dto.response;

import com.gatheria.domain.Member;
import com.gatheria.domain.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

  private String accessToken;
  private MemberRole role;
  private String affiliation;
  private String email;
  private String name;
  private boolean activate;
  private String phone;
  private Long memberId;

  public static LoginResponseDto from(String accessToken, Member member) {
    return new LoginResponseDto(
        accessToken,
        member.getRole(),
        member.getAffiliation(),
        member.getEmail(),
        member.getName(),
        member.isActive(),
        member.getPhone(),
        member.getId()
    );
  }
}
