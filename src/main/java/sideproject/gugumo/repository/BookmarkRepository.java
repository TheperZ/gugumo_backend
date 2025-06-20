package sideproject.gugumo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Bookmark;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.post.Post;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b from Bookmark b where b.member=:member and (b.post.title like concat('%', :q, '%') or b.post.content like concat('%', :q, '%'))")
    public Page<Bookmark> findInBookmark(@Param("member") Member member, @Param("q") String q, Pageable pageable);

    public Optional<Bookmark> findByMemberAndPost(Member member, Post post);

    public boolean existsByMemberAndPost(Member member, Post post);

    public Long countByPost(Post post);

    public void deleteByPost(Post post);


}
