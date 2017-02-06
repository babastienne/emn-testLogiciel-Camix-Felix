package camix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Classe de lancement des tests pour Camix.
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	CanalChatTestEasyMock.class,
	ServiceChatTestEasyMock.class,
	ClientChatTestEasyMock.class,
})
public class CamixTestSuite {
	// Empty class
}
