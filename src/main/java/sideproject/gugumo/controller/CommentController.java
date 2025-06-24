package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.request.CreateCommentReq;
import sideproject.gugumo.request.UpdateCommentReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.CommentService;

import java.util.List;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> saveComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                           @Valid @RequestBody CreateCommentReq req) {
        commentService.save(req, principal);

        return ResponseEntity.status(CREATE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(CREATE_COMMENT));

    }

    @GetMapping("/api/v1/posts/{post_id}/comments")
    public ResponseEntity<ApiResponse<List<CommentDto>>> findComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                                     @PathVariable("post_id") Long postId) {

        return ResponseEntity.status(FIND_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(FIND_COMMENT, commentService.findCommentNew(postId, principal)));
    }

    @PatchMapping("/api/v1/comments/{comment_id}")
    public ResponseEntity<ApiResponse<String>> updateComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                             @PathVariable("comment_id") Long commentId,
                                                             @RequestBody UpdateCommentReq req) {

        commentService.updateComment(commentId, req, principal);

        return ResponseEntity.status(UPDATE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(UPDATE_COMMENT));

    }

    @DeleteMapping("/api/v1/comments/{comment_id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@AuthenticationPrincipal CustomUserDetails principal,
                                                             @PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(commentId, principal);

        return ResponseEntity.status(DELETE_COMMENT.getHttpCode()).body(ApiResponse.createSuccess(DELETE_COMMENT));

    }
}
