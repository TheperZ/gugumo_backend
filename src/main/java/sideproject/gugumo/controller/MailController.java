package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.emailDto.EmailCheckDto;
import sideproject.gugumo.domain.dto.emailDto.EmailRequestDto;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.MailSenderService;

import static sideproject.gugumo.response.StatusCode.*;

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
    public ResponseEntity<ApiResponse<String>> mailSend(@RequestBody @Valid EmailRequestDto emailDto) {

        mailService.joinEmail(emailDto.getEmail());

        return ResponseEntity.status(EMAIL_SEND.getHttpCode()).body(ApiResponse.createSuccess(EMAIL_SEND));
    }

    /**
     * 코드에 대해 검증 행위 수행
     * @param emailCheckDto
     * @return
     */
    @PostMapping("/api/v1/email-verifications/verify")
    public ResponseEntity<ApiResponse<String>> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {

        mailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getEmailAuthNum());

        return ResponseEntity.status(EMAIL_VERIFY.getHttpCode()).body(ApiResponse.createSuccess(EMAIL_VERIFY));
    }

}
