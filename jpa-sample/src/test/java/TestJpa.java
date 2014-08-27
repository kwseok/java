import com.mysema.query.jpa.impl.JPAQuery;
import io.teamscala.java.sample.models.QUser;
import io.teamscala.java.sample.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "dev")
@ContextConfiguration(locations={
        "classpath*:META-INF/spring/context-application.xml",
        "classpath*:META-INF/spring/context-datasource-h2.xml",
        "classpath*:META-INF/spring/context-persistence-jpa.xml",
        "classpath*:META-INF/spring/context-persistence-jpa-h2.xml",
        "classpath*:META-INF/spring/context-transaction.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class TestJpa {

    private final Logger log = LoggerFactory.getLogger(TestJpa.class);

    @PersistenceContext EntityManager em;

    @Test
    public void testJpql() {
        Query query = em.createQuery("select u from User u where u.username = :username");
        query.setParameter("username", "tester");

        List<User> list = query.getResultList();
        for (User user: list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }

    @Test
    public void testJpaCriteria() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery(User.class);

        Root<User> u = q.from(User.class);
        q.where(cb.equal(u.get("username"), "tester"));

        Query query = em.createQuery(q);
        List<User> list = query.getResultList();

        for (User user: list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }

    @Test
    public void testQueryDsl() {
        QUser u = QUser.user;
        JPAQuery query = new JPAQuery(em);

        List<User> list = query.from(u).where(u.username.eq("tester")).list(u);
        for (User user: list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }


    // Private methods

    private void log(String message) { log.warn(message); }

}