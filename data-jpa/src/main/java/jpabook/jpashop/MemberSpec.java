package jpabook.jpashop;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MemberSpec {
    public static Specification<Member> teamName(final String teamName) {
        return (Specification<Member>) (root, query, builder) -> {

            if (StringUtils.isEmpty(teamName)) {
                return null;
            }
            Join<Member, Team> t = root.join("team", JoinType.INNER); //회원과
            return builder.equal(t.get("name"), teamName);
        };
    }
    public static Specification<Member> username(final String username) {
        return (Specification<Member>) (root, query, builder) ->
                builder.equal(root.get("username"), username);
    }
}
