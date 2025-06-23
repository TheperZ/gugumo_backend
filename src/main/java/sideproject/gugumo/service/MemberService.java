package sideproject.gugumo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.dto.memberDto.LoginCreateJwtDto;
import sideproject.gugumo.domain.dto.memberDto.MemberInfoDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpEmailMemberDto;
import sideproject.gugumo.domain.dto.memberDto.SignUpKakaoMemberDto;
import sideproject.gugumo.domain.entity.member.FavoriteSport;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.member.MemberStatus;
import sideproject.gugumo.exception.exception.DuplicateResourceException;
import sideproject.gugumo.exception.exception.NotFoundException;
import sideproject.gugumo.jwt.JwtUtil;
import sideproject.gugumo.repository.FavoriteSportRepository;
import sideproject.gugumo.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static sideproject.gugumo.response.StatusCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteSportRepository favoriteSportRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long joinMember(SignUpEmailMemberDto signUpEmailMemberDto) {

        String encodePassword = passwordEncoder.encode(signUpEmailMemberDto.getPassword());

        Member joinMember = Member.emailJoin()
                .username(signUpEmailMemberDto.getUsername())
                .nickname(signUpEmailMemberDto.getNickname())
                .password(encodePassword)
                .isAgreeMarketing(signUpEmailMemberDto.isAgreeMarketing())
                .isAgreeCollectingUsingPersonalInformation(signUpEmailMemberDto.isAgreeCollectingUsingPersonalInformation())
                .isAgreeTermsUse(signUpEmailMemberDto.isAgreeTermsUse())
                .build();

        validateDuplicateMemberByUsername(joinMember.getUsername());
        validateDuplicateMemberByNickname(joinMember.getNickname());

        String favoriteSports = signUpEmailMemberDto.getFavoriteSports();

        if (!favoriteSports.isEmpty()) {
            saveFavoriteSports(favoriteSports, joinMember);
        }

        memberRepository.save(joinMember);

        return joinMember.getId();
    }

    @Transactional
    public void kakaoJoinMember(SignUpKakaoMemberDto signUpKakaoMemberDto) {

        Member joinMember = Member.kakaoJoin()
                .kakaoId(signUpKakaoMemberDto.getKakaoId())
                .username(signUpKakaoMemberDto.getUsername())
                .nickname(signUpKakaoMemberDto.getNickname())
                .isAgreeMarketing(signUpKakaoMemberDto.isAgreeMarketing())
                .isAgreeTermsUse(signUpKakaoMemberDto.isAgreeTermsUse())
                .isAgreeCollectingUsingPersonalInformation(signUpKakaoMemberDto.isAgreeCollectingUsingPersonalInformation())
                .build();

        validateDuplicateMemberByUsername(joinMember.getUsername());
        validateDuplicateMemberByNickname(joinMember.getNickname());

        String favoriteSports = signUpKakaoMemberDto.getFavoriteSports();

        if (!favoriteSports.isEmpty()) {
            saveFavoriteSports(favoriteSports, joinMember);
        }

        memberRepository.save(joinMember);
    }

    public void saveFavoriteSports(String favoriteSports, Member joinMember) {
        String[] split = favoriteSports.split(",");
        for (String str : split) {
            FavoriteSport favoriteSport = FavoriteSport.createFavoriteSport(str, joinMember);
            favoriteSportRepository.save(favoriteSport);
        }
    }

    public MemberInfoDto getMemberInfo(Long id) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        List<FavoriteSport> favoriteSportList = favoriteSportRepository.getFavoriteSports(findMember);

        String favoriteSports = getFavoritesToString(favoriteSportList);

        return MemberInfoDto.builder()
                .username(findMember.getUsername())
                .nickname(findMember.getNickname())
                .favoriteSports(favoriteSports)
                .build();

    }

    public String getFavoritesToString(List<FavoriteSport> favoriteSportList) {

        StringBuilder favoriteSports = new StringBuilder();

        if (!favoriteSportList.isEmpty()) {
            for (FavoriteSport fs : favoriteSportList) {
                favoriteSports.append(fs.getGameType().name());
                favoriteSports.append(',');
            }
            favoriteSports.deleteCharAt(favoriteSports.length() - 1);
        }

        return favoriteSports.toString();
    }

    public Boolean isExistNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        return findMember.isPresent();
    }

    @Transactional
    public void updateNickname(Long id, String nickname) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        validateDuplicateMemberByNickname(nickname);

        findMember.updateMemberNickname(nickname);
    }

    private void validateDuplicateMemberByUsername(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isPresent()) {
            throw new DuplicateResourceException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateDuplicateMemberByNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        if (findMember.isPresent()) {
            throw new DuplicateResourceException(NICKNAME_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void updatePassword(Long id, String password) {

        memberRepository.findById(id)
                .ifPresent(member -> member.updateMemberPassword(passwordEncoder.encode(password)));
    }

    @Transactional
    public String resetPassword(String username) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        String newPassword = RandomStringUtils.randomAlphanumeric(10);

        findMember.updateMemberPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }

    @Transactional
    public void deleteMember(Long id) {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        if (findMember.getStatus() == MemberStatus.delete) {
            throw new NotFoundException(MEMBER_NOT_FOUND);
        }

        findMember.deleteMember();

    }

    // TODO USERNAME 수정(JWT 토큰 발급할 때 clame에 username 속성 빼기)
    public String kakaoLogin(String username) {

        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        LoginCreateJwtDto loginDto = LoginCreateJwtDto.builder()
                .id(findMember.getId())
                .username("username")
                .role(findMember.getRole().name())
                .requestTimeMs(LocalDateTime.now())
                .build();

        return jwtUtil.createJwt(loginDto);
    }
}
