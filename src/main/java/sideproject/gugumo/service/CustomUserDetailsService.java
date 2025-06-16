package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.memberDto.CustomUserInfoDto;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {


        Member findMember = memberRepository.findOne(Long.parseLong(id)).orElseThrow(() -> new UserNotFoundException("회원이 없습니다."));

        if (findMember.getStatus() == MemberStatus.delete) {
            throw new UserNotFoundException("탈퇴한 회원입니다.");
        }

        CustomUserInfoDto customUserInfoDto = CustomUserInfoDto.builder()
                .id(findMember.getId())
                .username(findMember.getUsername())
                .role(findMember.getRole())
                .password(findMember.getPassword())
                .build();

        return new CustomUserDetails(customUserInfoDto);
    }
}