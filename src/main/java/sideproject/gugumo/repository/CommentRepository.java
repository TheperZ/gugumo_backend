package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Comment;
import sideproject.gugumo.domain.entity.post.Post;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    public Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    Long countByPostAndIsDeletedFalse(Post post);
}
