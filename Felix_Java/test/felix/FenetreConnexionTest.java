package felix;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

/**
 * Clase testant la connexion à un chat.
 * Test unitaires effectués sur l'IHM
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
public class FenetreConnexionTest {

	/**
	 * Vue de la fenetre de connexion nécessaire aux tests
	 */
	private FenetreConnexion fenetreConnexion;
	
	/**
	 * Vue de la fenetre de chat nécessaire aux tests
	 */
	private JFrameOperator fenetreChat;
	
	/**
	 * Le bouton de la vue
	 */
	private JButtonOperator boutonConnexion;
	
	/**
	 * Les champs de texte de la vue
	 */
	private JTextFieldOperator fieldIPConnexion, fieldPortConnexion;
	
	/**
	 * Le label d'information de la vue
	 */
	private JLabelOperator labelInformationConnexion;
	
	/**
	 * La fenetre de la vue
	 */
	private JFrameOperator fenetre;
	
	/**
	 * Fixe les propriétés de Jemmy pour les tests.
	 * Crée un mock de la fenetre de chat et un mock de la connexion.
	 * Crée et affiche la vue nécessaire aux tests.
	 *
	 * Code exécuté avant les tests.
	 *
	 * @throws Exception toute exception.
	 * 
	 * @see org.netbeans.jemmy.JemmyProperties
	 *
	 */
	@Before
	public void setUp() throws Exception {
		// Fixe les timeouts de Jemmy (http://jemmy.java.net/OperatorsEnvironment.html#timeouts),
		// ici : 3s pour l'affichage d'une frame.
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		
		// création de la vue nécessaire aux tests
		this.fenetreConnexion = new FenetreConnexion(
				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_LARGEUR")),
				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR")),
				Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		
		// affichage de la vue et récuération de ses éléments
		this.fenetreConnexion.setVisible(true);
		this.recuperationVue();
	}
	
	/**
	 * Fermeture de la vue FenetreConnexion.
	 * 
	 * Méthode appelée après les test.
	 * 
	 * @throws Exception toute exception.
	 */
	@After
	public void tearDown() throws Exception {		
		if(this.fenetreConnexion != null)
			this.fenetreConnexion.ferme();
	}
	
	/**
	 * Récupération de la vue.
	 * Cette méthode récupère la vue FenetreConnexion.
	 */
	private void recuperationVue() {
		// index pour la récupération des widgets 
		Integer index = 2;
		
		// récupération de la fenêtre de la vue de la fenetre de connexion
		this.fenetre = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"));
		assertNotNull("La fenêtre de la vue de connexion n'est pas accessible", this.fenetre);
		
		// récupération du label d'information sur la connexion
		this.labelInformationConnexion = new JLabelOperator(this.fenetre, index);
		assertNotNull("Le label d'information de connexion de la fenêtre de connexion n'est pas accessible", this.labelInformationConnexion);
		
		// Ré-initialisation de l'index pour la récupération des widgets
		index = 0;
		
		// récupération du champ permettant l'insertion de l'ip de connexion
		this.fieldIPConnexion = new JTextFieldOperator(this.fenetre, index++);
		assertNotNull("Le champ permettant l'insertion de l'IP de connexion n'est pas accessible", this.fieldIPConnexion);
		
		// récupération du champ permettant l'insertion du port de connexion
		this.fieldPortConnexion = new JTextFieldOperator(this.fenetre, index++);
		assertNotNull("Le champ permettant l'insertion du port de connexion n'est pas accessible", this.fieldPortConnexion);
				
		// Ré-initialisation de l'index pour la récupération du bouton
		index = 0;
		
		// récupération du bouton permettant de lancer la connexion
		this.boutonConnexion = new JButtonOperator(this.fenetre, index++);
		assertNotNull("Le bouton permettant de lancer la connexion n'est pas accessible", this.boutonConnexion);
	}
	
	/**
	 * Test l'initialisation des différents champs de la vue.
	 */
	@Test
	public void testInitialiseVue() {
		// initialisation des résultats attendus pour les valeurs des éléments de la vue
		final String boutonConnexionAttendu = Felix.MESSAGES.getString("MESSAGE_CONNEXION");
		final String labelInformationConnexionAttendu = Felix.MESSAGES.getString("MESSAGE_INITIAL");
		final String fieldIPAttendu = Felix.CONFIGURATION.getString("ADRESSE_CHAT");
		final String fieldPortAttendu = Felix.CONFIGURATION.getString("PORT_CHAT");
		
		try {
			// récupération des valeurs réelles des éléments de la vue
			final String boutonConnexionActuel = this.boutonConnexion.getText();
			final String labelInformationConnexionActuel = this.labelInformationConnexion.getText();
			final String fieldIPActuel = this.fieldIPConnexion.getText();
			final String fieldPortActuel = this.fieldPortConnexion.getText();
			
			// Assertions
			assertEquals("Libellé du bouton de connexion invalide",  boutonConnexionAttendu, boutonConnexionActuel);
			assertEquals("Valeur du champs IP incorrecte",  fieldIPAttendu, fieldIPActuel);
			assertEquals("Valeur du champ port incorrecte",  fieldPortAttendu, fieldPortActuel);
			assertEquals("Valeur initiale du label d'information incorrecte",  labelInformationConnexionAttendu, labelInformationConnexionActuel);
		} catch (Exception e) {
			fail("Erreur lors de la récupération des données actuelles des éléments de la fenêtre de connexion.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de connexion au serveur par défault
	 * Note : un serveur actif Camix est nécessaire
	 * au bon déroulement de ce test
	 */
	@Test
	public void testConnexionEnCoursDefault() {
		testConnexionEnCours();
		
		testResultatConnexionOk();
	}
	
	/**
	 * Test de connexion lorsqu'un utilisateur rentre
	 * un port différent du port par défaut.
	 */
	@Test
	public void testConnexionEnCoursChangmentPort() {
		this.fieldPortConnexion.clearText();
		this.fieldPortConnexion.typeText("18532");
		
		testConnexionEnCours();
		
		testResultatConnexionImpossible();
	}
	
	/**
	 * Test de connexion lorsqu'un utilisateur rentre
	 * une adress ip différente de celle par défaut.
	 */
	@Test
	public void testConnexionEnCoursChangementIP() {
		this.fieldIPConnexion.clearText();
		this.fieldIPConnexion.typeText("192.168.2.45");
		
		testConnexionEnCours();
		
		testResultatConnexionImpossible();
	}
	
	/**
	 * Test de connexion lorsqu'un tilisateur rentre des informations 
	 * de connexion différentes de celles par défaut.
	 */
	@Test
	public void testConnexionEnCoursChangementIPEtPort() {
		this.fieldIPConnexion.clearText();
		this.fieldPortConnexion.clearText();
		this.fieldPortConnexion.typeText("14561");
		this.fieldIPConnexion.typeText("186.32.5.12");
		
		testConnexionEnCours();

		testResultatConnexionImpossible();
		
	}
	
	/**
	 * Méthode permettant le test des différents formats d'adresse
	 * ip refusés.
	 * Note : par soucis de temps, l'utilisation d'un jeu de test
	 * (avec fonction parameterized) n'a pas été effectuée.
	 */
	@Test
	public void testAdresseIPInvalide() {
		testAdresseIPInvalide("...");
		testAdresseIPInvalide("0...");
		testAdresseIPInvalide(".0..");
		testAdresseIPInvalide("..0.");
		testAdresseIPInvalide("...0");
		testAdresseIPInvalide("0.0..");
		testAdresseIPInvalide("0..0.");
		testAdresseIPInvalide("0...0");
		testAdresseIPInvalide(".0.0.");
		testAdresseIPInvalide(".0..0");
		testAdresseIPInvalide("..0.0");
		testAdresseIPInvalide("0.0.0.");
		testAdresseIPInvalide("0..0.0");
		testAdresseIPInvalide("0.0..0");
		testAdresseIPInvalide("-1.0.0.0");
		testAdresseIPInvalide("0.-1.0.0");
		testAdresseIPInvalide("0.0.-1.0");
		testAdresseIPInvalide("0.0.0.-1");
		testAdresseIPInvalide("256.0.0.0");
		testAdresseIPInvalide("0.256.0.0");
		testAdresseIPInvalide("0.0.256.0");
		testAdresseIPInvalide("0.0.0.256");
		testAdresseIPInvalide("adresse");
	}

	/**
	 * Méthode appelée lors des tests de connexion.
	 * Permet de vérifier que le message de connexion en cours
	 * est correct.
	 */
	private void testConnexionEnCours() {
		String ip = fieldIPConnexion.getText();
		String port = fieldPortConnexion.getText();
		
		String labelConnexionEnCoursAttendu = Felix.MESSAGES.getString("MESSAGE_CONNEXION") + " @" + ip + ":" + port;
		
		// on lance la tentative de connexion
		this.boutonConnexion.clickMouse();
		
		String labelConnexionEnCoursActuel = this.labelInformationConnexion.getText();	
		
		assertEquals("Valeur du message d'information de tentative de connexion en cours incorrecte", labelConnexionEnCoursAttendu, labelConnexionEnCoursActuel);
	}
	
	/**
	 * Méthode appelée lors des tests de connexion, permettant
	 * de vérifier que le message de connexion impossible est
	 * correct.
	 */
	private void testResultatConnexionImpossible() {
		String ip = fieldIPConnexion.getText();
		String port = fieldPortConnexion.getText();
		
		String labelConnexionTermineeAttendu = Felix.MESSAGES.getString("MESSAGE_CONNEXION") + " @" + ip + ":" + port + " impossible.";
		
		try {
			this.labelInformationConnexion.waitText(labelConnexionTermineeAttendu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String labelConnexionTermineeActuel = this.labelInformationConnexion.getText();
		assertEquals("Valeur du message d'information post-connexion incorrect", labelConnexionTermineeAttendu, labelConnexionTermineeActuel);
	}
	
	/**
	 * Méthode appelée lors des tests de connexion permettant
	 * de vérifier qu'une connexion a aboutie (= fenetre de chat
	 * ouverte).
	 */
	private void testResultatConnexionOk() {
		String labelInformationConnexionAboutiAttendu = Felix.MESSAGES.getString("MESSAGE_INITIAL");
		
		try {
			this.labelInformationConnexion.waitText(labelInformationConnexionAboutiAttendu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// récupération de la fenêtre de la vue de la fenetre de chat
		this.fenetreChat = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_TITRE"));
		assertNotNull("La fenêtre de chat n'est pas accessible", this.fenetreChat);
		
		String labelInformationConnexionAboutiActuel = this.labelInformationConnexion.getText();
		assertEquals("valeur du message d'information après connexion aboutie incorrecte",  labelInformationConnexionAboutiAttendu, labelInformationConnexionAboutiActuel);
	}
	
	/**
	 * Méthode permettant le test d'une connection au serveur avec
	 * adresse ip invalide. Le but est de montrer les différentes
	 * adresse refusées.
	 * @param ip l'adresse ip testée
	 */
	private void testAdresseIPInvalide(String ip) {
		this.fieldIPConnexion.clearText();
		this.fieldPortConnexion.clearText();
		this.fieldPortConnexion.typeText("12345");
		this.fieldIPConnexion.typeText(ip);
		
		String labelInformationConnexionIPInvalideAttendu = Felix.MESSAGES.getString("ADRESSE_IP_INVALIDE");
		
		// on lance la tentative de connexion
		this.boutonConnexion.clickMouse();
		
		String labelInformationConnexionIPInvalideActuel = this.labelInformationConnexion.getText();
		
		assertEquals("Valeur du label indiquant une adreese ip invalide incorrecte",  labelInformationConnexionIPInvalideAttendu, labelInformationConnexionIPInvalideActuel);
	}
}
