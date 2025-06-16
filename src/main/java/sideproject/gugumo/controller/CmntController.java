package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.request.CreateCmntReq;
import sideproject.gugumo.request.UpdateCmntReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.CmntService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CmntController {

    private final CmntService cmntService;

    @PostMapping("/api/v1/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> saveComment(@AuthenticationPrincipal CustomUserDetails principal,
                                           @Valid @RequestBody CreateCmntReq req) {
        cmntService.save(req, principal);

        return ApiResponse.createSuccess("댓글 저장 완료");

    }

    @GetMapping("/api/v1/posts/{post_id}/comments")
    public ApiResponse<List<CmntDto>> findComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                  @PathVariable("post_id") Long postId) {

        return ApiResponse.createSuccess(cmntService.findComment(postId, principal));
    }

    @PatchMapping("/api/v1/comments/{comment_id}")
    public ApiResponse<String> updateComment(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("comment_id") Long commentId,
                                             @RequestBody UpdateCmntReq req) {

        cmntService.updateComment(commentId, req, principal);

        return ApiResponse.createSuccess("댓글 갱신 완료");

    }

    @DeleteMapping("/api/v1/comments/{comment_id}")
    public ApiResponse<String> deleteComment(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("comment_id") Long commentId) {
        cmntService.deleteComment(commentId, principal);

        return ApiResponse.createSuccess("댓글 삭제 완료");

    }
}
