package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.gugumo.domain.entity.meeting.Meeting;
import sideproject.gugumo.domain.entity.meeting.MeetingStatus;
import sideproject.gugumo.domain.entity.post.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    public List<Meeting> findByMeetingDeadlineBeforeAndStatus(LocalDate date, MeetingStatus status);

}
