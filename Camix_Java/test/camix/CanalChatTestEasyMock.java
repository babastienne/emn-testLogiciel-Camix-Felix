package camix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test du CanalChat
 * 
 * @version 1.0
 * @author bpotiron
 *
 */
public class CanalChatTestEasyMock {

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
	 * Méthode de test. Celle-ci test l'ajout d'un client (emulé par un mock)
	 * puis l'ajout du même client une seconde fois (impossible selon la 
	 * conception du programme). 
	 */
	@Test
	public void testAjouteClient() {
		CanalChat canal = new CanalChat("Canal de test pour ajout de client");
		
		String id = "Client1";
		
		EasyMock.expect(this.client.donneId()).andReturn(id);
		EasyMock.expectLastCall().times(4);
		
		EasyMock.replay(this.client);
		
		// on ajoute un client. On verifie ensuite que le canal possède un client (et qu'il s'agit bien de celui ajouté)
		try {
			canal.ajouteClient(this.client);
		} catch(Exception e) {
			fail("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
		
		assertEquals(canal.donneNombreClients(), new Integer(1));
		assertTrue(canal.estPresent(this.client));
		
		
		// on verifie qu'on ne peut ajouter deux fois le meme client
		try {
			canal.ajouteClient(this.client);
		} catch(Exception e) {
			fail("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
		
		assertEquals(canal.donneNombreClients(), new Integer(1));
		
		EasyMock.verify(this.client);
	}
}
