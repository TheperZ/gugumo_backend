package sideproject.gugumo.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmntDto {

    private Long commentId;

    private String author;
    private boolean isYours;
    private boolean isAuthorExpired;

    private String content;
    private LocalDateTime createdDateTime;
    private boolean isNotRoot;
    private Long parentCommentId;       //부모가 존재하지 않으면 null 반환->값을 주지 않음
    private long orderNum;


    @QueryProjection
    public CmntDto(Long commentId, String author, boolean isYours, boolean isAuthorExpired, String content, LocalDateTime createdDateTime, boolean isNotRoot, Long parentCommentId, long orderNum) {
        this.commentId = commentId;
        this.author = author;
        this.isYours = isYours;
        this.isAuthorExpired = isAuthorExpired;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.isNotRoot = isNotRoot;
        this.parentCommentId = parentCommentId;
        this.orderNum = orderNum;
    }
}
