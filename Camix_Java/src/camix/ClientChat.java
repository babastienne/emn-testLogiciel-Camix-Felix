package camix;

import java.io.IOException;
import java.net.Socket;

/**
 * Classe client du serveur. 
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 * 
 */
public class ClientChat extends Thread 
{
	/**
	 * Identifiant du client.
	 */
	private String id;

	/**
	 * Le surnom du client.
	 */
	private String surnom;
	
	/**
	 * Le canal du client.
	 */
	private CanalChat canal;
	
	/**
	 * La connexion réseau avec le client.
	 */
	private ConnexionClient connexion;

	/**
	 * Service Chat du client.
	 */
	private ServiceChat chat;


	/**
	 * Accesseur à l'identifiant (interne) du client.
	 * 
	 * @return l'identifiant du client.
	 */
	public String donneId()
	{
		return this.id;
	}

	/**
	 * Accesseur du surnom du client.
	 *
	 * @return le surnom du client.
	 */
	public String donneSurnom()
	{
		return this.surnom;
	}

	/**
	 * Accesseur du canal du client.
	 *
	 * @return le canal du client.
	 */
	public CanalChat donneCanal()
	{
		return this.canal;
	}

	/**
	 * Constructeur d'un client du chat.
	 *
	 * @param chat le chat du client.
	 * @param socket la socket de connexion du client. 
	 * @param surnom le surnom du client.
	 * @param canal le canal du client.
	 *
	 * @throws IOException exception d'entrée/sortie.
	 *
	 */
	public ClientChat(ServiceChat chat, Socket socket, String surnom, CanalChat canal) throws IOException
	{
		super();
		
		this.chat = chat;
		this.surnom = surnom;
		this.canal = canal;
		
		try {
			// Création d'une connexion réseau avec le client.
			this.connexion = new ConnexionClient(socket);
			
			// Utilisation de la socket de connexion du client comme identifiant.
			this.id = socket.toString();
		}
		catch (IOException ex) {
			System.err.println("Problème de mise en place d'une gestion de client.");
			throw ex;
		}
		catch (NullPointerException e) {
			
		}		
	}

	/**
	 * Lancement du service à un client.
	 */
	public void lanceService()
	{
		this.start();
	}
	
	/**
	 * Point d'entrée du thread de service au client 
	 * (atteint via start() dans le lancement du service au client).
	 * 
	 * <p>
	 * Lecture de messages sur la socket de communication avec le client puis traitement du message.
	 * S'arrête quand le message est <tt>null</tt>. 
	 * </p>
	 */
	public void run()
	{
		try {
			while (true) {
				final String message = this.connexion.lire();
				if (message == null) {
					// Fermeture de la connexion par le client.
					break;
				}
				this.traiteMessage(message);
			}
		}
		catch (IOException ex) {
			System.err.println("Problème de gestion d'un client - id : " + this.id);
			System.err.println(ex.getMessage());
		} 
		finally {
			this.chat.fermeConnexion(this);
		}
	}
	
	/**
	 * Traitement d'un message envoyé par le client.
	 * 
	 * @param message le message à traiter.
	 */
	private void traiteMessage(String message)
	{
		if (ProtocoleChat.estUneCommande(message)) {
			
			switch (ProtocoleChat.commandeDuMessage(message)) {

				case ProtocoleChat.COMMANDE_CHANGE_SURNOM_CLIENT : 
					this.chat.changeSurnomClient(this, ProtocoleChat.parametreCommande(message));
					break;
				case ProtocoleChat.COMMANDE_CHANGE_CANAL_CLIENT : 
					this.chat.changeCanalClient(this, ProtocoleChat.parametreCommande(message)); 
					break;
				case ProtocoleChat.COMMANDE_AJOUTE_CANAL : 
					this.chat.ajouteCanal(this, ProtocoleChat.parametreCommande(message)); 
					break;
				case ProtocoleChat.COMMANDE_SUPPRIME_CANAL : 
					this.chat.supprimeCanal(this, ProtocoleChat.parametreCommande(message)); 
					break;
				case ProtocoleChat.COMMANDE_AFFICHE_CANAUX : 
					this.chat.afficheCanaux(this);
					break;
				case ProtocoleChat.COMMANDE_AFFICHE_CLIENT : 
					this.chat.afficheInformationsClient(this);
					break;
				case ProtocoleChat.COMMANDE_DECONNEXION_CHAT:
					this.chat.fermeConnexion(this);
					break;
				
				default : 
					this.chat.afficheAide(this);
					break;		
			}
		} else {
			// Si le message n'est pas une commande,
			// le message est à transmettre aux clients du canal de l'emetteur. 
			this.envoieCanal(String.format(ProtocoleChat.MESSAGE_PREFIXE_MESSAGE, this.donneSurnom(), message));
		}
	}
	
	
	/**
	 * Change le surnom du client.
	 * 
	 * @param surnom le nouveau surnom du client.
	 */
	public void changeSurnom(String surnom)
	{
		this.surnom = surnom;
	} 

	/**
	 * Change le canal du client.
	 * 
	 * @param canal le nouveau canal du client.
	 */
	public void changeCanal(CanalChat canal)
	{
		if (canal != null) {
			this.canal.enleveClient(this);
			this.canal = canal;
			this.canal.ajouteClient(this);
		}
	}

	/**
	 * Envoyer un message à un client.
	 * 
	 * @param message le message à envoyer.
	 *
	 */
	public void envoieMessage(String message)
	{
		try {
			this.connexion.ecrire(message);
		} 
		catch (IOException ex) { 
			System.err.println("Problème d'envoi d'un message à un client - id : " + this.id); 
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * Transmet un message à tous les contacts d'un client du chat (les clients du même canal).
	 *
	 * <p>Le message n'est pas envoyé au client mais seulement à ses contacts sur le canal.</p>
	 *
	 * @param message le message à envoyer.
	 *
	 */
	public void envoieContacts(String message)
	{
		// Synchronisation : 
		// Pour éviter qu'un client ne soit supprimé du canal lors de l'envoi.
		synchronized (this.canal) {
			this.canal.envoieContacts(this, message);
		}
	}
	
	/**
	 * Envoie d'un message sur le canal d'un client.
	 *
	 * @param message le message à envoyer.
	 *
	 */
	public void envoieCanal(String message)
	{
		// Synchronisation : 
		// Pour éviter qu'un client ne soit supprimé du canal lors de l'envoi.
		synchronized (this.canal) {
			this.canal.envoieClients(message);
		}
	}
	
	/**
	 * Fermeture de la connexion du client.
	 *
	 */
	public void fermeConnexion() 
	{
		// Synchronisation :
		// Pour éviter qu'une connexion soit fermée lors de l'envoi d'un message sur le canal du client.
		synchronized (this.canal) {
			// Suppression du client dans le canal.
			this.canal.enleveClient(this);
		
			// Fermeture de la connexion.
			this.connexion.ferme();
		}
	}
}
