package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.memberDto.*;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MailSenderService;
import sideproject.gugumo.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailSenderService mailService;

    @PostMapping("/api/v1/login/email")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> emailLogin(@RequestBody EmailLoginRequestDto emailLoginRequestDto) {

        String token = memberService.emailLogin(emailLoginRequestDto);

        return ApiResponse.createSuccess("Bearer " + token);
    }

    @PostMapping("/api/v2/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Boolean> joinMemberWithEmailAuth(@RequestBody @Valid SignUpEmailMemberDto signUpEmailMemberDto) {

        mailService.checkAuthNum(signUpEmailMemberDto.getUsername(), signUpEmailMemberDto.getEmailAuthNum());

        Long joinId = memberService.joinMember(signUpEmailMemberDto);

        return ApiResponse.createSuccess(true);
    }

    @GetMapping("/api/v1/members")
    public ApiResponse<MemberInfoDto> getMemberInfo(@AuthenticationPrincipal CustomUserDetails principal) {

        long id = principal.getId();

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(id);

        return ApiResponse.createSuccess(memberInfoDto);
    }

    @PatchMapping("/api/v1/members/nicknames")
    public ApiResponse<MemberInfoDto> updateMemberNickname(@AuthenticationPrincipal CustomUserDetails principal,
                                                           @RequestBody UpdateMemberNicknameDto updateMemberNicknameDto) {

        String updateNickname = updateMemberNicknameDto.getNickname();
        long id = principal.getId();

        // 회원 정보 수정
        memberService.updateNickname(id, updateNickname);

        // 수정한 정보로 회원 조회
        MemberInfoDto memberInfo = memberService.getMemberInfo(id);

        return ApiResponse.createSuccess(memberInfo);
    }

    @GetMapping("/api/v1/members/nicknames/check")
    public ApiResponse<Boolean> checkDuplicateNickname(@RequestParam String nickname) {

        Boolean isExistNickname = memberService.isExistNickname(nickname);

        return ApiResponse.createSuccess(isExistNickname);
    }

    @PatchMapping("/api/v1/members/passwords")
    public ApiResponse<Boolean> updateMemberPassword(@AuthenticationPrincipal CustomUserDetails principal,
                                                     @RequestBody UpdateMemberPasswordDto updateMemberPasswordDto) {

        long id = principal.getId();

        memberService.updatePassword(id, updateMemberPasswordDto.getPassword());

        return ApiResponse.createSuccess(true);
    }

    @DeleteMapping("/api/v1/member")
    public ApiResponse<Boolean> deleteMember(@AuthenticationPrincipal CustomUserDetails principal) {

        long id = principal.getId();

        memberService.deleteMember(id);

        return ApiResponse.createSuccess(true);
    }

    @PostMapping("/api/v1/reset-password")
    public ApiResponse<Boolean> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        String username = resetPasswordDto.getEmail();

        String newPassword = memberService.resetPassword(username);

        mailService.resetPassword(username, newPassword);

        return ApiResponse.createSuccess(true);
    }
}
