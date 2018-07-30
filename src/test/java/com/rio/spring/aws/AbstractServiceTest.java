package com.rio.spring.aws;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * @author rioortizr
 *
 */
@SpringBootTest
@RunWith( SpringRunner.class )
@ActiveProfiles( "test" )
@TestExecutionListeners( {
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class
} )
public abstract class AbstractServiceTest
{
}
