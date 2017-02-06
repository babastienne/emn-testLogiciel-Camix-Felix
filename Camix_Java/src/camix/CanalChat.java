package camix;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Classe canal du serveur. 
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 * 
 */
public class CanalChat
{
	/**
	 * Le nom du canal.
	 */
	private String nom;

	/**
	 * L'ensemble des clients du canal.
	 */
	private Hashtable<String, ClientChat> clients;

	/**
	 * Accesseur du nom du canal.
	 *
	 * @return le nom du canal.
	 */
	public String donneNom()
	{
		return this.nom;
	}

	/**
	 * Donne le nombre de clients dans le canal.
	 *
	 * @return le nombre de cliants dans le canal.
	 */
	public Integer donneNombreClients()
	{
		return this.clients.size();
	}

	/**
	 * Informe de la présence d'un client dans le canal.
	 *
	 * @param client le client concerné.
	 * @return 'true' si le client est présent, 'false' sinon.
	 */
	public Boolean estPresent(ClientChat client)
	{
		return this.clients.get(client.donneId()) != null;
	}

	/**
	 * Constructeur d'un canal du chat.
	 *
	 * @param nom le nom du canal.
	 *
	 */
	public CanalChat(String nom)
	{
		this.nom = nom;
		this.clients = new Hashtable<String, ClientChat>();
	}

	/**
	 * Ajout d'un client dans le canal.
	 *
	 * @param client le client à ajouter dans le canal.
	 */
	public void ajouteClient(ClientChat client)
	{
		// Si le client n'est pas déjà dans le canal.
		if (!this.estPresent(client)) {
			this.clients.put(client.donneId(), client);
		}
	}

	/**
	 * Suppression d'un client dans le canal.
	 *
	 * @param client le client à enlever du canal.
	 */
	public void enleveClient(ClientChat client)
	{
		// Si le client est dans le canal.
		if (this.estPresent(client)) {
			this.clients.remove(client.donneId());
		}
	}

	/**
	 * Envoi d'un message sur le canal.
	 *
	 * @param message le message à envoyer.
	 *
	 */
	public void envoieClients(String message)
	{
		// Pour chaque client du canal.
		final Iterator<String> iter = this.clients.keySet().iterator();

		while (iter.hasNext()) {
			final ClientChat contact = this.clients.get(iter.next());

			// Envoi du message.
			contact.envoieMessage(message);
		}
	}
	
	/**
	 * Envoi d'un message aux contacts d'un client sur le canal.
	 *
	 * <p>Le message n'est pas envoyé au client mais seulement à ses contacts sur le canal.</p>
	 * 
	 * @param client le client concerné.
	 * @param message le message à envoyer.
	 *
	 */
	public void envoieContacts(ClientChat client, String message)
	{
		// Pour chaque client du canal.
		final Iterator<String> iter = this.clients.keySet().iterator();

		while (iter.hasNext()) {
			final ClientChat contact = this.clients.get(iter.next());

			if (!contact.equals(client)) {
				// Envoi du message si le contact n'est pas le client.
				contact.envoieMessage(message);
			}
		}
	}

}
