package felix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTextPaneOperator;

/**
 * Classe effectuant un test de validation
 * sur l'interaction de plusieurs instances
 * de felix avec u serveur de chat Camix.
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
public class FenetreTest {

	/**
	 * Nombre d'instance d'applications Felix impliquées dans le test
	 */
	private static final int NBINSTANCE = 2;
	
	/**
	 * L'application à tester
	 */
	private static ClassReference application;
	
	/**
	 * Les paramètres de lancement de l'application
	 */
	private static String[] parametres;
	
	// Widget de la vue
	/**
	 * Opérateurs de JFrame utiles pour le test
	 */
	private JFrameOperator[] fenetreConnexion = new JFrameOperator[NBINSTANCE];
	private JFrameOperator[] fenetreChat = new JFrameOperator[NBINSTANCE];
	
	/**
	 * Le bouton de connexion au chat
	 */
	private JButtonOperator[] boutonConnexionChat = new JButtonOperator[NBINSTANCE];
	
	/**
	 * Les champs de saisi des messages.
	 */
	private JTextFieldOperator[] fieldTexteMessage = new JTextFieldOperator[NBINSTANCE];
	private JTextFieldOperator[] fieldIPConnexion = new JTextFieldOperator[NBINSTANCE];
	private JTextFieldOperator[] fieldPortConnexion = new JTextFieldOperator[NBINSTANCE];

	// Champ texte de la vue
	/**
	 * Le panneau texte des messages du chat.
	 */
	private JTextPaneOperator[] paneTexteChatMessages = new JTextPaneOperator[NBINSTANCE];
	
	/**
	 * Le label d'information sur la connexion au chat
	 */
	private JLabelOperator[] labelInformationConnexion = new JLabelOperator[NBINSTANCE];
	
	/**
	 * Positions courantes dans les panneaux textes des vues (ticket) des différentes instances de Felix
	 */
	private static int[] panPosition = new int[NBINSTANCE];
	
	/**
	 * Configuration de Jemmy et lancement des applications à tester
	 * 
	 * @throws Exception toute exception
	 */
	@Before
	public void setUp() throws Exception {
		// Fixe les timeouts de Jemmy (http://jemmy.java.net/OperatorsEnvironment.html#timeouts),
		// ici : 3s pour l'affichage d'une frame ou une attente de changement d'état (waitText par exemple).
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
		JemmyProperties.setCurrentTimeout("ComponentOperator.WaitStateTimeout", timeout);
		
		if(FenetreTest.application == null) {
			// Démarrage des instances de Felix nécessaires aux test,
			// si celles-ci ne sont pas déjà lancées.
			try {
				FenetreTest.application = new ClassReference("felix.Felix");
				FenetreTest.parametres = new String[1];
				FenetreTest.parametres[0] = "";
				
				
			} catch (Exception e) {
				fail("Problème d'accès à la classe invoquée : " + e.getMessage());
				throw e;
			}
			this.lanceInstancesFelix();
		} else {
			this.recuperationinterfacesFelix();
		}
	}

	/**
	 * Code executé après les tests. Destruction des objets encore
	 * existants.
	 * 
	 * @throws Exception toute exception
	 */
	@After
	public void tearDown() throws Exception {
		if(FenetreTest.application != null)
			FenetreTest.application = null;
	}
	
	/**
	 * Lancement de toutes les instances de Felix nécessaires aux tests
	 * 
	 * @throws Exception toute exception
	 */
	private void lanceInstancesFelix() throws Exception {
		for(int index = 0; index < NBINSTANCE; index++) {
			// lancement d'une instance de felix
			this.lanceInstance(index);		
			
			// 10 secondes d'observation par suspension du thread (objectif pédagogique)
			// (pour prendre le temps de déplacer les fenêtres à l'écran).
			final Long timeout = Long.valueOf(3000); 
			Thread.sleep(timeout);
		}
		
	}

	/**
	 * Lancement d'une instance de Felix
	 * 
	 * @param index le numéro de l'instance
	 * @throws Exception toute exception
	 */
	private void lanceInstance(int index) throws Exception {
		try {
			// Lancement d'une application
			FenetreTest.application.startApplication(FenetreTest.parametres);
			
			FenetreTest.panPosition[index] = 0;
			
		} catch (Exception e) {
			fail("Erreur lors de la création d'une application : " + e.getMessage());
			throw e;
		}
		this.recuperationInterface(index);
	}

	/**
	 * Récupération de l'interface de l'instance de Felix
	 * Note : le serveur de chat Camix doit être actif
	 * @param index
	 */
	private void recuperationInterface(int index) {
		// Récupération de l'interface de la vue connexion
		this.recuperationVueConnexion(index);
		
		// Comme on s'interesse ici au chat, on lance immédiatement la connexion au serveur
		this.lancementConnexion(index);
		
		// On récupère l'interface de la vue Chat
		this.recuperationVueChat(index);
	}

	/**
	 * Méthode permettant de récupérer l'interface de la vue connexion.
	 * On pourra ensuite manipuler l'interface pour les tests.
	 * 
	 * @param index l'index de l'instance de la vue à récupérer
	 */
	private void recuperationVueConnexion(int index) {
		// index pour la récupération des widgets 
		Integer iW = 2;
		
		// récupération de la fenêtre de la vue de la fenetre de connexion
		this.fenetreConnexion[index] = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_CONNEXION_TITRE"), index);
		assertNotNull("La fenêtre de la vue de connexion n'est pas accessible", this.fenetreConnexion[index]);
		
		// récupération du label d'information sur la connexion
		this.labelInformationConnexion[index] = new JLabelOperator(this.fenetreConnexion[index], iW);
		assertNotNull("Le label d'information de connexion de la fenêtre de connexion n'est pas accessible", this.labelInformationConnexion[index]);
		
		// Ré-initialisation de l'index pour la récupération des widgets
		iW = 0;
		
		// récupération du champ permettant l'insertion de l'ip de connexion
		this.fieldIPConnexion[index] = new JTextFieldOperator(this.fenetreConnexion[index], iW++);
		assertNotNull("Le champ permettant l'insertion de l'IP de connexion n'est pas accessible", this.fieldIPConnexion[index]);
		
		// récupération du champ permettant l'insertion du port de connexion
		this.fieldPortConnexion[index] = new JTextFieldOperator(this.fenetreConnexion[index], iW++);
		assertNotNull("Le champ permettant l'insertion du port de connexion n'est pas accessible", this.fieldPortConnexion[index]);
				
		// Ré-initialisation de l'index pour la récupération du bouton
		iW = 0;
		
		// récupération du bouton permettant de lancer la connexion
		this.boutonConnexionChat[index] = new JButtonOperator(this.fenetreConnexion[index], iW++);
		assertNotNull("Le bouton permettant de lancer la connexion n'est pas accessible", this.boutonConnexionChat[index]);		
	}
	
	/**
	 * On lance la connexion d'une instance d'une fenetre de connexion
	 * 
	 * @param index index de l'instance de la vue pour laquelle lancer la connexion
	 */
	private void lancementConnexion(int index) {
		this.boutonConnexionChat[index].clickMouse();
		
		// Note : les tests relatifs à la connexion sont effectués dans une autre classe de test
		// @see FenetreConnexionTest
		
		String labelInformationConnexionAboutiAttendu = Felix.MESSAGES.getString("MESSAGE_INITIAL");
		
		try {
			this.labelInformationConnexion[index].waitText(labelInformationConnexionAboutiAttendu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode de récupération de la vue du chat
	 * 
	 * @param index l'index de l'instance de la vue chat à récupérer
	 */
	private void recuperationVueChat(int index) {
		// Récupération de la fenetre de chat
		this.fenetreChat[index] = new JFrameOperator(Felix.CONFIGURATION.getString("FENETRE_TITRE"), index);
		assertNotNull("La fenêtre de chat n'est pas accessible",  this.fenetreChat[index]);
		
		// Récupération du champ d'insertion d'un message
		this.fieldTexteMessage[index] = new JTextFieldOperator(this.fenetreChat[index], 0);
		assertNotNull("Le champ d'insertino de message n'est pas accessible", this.fieldTexteMessage[index]);
		
		// Récupération du panneau d'affichage des message
		this.paneTexteChatMessages[index] = new JTextPaneOperator(this.fenetreChat[index], 0);
		assertNotNull("Le panneau d'affichage des messages du chat n'est pas accessible", this.paneTexteChatMessages[index]);
	}
	
	private void recuperationinterfacesFelix() {
		for (int index = 0; index < NBINSTANCE; index++) {
			this.recuperationInterface(index);
		}
	}
	
	/**
	 * Méthode de test vérifiant la bonne initialisation des composant de la vue chargée.
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
			final String boutonConnexionActuel = this.boutonConnexionChat[0].getText();
			final String labelInformationConnexionActuel = this.labelInformationConnexion[0].getText();
			final String fieldIPActuel = this.fieldIPConnexion[0].getText();
			final String fieldPortActuel = this.fieldPortConnexion[0].getText();
			
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
	
}
