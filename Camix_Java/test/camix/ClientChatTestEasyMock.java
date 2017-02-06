package camix;

import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test du client chat
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
public class ClientChatTestEasyMock {
	
	/**
	 * Canal chat utilisé lors de la création d'un client chat
	 */
	private CanalChat canal;
	
	/**
	 * Le service chat utilisé lors du test
	 */
	private ServiceChat chat;
	
	/**
	 * On créé un faux serviceChat et un faux canalChat à l'aide 
	 * d'easyMock pour répondre aux besoins du test.
	 * On note que le code suivant est executé avant le test.
	 * 
	 * @throws Exception toute exception
	 * @see org.easymock.EasyMock
	 */
	@Before
	public void setUp() throws Exception {
		// creation du faux canal chat
		this.canal = EasyMock.createMock(CanalChat.class);
		
		// création du faux service chat
		this.chat = EasyMock.createMock(ServiceChat.class);
	}
	
	/**
	 * Test de demande de traitement d'un message système de déconnexion.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageDeconnexion() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_DECONNEXION_CHAT;
			
			this.chat.fermeConnexion(client);
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message système concernant le changement de surnom d'un client.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageChangeSurnom() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_CHANGE_SURNOM_CLIENT;
			
			this.chat.changeSurnomClient(client, ProtocoleChat.parametreCommande(message));
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message système concernant le changement de canal.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageChangeCanalClient() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_CHANGE_CANAL_CLIENT;
			
			this.chat.changeCanalClient(client, ProtocoleChat.parametreCommande(message));
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message système concernant l'ajout d'un canal.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageAjouteCanal() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_AJOUTE_CANAL;
			
			this.chat.ajouteCanal(client, ProtocoleChat.parametreCommande(message));
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message système concernant la suppression d'un canal.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageSupprimeCanal() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_SUPPRIME_CANAL;
			
			this.chat.supprimeCanal(client, ProtocoleChat.parametreCommande(message));
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message système d'informations sur les canaux.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageAfficheCanaux() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_AFFICHE_CANAUX;
			
			this.chat.afficheCanaux(client);
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message demandant l'affichage d'informations client.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageAfficheClient() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + ProtocoleChat.COMMANDE_AFFICHE_CLIENT;
			
			this.chat.afficheInformationsClient(client);;
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de demande de traitement d'un message d'aide.
	 * Cas nominal du traitement d'un message système.
	 * La méthode testée est 'traiteMessage'.
	 */
	@Test
	public void testTraiteMessageAide() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);
			String message = "/" + "messageSystemeStandard";
			
			this.chat.afficheAide(client);;
			
			traiteMessage(client, message);	
		} catch (Exception e) {
			fail("Erreur : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode générique appelée pour effectuer le test avec le mock
	 * ServiceChat afin de vérifier le bon fonctionnement de la méthode 
	 * traiteMessage d'un ClientChat.
	 * Cette méthode est appelée par différentes méthodes de test.
	 * @param client les client chat à appeler (objet testé)
	 * @param message le massage envoyé à la methode traiteMessage sur laquelle est effectué le test
	 */
	private void traiteMessage(ClientChat client, String message) {
		Method method;
		String methodeTestee = "traiteMessage";
		Class<?>[] parametresMethodetestee = {String.class};
		
		try {
			method = client.getClass().getDeclaredMethod(methodeTestee, parametresMethodetestee);
			method.setAccessible(true);
			
			EasyMock.replay(chat);
			
			method.invoke(client, message);
			
			EasyMock.verify(chat);
		} catch (Exception e) {
			fail("Erreur lors de l'invocation de la méthode : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFermeConnexion() {
		try {
			ClientChat client = new ClientChat(chat, null, "merlin", canal);

			canal.enleveClient(client);
			
			EasyMock.replay(canal);
			
			client.fermeConnexion();
			
			EasyMock.verify(canal);
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
