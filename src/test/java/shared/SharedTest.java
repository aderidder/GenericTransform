package shared;

import junit.framework.TestCase;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class SharedTest extends TestCase {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    @Parameters(source = TestProvider.class, method = "parametersOperatorPattern")
    public void testGetTranslatedFileName(String line, String operator)  throws InvocationTargetException {
        String foundOperator = OperatorOperations.getOperator(line);
        assertThat(foundOperator, is(operator));
    }

    @Test
    @Parameters(source = TestProvider.class, method = "idTestPattern")
    public void testIdFiles(int testNr, boolean value){
//        exit.expectSystemExit();
        Logging.setWriteToScreen(false);
        assertThat(TestTransform.runTest("id", testNr), is(value));
    }

    @Test
    @Parameters(source = TestProvider.class, method = "templateTestPattern")
    public void testTemplateFiles(int testNr, boolean value){
        Logging.setWriteToScreen(false);
        assertThat(TestTransform.runTest("template", testNr), is(value));
    }
}