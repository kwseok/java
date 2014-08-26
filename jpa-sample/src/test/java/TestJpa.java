import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath*:META-INF/spring/context-application.xml",
        "classpath*:META-INF/spring/context-datasource-hsql.xml",
        "classpath*:META-INF/spring/context-persistence-jpa.xml",
        "classpath*:META-INF/spring/context-persistence-jpa-hsql.xml",
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