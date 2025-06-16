package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.emailDto.EmailCheckDto;
import sideproject.gugumo.domain.dto.emailDto.EmailRequestDto;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MailSenderService;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailSenderService mailService;

    /**
     * '이메일 인증요청' 자원을 생성하는 행위로 해석
     * @param emailDto
     * @return
     */
    @PostMapping("/api/v1/email-verifications")
    public ApiResponse<String> mailSend(@RequestBody @Valid EmailRequestDto emailDto) {

        mailService.joinEmail(emailDto.getEmail());

        return ApiResponse.createSuccess("인증번호 전송 완료.");
    }

    /**
     * 코드에 대해 검증 행위 수행
     * @param emailCheckDto
     * @return
     */
    @PostMapping("/api/v1/email-verifications/verify")
    public ApiResponse<String> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {

        mailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getEmailAuthNum());

        return ApiResponse.createSuccess("인증 완료.");
    }
}
