package sideproject.gugumo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.customnotidto.CustomNotiDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.response.StatusCode;
import sideproject.gugumo.service.FcmNotificationService;

import java.util.List;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FcmNotificationController {


    private final FcmNotificationService fcmNotificationService;


    @GetMapping("/notifications")
    public <T extends CustomNotiDto> ResponseEntity<ApiResponse<List<T>>> findNoti(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(FIND_NOTIFICATION.getHttpCode()).body(ApiResponse.createSuccess(FIND_NOTIFICATION, fcmNotificationService.findNotification(principal)));
    }

    @PatchMapping("/notifications/{noti_id}")
    public ResponseEntity<ApiResponse<String>> read(@AuthenticationPrincipal CustomUserDetails principal,
                                    @PathVariable("noti_id") Long id) {

        fcmNotificationService.read(principal, id);
        return ResponseEntity.status(READ_NOTIFICATION.getHttpCode()).body(ApiResponse.createSuccess(READ_NOTIFICATION));
    }

    @PatchMapping("/notifications")
    public ResponseEntity<ApiResponse<String>> readAll(@AuthenticationPrincipal CustomUserDetails principal) {

        fcmNotificationService.readAll(principal);
        return ResponseEntity.status(READ_ALL_NOTIFICATION.getHttpCode()).body(ApiResponse.createSuccess(READ_ALL_NOTIFICATION));
    }

    @DeleteMapping("/notifications/{noti_id}")
    public ResponseEntity<ApiResponse<String>> deleteNoti(@AuthenticationPrincipal CustomUserDetails principal,
                                          @PathVariable("noti_id") Long id) {
        fcmNotificationService.deleteNotification(principal, id);

        return ResponseEntity.status(DELETE_NOTIFICATION.getHttpCode()).body(ApiResponse.createSuccess(DELETE_NOTIFICATION));
    }


    @DeleteMapping("/notifications/read")
    public ResponseEntity<ApiResponse<String>> deleteReadNoti(@AuthenticationPrincipal CustomUserDetails principal) {
        fcmNotificationService.deleteReadNotification(principal);

        return ResponseEntity.status(DELETE_READ_NOTIFICATION.getHttpCode()).body(ApiResponse.createSuccess(DELETE_READ_NOTIFICATION));
    }

}
