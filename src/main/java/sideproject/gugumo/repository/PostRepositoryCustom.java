package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sideproject.gugumo.cond.PostSearchCondition;
import sideproject.gugumo.domain.dto.simplepostdto.SimplePostQueryDto;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.List;

public interface PostRepositoryCustom {
    Page<SimplePostQueryDto> search(PostSearchCondition cond, Pageable pageable, Member member);

    Page<SimplePostQueryDto> searchMy(Pageable pageable, Member member, String q);

    List<SimplePostQueryDto> findRecommendPost(Member member);
}
