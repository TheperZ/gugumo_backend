package sideproject.gugumo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sideproject.gugumo.domain.dto.detailpostdto.DetailPostDto;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostDto;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.request.CreatePostReq;
import sideproject.gugumo.request.UpdatePostReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.PostService;

import java.util.List;

import static sideproject.gugumo.response.StatusCode.*;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> save(@AuthenticationPrincipal CustomUserDetails principal,
                                                    @RequestBody @Valid CreatePostReq createPostReq) {
        postService.save(principal, createPostReq);

        return ResponseEntity.status(CREATE_POST.getHttpCode()).body(ApiResponse.createSuccess(CREATE_POST));
    }


    /**
     * 정렬(Sort)은 조건이 조금만 복잡해져도 Pageable의 Sort기능을 사용하기 어렵다. 루트 엔티티 범위를 넘어가는(join을 하는 등)
     * 동적 정렬 기능이 필요하면 스프링 데이터 페이징이 제공하는 Sort를 사용하기 보다는 파라미터를 받아서 직접 처리하는 것을 권장한다.
     */
    @GetMapping("/api/v1/posts")
    public <T extends SimplePostDto> ResponseEntity<ApiResponse<PageCustom<T>>> findPostSimple(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size = 12) Pageable pageable,
            @RequestParam(required = false, value = "q") String q,
            @RequestParam(required = false, value = "location") String location,
            @RequestParam(required = false, value = "gametype") String gameType,
            @RequestParam(required = false, value = "meetingstatus", defaultValue = "RECRUIT") String meetingStatus,
            @RequestParam(required = false, value = "sort", defaultValue = "NEW") String sortType) {


        return ResponseEntity.status(FIND_POST.getHttpCode()).body(ApiResponse.createSuccess(FIND_POST, postService.findSimplePost(principal, pageable, q, gameType, location, meetingStatus, sortType)));
    }

    @GetMapping("/api/v1/posts/{post_id}")
    public <T extends DetailPostDto> ResponseEntity<ApiResponse<T>> findPostDetail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {
        DetailPostDto detailPostDto = postService.findDetailPostByPostId(principal, postId);

        return ResponseEntity.status(FIND_POST_DETAIL.getHttpCode()).body(ApiResponse.createSuccess((T) detailPostDto));
    }

    @PatchMapping("/api/v1/posts/{post_id}")
    public ResponseEntity<ApiResponse<String>> updatePost(@AuthenticationPrincipal CustomUserDetails principal,
                                          @PathVariable("post_id") Long postId,
                                          @RequestBody @Valid UpdatePostReq updatePostReq) {
        postService.update(principal, postId, updatePostReq);

        return ResponseEntity.status(UPDATE_POST.getHttpCode()).body(ApiResponse.createSuccess(UPDATE_POST));
    }


    @DeleteMapping("/api/v1/posts/{post_id}")
    public ResponseEntity<ApiResponse<String>> deletePost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("post_id") Long postId) {

        postService.deletePost(principal, postId);

        return ResponseEntity.status(DELETE_POST.getHttpCode()).body(ApiResponse.createSuccess(DELETE_POST));
    }


    @GetMapping("/api/v1/posts/my")
    public <T extends SimplePostDto> ResponseEntity<ApiResponse<PageCustom<T>>> findMyPost(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size = 12, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "q", defaultValue = "") String q) {
        return ResponseEntity.status(FIND_MY_POST.getHttpCode()).body(ApiResponse.createSuccess(postService.findMyPost(principal, pageable, q)));

    }

    @GetMapping("/api/v1/posts/recommend")
    public <T extends SimplePostDto> ResponseEntity<ApiResponse<List<T>>> findRecommendPost(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.status(FIND_RECOMMEND_POST.getHttpCode()).body(ApiResponse.createSuccess(FIND_RECOMMEND_POST, postService.findRecommendPost(principal)));
    }


}
