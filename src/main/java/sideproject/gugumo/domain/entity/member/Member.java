package sideproject.gugumo.domain.entity.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    // 공통 속성
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String nickname;
    private String profileImagePath;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private MemberRole role;
    // 서비스 이용 약관 동의 여부
    private Boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    private Boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    private Boolean isAgreeMarketing;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<FavoriteSport> favoriteSports = new ArrayList<>();

    // email 속성
    private Boolean isEmailLogin;
    private String username;
    private String password;

    // kakao 속성
    private Boolean isKakaoLogin;
    private Long kakaoId;
    private String kakaoNickname;


    public Member(Long id, String nickname, String profileImagePath, MemberStatus status, MemberRole role, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing, List<FavoriteSport> favoriteSports, Boolean isEmailLogin, String username, String password, Boolean isKakaoLogin, Long kakaoId, String kakaoNickname) {
        this.id = id;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.status = status;
        this.role = role;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
        this.favoriteSports = favoriteSports;
        this.isEmailLogin = isEmailLogin;
        this.username = username;
        this.password = password;
        this.isKakaoLogin = isKakaoLogin;
        this.kakaoId = kakaoId;
        this.kakaoNickname = kakaoNickname;
    }

    @Builder(builderClassName = "UserLogin", builderMethodName = "userLogin")
    public Member(Long id, String username, MemberRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    @Builder(builderClassName = "EmailJoin", builderMethodName = "emailJoin")
    public Member(Long id, String username, String password, String nickname, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
        this.role = MemberRole.ROLE_USER;
        this.status = MemberStatus.active;
        this.isEmailLogin = true;
    }

    @Builder(builderClassName = "KakaoJoin", builderMethodName = "kakaoJoin")
    public Member(Long id, String username, String nickname, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing, Long kakaoId, String kakaoNickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
        this.kakaoId = kakaoId;
        this.kakaoNickname = kakaoNickname;
        this.role = MemberRole.ROLE_USER;
        this.status = MemberStatus.active;
        this.isKakaoLogin = true;
    }

    public void updateMemberNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateMemberPassword(String password) {
        this.password = password;
    }

    public void deleteMember() {

        this.status = MemberStatus.delete;

        this.nickname = null;
        this.profileImagePath = null;
        this.role = null;
        this.isAgreeTermsUse = null;
        this.isAgreeCollectingUsingPersonalInformation = null;
        this.isAgreeMarketing = null;
        this.favoriteSports = null;
        this.isEmailLogin = null;
        this.username = null;
        this.password = null;
        this.isKakaoLogin = null;
        this.kakaoId = null;
        this.kakaoNickname = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(username, member.username) && Objects.equals(password, member.password) && Objects.equals(nickname, member.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, nickname);
    }
}