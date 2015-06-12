package shared;

import static junitparams.JUnitParamsRunner.*;
import junitparams.JUnitParamsRunner;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TestProvider {

    public Object[] parametersOperatorPattern() {
        return $(
                $("string>string", ">"),
                $("string<string", "<"),
                $("string=string", "="),
                $("string= string", "="),
                $("string = string", "="),
                $(" string = string ", "=")
        );
    }

    public Object[] idTestPattern() {
        return $(
                $(1, false), $(2, false), $(3, false), $(4, false),
                $(5, false), $(6, false), $(7, false), $(8, false),
                $(9, false), $(10, false), $(11, false), $(12, false),
                $(13, false), $(14, false), $(15, false), $(16, false)
        );
    }

    public Object[] templateTestPattern() {
        return $(
                $(1, false), $(2, false), $(3, false), $(4, false),
                $(5, false), $(6, false), $(7, false), $(8, false),
                $(9, false), $(10, false)
        );
    }

}
