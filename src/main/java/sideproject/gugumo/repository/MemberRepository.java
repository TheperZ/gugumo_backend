package sideproject.gugumo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByUsername(String username);

    public Optional<Member> findByNickname(String nickname);

    public Optional<Member> findByKakaoId(Long id);

}
