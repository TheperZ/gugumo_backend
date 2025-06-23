package sideproject.gugumo.repository;

import sideproject.gugumo.domain.dto.CommentDto;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findComment(Long postId, Member user);
}
