package sideproject.gugumo.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

@Getter
@AllArgsConstructor
public class CommentFcmEvent {


    private Comment comment;
    private Member commentAuthor;

    public boolean isCommentPostAuthorEq(Post post) {
        return !commentAuthor.getId().equals(post.getMember().getId());
    }

}
