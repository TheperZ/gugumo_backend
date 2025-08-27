package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.Member;
import sideproject.gugumo.domain.entity.notification.CustomNoti;

import java.util.List;

@Repository
public interface CustomNotiRepository extends JpaRepository<CustomNoti, Long> {
    public List<CustomNoti> findByMemberOrderByCreatedAtDesc(Member member);

    @Modifying(clearAutomatically = true)
    @Query("delete from CustomNoti c where c.member=:member and c.isRead=true")
    @Transactional
    public void deleteAllByMemberAndIsReadTrue(@Param("member") Member member);

}
