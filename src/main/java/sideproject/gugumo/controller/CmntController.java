package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.request.CreateCmntReq;
import sideproject.gugumo.request.UpdateCmntReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.CmntService;

import java.util.List;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class CmntController {

    private final CmntService cmntService;

    @PostMapping("/api/v1/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> saveComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                           @Valid @RequestBody CreateCmntReq req) {
        cmntService.save(req, principal);

        return ResponseEntity.status(CREATE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(CREATE_COMMENT));

    }

    @GetMapping("/api/v1/posts/{post_id}/comments")
    public ResponseEntity<ApiResponse<List<CmntDto>>> findComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                                  @PathVariable("post_id") Long postId) {

        return ResponseEntity.status(FIND_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(FIND_COMMENT, cmntService.findComment(postId, principal)));
    }

    @PatchMapping("/api/v1/comments/{comment_id}")
    public ResponseEntity<ApiResponse<String>> updateComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                             @PathVariable("comment_id") Long commentId,
                                                             @RequestBody UpdateCmntReq req) {

        cmntService.updateComment(commentId, req, principal);

        return ResponseEntity.status(UPDATE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(UPDATE_COMMENT));

    }

    @DeleteMapping("/api/v1/comments/{comment_id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                             @PathVariable("comment_id") Long commentId) {
        cmntService.deleteComment(commentId, principal);

        return ResponseEntity.status(DELETE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(DELETE_COMMENT));

    }
}
