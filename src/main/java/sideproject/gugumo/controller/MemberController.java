package sideproject.gugumo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.memberDto.*;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.response.StatusCode;
import sideproject.gugumo.service.MailSenderService;
import sideproject.gugumo.service.MemberService;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailSenderService mailService;

    @PostMapping("/api/v1/login/email")
    public ResponseEntity<ApiResponse<String>> emailLogin(HttpServletResponse response, @RequestBody EmailLoginRequestDto emailLoginRequestDto) {

        String token = memberService.emailLogin(emailLoginRequestDto);
        response.addHeader("Authorization", "Bearer " + token);

        return ResponseEntity.status(LOGIN_SUCCESS.getHttpCode()).body(ApiResponse.createSuccess(LOGIN_SUCCESS));
    }

    @PostMapping("/api/v2/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Long>> joinMemberWithEmailAuth(@RequestBody @Valid SignUpEmailMemberDto signUpEmailMemberDto) {

        mailService.checkAuthNum(signUpEmailMemberDto.getUsername(), signUpEmailMemberDto.getEmailAuthNum());

        Long joinId = memberService.joinMember(signUpEmailMemberDto);

        return ResponseEntity.status(JOIN_MEMBER_WITH_EMAIL_AUTH.getHttpCode()).body(ApiResponse.createSuccess(JOIN_MEMBER_WITH_EMAIL_AUTH, joinId));
    }

    @GetMapping("/api/v1/members")
    public ResponseEntity<ApiResponse<MemberInfoDto>> getMemberInfo(@AuthenticationPrincipal CustomUserDetails principal) {

        long id = principal.getId();

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(id);

        return ResponseEntity.status(GET_MEMBER_INFO.getHttpCode()).body(ApiResponse.createSuccess(GET_MEMBER_INFO, memberInfoDto));
    }

    @PatchMapping("/api/v1/members/nicknames")
    public ResponseEntity<ApiResponse<MemberInfoDto>> updateMemberNickname(@AuthenticationPrincipal CustomUserDetails principal,
                                                           @RequestBody UpdateMemberNicknameDto updateMemberNicknameDto) {

        String updateNickname = updateMemberNicknameDto.getNickname();
        long id = principal.getId();

        // 회원 정보 수정
        memberService.updateNickname(id, updateNickname);

        // 수정한 정보로 회원 조회
        MemberInfoDto memberInfo = memberService.getMemberInfo(id);

        return ResponseEntity.status(UPDATE_NICKNAME.getHttpCode()).body(ApiResponse.createSuccess(UPDATE_NICKNAME, memberInfo));
    }

    @GetMapping("/api/v1/members/nicknames/check")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicateNickname(@RequestParam String nickname) {

        Boolean existNickname = memberService.isExistNickname(nickname);

        return ResponseEntity.status(CHECK_NICKNAME_DUPLICATE.getHttpCode()).body(ApiResponse.createSuccess(CHECK_NICKNAME_DUPLICATE, existNickname));
    }

    @PatchMapping("/api/v1/members/passwords")
    public ResponseEntity<ApiResponse<Boolean>> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails principal,
                                                     @RequestBody UpdateMemberPasswordDto updateMemberPasswordDto) {

        long id = principal.getId();

        memberService.updatePassword(id, updateMemberPasswordDto.getPassword());

        return ResponseEntity.status(UPDATE_PASSWORD.getHttpCode()).body(ApiResponse.createSuccess(UPDATE_PASSWORD));
    }

    @DeleteMapping("/api/v1/member")
    public ResponseEntity<ApiResponse<Boolean>> deleteMember(@AuthenticationPrincipal CustomUserDetails principal) {

        long id = principal.getId();

        memberService.deleteMember(id);

        return ResponseEntity.status(DELETE_MEMBER.getHttpCode()).body(ApiResponse.createSuccess(DELETE_MEMBER));
    }

    @PostMapping("/api/v1/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        String username = resetPasswordDto.getEmail();

        String newPassword = memberService.resetPassword(username);

        mailService.resetPassword(username, newPassword);

        return ResponseEntity.status(RESET_PASSWORD.getHttpCode()).body(ApiResponse.createSuccess(RESET_PASSWORD));
    }
}
