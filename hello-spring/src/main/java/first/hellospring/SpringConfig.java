package first.hellospring;

import first.hellospring.repository.JdbcTemplateMemberRepository;
import first.hellospring.repository.JpaMemberRepository;
import first.hellospring.repository.MemberRepository;
import first.hellospring.repository.MemoryMemberRepository;
import first.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }
/*
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource=dataSource;
    }

 */

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        //return new MemoryMemberRepository();
        //return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }

}
