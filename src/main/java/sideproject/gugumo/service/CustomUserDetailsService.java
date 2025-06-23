package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.NotFoundException;
import sideproject.gugumo.repository.MemberRepository;

import static sideproject.gugumo.response.StatusCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        if (findMember.getStatus() == MemberStatus.delete) {
            throw new NotFoundException(MEMBER_NOT_FOUND);
        }

        return CustomUserDetails.builder()
                .id(findMember.getId())
                .username(findMember.getUsername())
                .role(findMember.getRole())
                .password(findMember.getPassword())
                .build();
    }
}