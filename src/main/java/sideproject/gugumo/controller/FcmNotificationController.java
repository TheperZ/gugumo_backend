package sideproject.gugumo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.customnotidto.CustomNotiDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.FcmNotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FcmNotificationController {


    private final FcmNotificationService fcmNotificationService;


    //몇개?
    @GetMapping("/notification")
    public <T extends CustomNotiDto> ApiResponse<List<T>> findNoti(@AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.createSuccess(fcmNotificationService.findNotification(principal));
    }

    @PatchMapping("/notification/read/{noti_id}")
    public ApiResponse<String> read(@AuthenticationPrincipal CustomUserDetails principal,
                                    @PathVariable("noti_id") Long id) {

        fcmNotificationService.read(principal, id);
        return ApiResponse.createSuccess("알림 읽음처리 완료");
    }

    @PatchMapping("/notification/read")
    public ApiResponse<String> readAll(@AuthenticationPrincipal CustomUserDetails principal) {

        fcmNotificationService.readAll(principal);
        return ApiResponse.createSuccess("알림 모두 읽음처리 완료");
    }

    @DeleteMapping("/notification/{noti_id}")
    public ApiResponse<String> deleteNoti(@AuthenticationPrincipal CustomUserDetails principal,
                                          @PathVariable("noti_id") Long id) {
        fcmNotificationService.deleteNotification(principal, id);

        return ApiResponse.createSuccess("알림 삭제 완료");
    }


    @DeleteMapping("/notification/read")
    public ApiResponse<String> deleteReadNoti(@AuthenticationPrincipal CustomUserDetails principal) {
        fcmNotificationService.deleteReadNotification(principal);

        return ApiResponse.createSuccess("읽은 알림 삭제 완료");
    }
}
