package sideproject.gugumo.repository;

import sideproject.gugumo.domain.dto.CmntDto;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;

public interface CmntRepositoryCustom {
    List<CmntDto> findComment(Long postId, Member user);
}
