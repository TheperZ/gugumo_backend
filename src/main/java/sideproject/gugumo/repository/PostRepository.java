package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByIdAndIsDeleteFalse(Long id);

    Page<Post> findByMemberAndTitleContainingAndIsDeleteFalse(Pageable pageable, Member member, String title);
}
