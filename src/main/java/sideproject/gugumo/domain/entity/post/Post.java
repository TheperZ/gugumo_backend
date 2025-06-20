package sideproject.gugumo.domain.entity.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import sideproject.gugumo.domain.entity.meeting.Meeting;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.request.UpdatePostReq;

import java.time.LocalDateTime;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column(length = 10000)
    private String content;

    @NotNull
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    @NotNull
    @Builder.Default
    private long viewCount = 0;

    @NotNull
    @Builder.Default
    private boolean isDelete = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @NotNull
    @Builder.Default
    private long commentCnt = 0L;


    public void addViewCount() {
        this.viewCount += 1;
    }

    public void update(UpdatePostReq updatePostReq) {

        this.title = updatePostReq.getTitle();
        this.content = updatePostReq.getContent();


    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public void tempDelete() {
        this.isDelete = true;
    }

    public void increaseCommentCnt() {
        this.commentCnt++;
    }

    public void decreaseCommentCnt() {
        this.commentCnt--;
    }
}
