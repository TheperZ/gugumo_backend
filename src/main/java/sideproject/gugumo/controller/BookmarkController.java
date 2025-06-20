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
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostDto;
import sideproject.gugumo.page.PageCustom;
import sideproject.gugumo.request.CreateBookmarkReq;
import sideproject.gugumo.response.ApiResponse;
import sideproject.gugumo.service.BookmarkService;

import static sideproject.gugumo.response.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping("/api/v1/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> saveBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid CreateBookmarkReq createBookmarkReq) {

        bookmarkService.save(principal, createBookmarkReq);

        return ResponseEntity.status(CREATE_BOOKMARK.getHttpCode()).body(ApiResponse.createSuccess(CREATE_BOOKMARK));
    }

    @GetMapping("/api/v1/bookmarks")
    public <T extends SimplePostDto> ResponseEntity<ApiResponse<PageCustom<T>>> findBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "q", defaultValue = "") String q) {

        return ResponseEntity.status(FIND_BOOKMARK.getHttpCode()).body(ApiResponse.createSuccess(FIND_BOOKMARK, bookmarkService.findBookmarkByMember(principal, pageable, q)));
    }

    @DeleteMapping("/api/v1/bookmarks/{bookmark_id}")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("bookmark_id") Long bookmarkId) {
        bookmarkService.delete(bookmarkId, principal);

        return ResponseEntity.status(DELETE_BOOKMARK.getHttpCode()).body(ApiResponse.createSuccess(DELETE_BOOKMARK));
    }

}
