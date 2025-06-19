package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.request.FcmTokenDto;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.response.StatusCode;
import sideproject.gugumo.service.FcmNotificationTokenService;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FcmNotificationTokenController {

    private final FcmNotificationTokenService fcmNotificationTokenService;

    @PostMapping("/fcm-tokens")
    public ResponseEntity<ApiResponse<String>> subscribe(@AuthenticationPrincipal CustomUserDetails principal,
                                                        @Valid @RequestBody FcmTokenDto fcmTokenDto) {

        fcmNotificationTokenService.subscribe(principal, fcmTokenDto);

        return ResponseEntity.status(CREATE_FCM_TOKEN.getHttpCode()).body(ApiResponse.createSuccess(CREATE_FCM_TOKEN));
    }
}
