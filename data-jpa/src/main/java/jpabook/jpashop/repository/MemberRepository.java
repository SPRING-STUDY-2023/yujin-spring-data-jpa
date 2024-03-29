package jpabook.jpashop.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.dto.UsernameOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")
//    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select new jpabook.jpashop.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

//    Page<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용
//    Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용안함
//    List<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용안함
//    List<Member> findByUsername(String name, Sort sort);

    @Modifying(clearAutomatically = true) // 벌크성 수정, 삭제 쿼리는 영속성 초기화까지 해야함 (DB만 연산하고, 영속성 컨텍스트는 무시되고 실행됨)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"}) List<Member> findAll();
    //JPQL + 엔티티 그래프
//    @EntityGraph(attributePaths = {"team"})
//    @Query("select m from Member m")
//    List<Member> findMemberEntityGraph();
//메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value =
            "true"))
    Member findReadOnlyByUsername(String username);

    @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly",
            value = "true")}, forCounting = true)
    Page<Member> findByUsername(String name, Pageable pageable);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    List<Member> findByUsername(String name);
    List<UsernameOnly> findProjectionsByUsername(String userName);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);


//    @Query(value = "SELECT m.member_id as id, m.username, t.name as teamName " +
//            "FROM member m left join team t ON m.team_id = t.team_id",
//            countQuery = "SELECT count(*) from member",
//            nativeQuery = true)
//    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
