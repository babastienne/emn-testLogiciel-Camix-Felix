package felix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite de tests unitaires pour le programme Felix
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	FenetreConnexionTest.class, 
	FenetreTest.class,
})
public class FelixTestSuite {

}
