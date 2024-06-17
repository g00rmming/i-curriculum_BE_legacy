package goorming.iCurriculum.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByClientId(String username);

    boolean existsByNickname(String nickname);

    Member findByClientId(String username);
}
