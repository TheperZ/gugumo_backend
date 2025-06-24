package sideproject.gugumo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long commentId;
    private Long parentCommentId;

    private String author;
    private boolean isYours;
    private boolean isAuthorExpired;

    private String content;
    private LocalDateTime createdAt;
    private List<CommentDto> childComments = new ArrayList<>();


    @QueryProjection
    public CommentDto(Long commentId, Long parentCommentId, String author, boolean isYours, boolean isAuthorExpired, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.isYours = isYours;
        this.isAuthorExpired = isAuthorExpired;
        this.content = content;
        this.createdAt = createdAt;
    }
}
