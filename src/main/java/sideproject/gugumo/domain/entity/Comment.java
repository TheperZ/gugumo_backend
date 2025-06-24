package sideproject.gugumo.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;
import sideproject.gugumo.request.UpdateCommentReq;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comment")
@Builder
@AllArgsConstructor
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @NotNull
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    @Builder.Default
    private List<Comment> childComments = new ArrayList<>();

    // 부모 댓글을 추가하면서 부모 댓글에도 자식 댓글 추가
    public void updateParent(Comment parentComment) {
        this.parentComment = parentComment;
        parentComment.addChild(this);
    }

    public void addChild(Comment childComment) {
        childComments.add(childComment);
    }

    public void tempDelete() {
        this.isDeleted = true;
    }

    public void update(UpdateCommentReq req) {
        this.content = req.getContent();
    }
}
