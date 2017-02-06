package camix;

import static org.junit.Assert.fail;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Optional;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test de ServiceChat
 * 
 * @version 1.1
 * @author bpotiron
 *
 */
public class ServiceChatTestEasyMock {
	
	/**
	 * Le client chat utilisé lors du test
	 */
	private ClientChat client;
	
	/**
	 * On créé un faux clientChat à l'aide d'easyMock pour répondre aux
	 * besoins du test.
	 * On note que le code suivant est executé avant le test.
	 * 
	 * @throws Exception toute exception
	 * @see org.easymock.EasyMock
	 */
	@Before
	public void setUp() throws Exception {
		this.client = EasyMock.createMock(ClientChat.class);
	}

	/**
	 * Test permettant de simuler le départ d'un client du serveur de chat.
	 * La méthode testée est 'informeDepartClient'.
	 */
	@Test
	public void testInformeDepartClient() {
		try {
			ServiceChat service = new ServiceChat("default", 12652);
			
			String surnom = "merlin";
			
			EasyMock.expect(this.client.donneSurnom()).andReturn(surnom);
			
			String message = String.format(ProtocoleChat.MESSAGE_DEPART_CHAT, surnom);
			this.client.envoieContacts(message);
			
			EasyMock.replay(this.client);
			
			service.informeDepartClient(this.client);
			
			EasyMock.verify(this.client);
			
		} catch (IOException e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * On test d'ajouter un canal qui n'existe pas déjà
	 */
	@Test
	public void testAjouteCanalUnique() {
		testAjouteCanal(false);
	}
	
	/**
	 * On test l'ajout d'un canal déjà existant (deux fois le même).
	 * Un message différent du scénario nominal est alors attendu.
	 */
	@Test
	public void testAjouteCanalExistant() {
		testAjouteCanal(true);
	}
	
	/**
	 * Test permettant de simuler l'ajout d'un canal sur le serveur de chat.
	 * La méthode testée est 'ajouteCanal'.
	 * 
	 * @param testMultipleCan informe si l'on dois créer plusieurs fois le meme canal
	 */
	private void testAjouteCanal(boolean testMultipleCan) {
		try {
			ServiceChat service = new ServiceChat("default", 12345);
			String canal = "canalAjoute";
			 
			String ajoutValide = String.format(ProtocoleChat.MESSAGE_CREATION_CANAL, canal);
			
			// Si l'ajout est correct, alors le client recevra le message 'ajoutValide'
			this.client.envoieMessage(ajoutValide);
			
			EasyMock.replay(client);
			
			service.ajouteCanal(this.client, canal);
			
			EasyMock.verify(client);
			
			if(testMultipleCan) {
				EasyMock.reset(client);
				String ajoutNonValide = String.format(ProtocoleChat.MESSAGE_CREATION_IMPOSSIBLE_CANAL, canal);
				
				// L'ajout ne doit pas être validé. Le message recu sera 'ajoutNonValide'
				this.client.envoieMessage(ajoutNonValide);
				
				EasyMock.replay(client);
				
				service.ajouteCanal(this.client, canal);
				
				EasyMock.verify(client);
			}
			 
		} catch (IOException e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de suppression d'un canal du serveur de chat.
	 * Cas où la suppression est effectuée avec succès (cas nominal).
	 * La méthode testée est 'supprimeCanal'.
	 */
	@Test
	public void testSupprimeCanalNominal() {
		try {
			String canalDefaut = "default";
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			String canalAjoute = "newCanal";
			
			service.ajouteCanal(client, canalAjoute);
			
			String message = String.format(ProtocoleChat.MESSAGE_SUPPRESSION_CANAL, canalAjoute);
			supprimeCanal(message, service, canalAjoute);
			
		} catch(Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de suppression d'un canal du serveur de chat.
	 * Cas où la suppression est impossible car le canal est non vide.
	 * La méthode testée est 'supprimeCanal'.
	 */
	@Test
	public void testSupprimeCanalNonVide() {
		try {
			String canalDefaut = "default";
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			String canalAjoute = "newCanal";
			
			// on utilise la méthode créée pour changer un client de canal
			changeCanalClient(true, service, canalDefaut, canalAjoute);
			
			String message = String.format(ProtocoleChat.MESSAGE_SUPPRESSION_CANAL_NON_VIDE, canalAjoute);
			supprimeCanal(message, service, canalAjoute);
			
		} catch(Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de suppression d'un canal du serveur de chat.
	 * Cas où la suppression est impossible car le canal est le canal par defaut.
	 * La méthode testée est 'supprimeCanal'.
	 */
	@Test
	public void testSupprimeCanalParDefaut() {
		try {
			String canalDefaut = "default";
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			
			String message = String.format(ProtocoleChat.MESSAGE_SUPPRESSION_CANAL_PAR_DEFAUT, canalDefaut);
			supprimeCanal(message, service, canalDefaut);
			
		} catch(Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de suppression d'un canal du serveur de chat.
	 * Cas où la suppression est impossible car le canal n'existe pas.
	 * La méthode testée est 'supprimeCanal'.
	 */
	@Test
	public void testSupprimeCanalInexistant() {
		try {
			String canalDefaut = "default";
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			String canalInexistant = "canalInexistant";
			
			String message = String.format(ProtocoleChat.MESSAGE_SUPPRESSION_CANAL_INEXISTANT, canalInexistant);
			supprimeCanal(message, service, canalInexistant);
			
		} catch(Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode effectuant le test de suppression d'un canal.
	 * Méthode appelée par tous les TU concernant la methode 'supprimeCanal'.
	 * @param message le message avec lequel le client va être appelé
	 * @param service le service créé et utilisé dans le TU appelant
	 * @param canal le nom du canal à supprimer
	 */
	private void supprimeCanal(String message, ServiceChat service, String canal) {
		EasyMock.reset(client);
		this.client.envoieMessage(message);
		
		EasyMock.replay(client);
		
		service.supprimeCanal(client, canal);
		
		EasyMock.verify(client);
	}
	
	/**
	 * Test de changement de canal d'un client sur un canal existant
	 * Cas nominal du cu.
	 * La méthode testée est 'changeCanalClient'
	 */
	@Test
	public void testChangeCanalClientExiste() {
		String canalDefaut = "default";
		String canalAjoute = "newCanal";
		try {
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			
			changeCanalClient(true, service, canalDefaut, canalAjoute);
		} catch (IOException e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test de changement de canal d'un client sur un canal inexistant
	 * Cas non nominal, où le canal n'a pas été créé préalablement.
	 * La méthode testée est 'changeCanalClient'
	 */
	@Test
	public void testchangeCanalClientnonExistant() {
		String canalDefaut = "default";
		String canalAjoute = "newCanal";
		try {
			ServiceChat service = new ServiceChat(canalDefaut, 12345);
			
			changeCanalClient(false, service, canalDefaut, canalAjoute);
		} catch (IOException e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode utilisée par les tests pour changer de canal un client (mock)
	 * Utilisée à la fois parl es tests de changement de canal mais églament 
	 * de suppression de canal.
	 * @param canalExiste vrai si le canal dans lequel le changement est effectué doit exister (utilisé pour tester l'erreur en cas de canal non existant)
	 * @param service le ServiceChat créé et transmis en paramètre
	 * @param canalDefaut le nom du canal par défaut (tel que créé dans le service)
	 * @param canalAjoute le nom du canal secondaire à créer
	 */
	private void changeCanalClient(boolean canalExiste, ServiceChat service, String canalDefaut, String canalAjoute) {
		try {
			String clientNom = "merlin";

			if(canalExiste) {
				
				service.ajouteCanal(client, canalAjoute);
				
				EasyMock.reset(client);
				
				// préparation des méthode qui seront appelée sur client par 'changeCanalClient'
				EasyMock.expect(this.client.donneSurnom()).andReturn(clientNom);
				EasyMock.expect(this.client.donneCanal()).andReturn(new CanalChat(canalDefaut));
				
				String messageDepart = String.format(ProtocoleChat.MESSAGE_DEPART_CANAL, clientNom, canalDefaut);
				client.envoieCanal(messageDepart);
				
				// on récupère un attribut privé de ServiceChat
				Field field;
				field = service.getClass().getDeclaredField("canaux");
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				Optional<Hashtable<String, CanalChat>> canaux = Optional.of((Hashtable<String, CanalChat>) field.get(service));
				
				if(canaux.isPresent()) {
					client.changeCanal(canaux.get().get(canalAjoute));
					
					// création d'un mock temporaire pour prévoir l'action d'ajout d'un client dans un canal
					ClientChat clientTmp = EasyMock.createMock(ClientChat.class);
					EasyMock.expect(clientTmp.donneId()).andReturn(clientNom).times(2);
					EasyMock.replay(clientTmp);
					canaux.get().get(canalAjoute).ajouteClient(clientTmp);
					EasyMock.verify(clientTmp);
				} else
					fail("Erreur lors de la récupération de l'attribut privé de ServiceChat");
				
				EasyMock.expect(this.client.donneSurnom()).andReturn(clientNom);
				EasyMock.expect(this.client.donneCanal()).andReturn(new CanalChat(canalAjoute));

				String messageArrivee = String.format(ProtocoleChat.MESSAGE_ARRIVEE_CANAL, clientNom, canalAjoute);
				client.envoieCanal(messageArrivee);
				
				EasyMock.replay(client);
				service.changeCanalClient(client, canalAjoute);
				EasyMock.verify(client);
				
			} else {
				String canalNonExistant = "canalNonExistant";
				
				String message = String.format(ProtocoleChat.MESSAGE_NON_EXISTENCE_CANAL_DEMANDE, canalNonExistant);
				client.envoieMessage(message);
				
				EasyMock.replay(client);
				service.changeCanalClient(client, canalNonExistant);
				EasyMock.verify(client);
			}	
			
		} catch(IllegalArgumentException e) {
			fail("Erreur lors de la conversion d'un argument. Invalide");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test permettant de simuler la fermeture d'une connexion vers un client.
	 * Cette méthode est appelée lorsqu'un client se déconnecte ou qu'il envoi le signal
	 * de déconnexion au serveur ('/q').
	 * La méthode testée est 'fermeConnexion'.
	 */
	@Test
	public void testFermeConnexion() {
		try {
			ServiceChat service = new ServiceChat("default", 12345);
			String surnom = "surnom";
			CanalChat canal = new CanalChat("merlin");
			
			EasyMock.expect(client.donneId()).andReturn("idClient").times(4);
			EasyMock.expect(client.donneCanal()).andReturn(canal);
			
			client.envoieMessage(ProtocoleChat.MESSAGE_DEPART_CLIENT);
			
			EasyMock.expect(client.donneSurnom()).andReturn(surnom);
			
			client.envoieContacts(String.format(ProtocoleChat.MESSAGE_DEPART_CHAT, surnom));
			client.fermeConnexion();
			
			EasyMock.replay(client);
			
			canal.ajouteClient(client);
			service.fermeConnexion(client);
			
			EasyMock.verify(client);
		} catch (Exception e) {
			fail("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
