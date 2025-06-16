package sideproject.gugumo.domain.dto.memberDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpEmailMemberDto {
    // 서비스 이용 약관 동의 여부
    boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    boolean isAgreeMarketing;
    private String nickname;
    private String username;
    private String password;
    private String favoriteSports;
    private String emailAuthNum;

    @Builder
    public SignUpEmailMemberDto(String nickname, String username, String password, String favoriteSports, String emailAuth, boolean isAgreeTermsUse, boolean isAgreeCollectingUsingPersonalInformation, boolean isAgreeMarketing) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.favoriteSports = favoriteSports;
        this.emailAuthNum = emailAuth;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
    }
}
