package com.genesys.rivescript;

import com.genesys.rivescript.engine.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by RomanH on 24.03.2017.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BeginTest.class,
        BotVariablesTest.class,
        MathTest.class,
        OptionsTest.class,
        RepliesTest.class,
        RiveScriptTest.class,
        SubstitutionsTest.class,
        TopicsTest.class,
        TriggersTest.class,
})
public class JunitTestSuite {
}
