import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "dev")
@ContextConfiguration(locations={
        "classpath*:META-INF/spring/context-application.xml",
        "classpath*:META-INF/spring/context-datasource-h2.xml",
        "classpath*:META-INF/spring/context-persistence-jpa.xml",
        "classpath*:META-INF/spring/context-persistence-jpa-h2.xml",
        "classpath*:META-INF/spring/context-transaction.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TestJpa {

    @Test
    public void testCase1() {
        System.out.println("helloTest");
    }

}