package sideproject.gugumo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sideproject.gugumo.domain.entity.member.Member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepository {

    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList().stream().findAny();
    }

    public Optional<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findAny();
    }

    public Optional<Member> findByKakaoId(Long id) {
        return em.createQuery("select m from Member m where m.kakaoId = :id", Member.class)
                .setParameter("id", id)
                .getResultList().stream().findAny();
    }

    @Transactional
    public void deleteMember(Long id) {
        Member findMember = em.find(Member.class, id);
//        em.remove(findMember);
        findMember.deleteMember();
    }
}
