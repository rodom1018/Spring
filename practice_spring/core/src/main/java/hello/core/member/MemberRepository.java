package hello.core.member;

public interface MemberRepository {
    void save(Member member);

    public Member findById(Long memberId);
}
