package sideproject.gugumo.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByIdAndIsDeleteFalse(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p where p.id=:id and p.isDelete=false")
    Optional<Post> findByIdAndIsDeleteFalseWithLock(@Param("id") Long id);

    Page<Post> findByMemberAndTitleContainingAndIsDeleteFalse(Pageable pageable, Member member, String title);
}
