package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.Comment;

import java.util.Optional;

@Repository
public interface CmntRepository extends JpaRepository<Comment, Long>, CmntRepositoryCustom {

    public Optional<Comment> findByIdAndIsDeleteFalse(Long id);
}
