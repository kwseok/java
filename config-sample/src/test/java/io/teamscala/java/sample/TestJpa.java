package io.teamscala.java.sample;

import com.mysema.query.jpa.impl.JPAQuery;
import io.teamscala.java.sample.config.ApplicationConfig;
import io.teamscala.java.sample.config.DataSourceConfig;
import io.teamscala.java.sample.config.PersistenceConfig;
import io.teamscala.java.sample.config.TransactionConfig;
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

import static io.teamscala.java.sample.misc.Globals.Profiles.DEV;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = DEV)
@ContextConfiguration(classes = {
    ApplicationConfig.class,
    DataSourceConfig.class,
    PersistenceConfig.class,
    TransactionConfig.class
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class TestJpa {

    private final Logger log = LoggerFactory.getLogger(TestJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testJpql() {
        TypedQuery<User> query = em.createQuery("select u from User u where u.username = :username", User.class);
        query.setParameter("username", "tester");

        List<User> list = query.getResultList();
        for (User user : list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }

    @Test
    public void testJpaCriteria() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);

        Root<User> u = q.from(User.class);
        q.where(cb.equal(u.get("username"), "tester"));

        TypedQuery<User> query = em.createQuery(q);
        List<User> list = query.getResultList();

        for (User user : list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }

    @Test
    public void testQueryDsl() {
        QUser u = QUser.user;
        JPAQuery query = new JPAQuery(em);

        List<User> list = query.from(u).where(u.username.eq("tester")).list(u);
        for (User user : list) {
            log(String.format("user [%s] selected.", user.getUsername()));
        }
    }


    // Private methods

    private void log(String message) {
        log.warn(message);
    }

}