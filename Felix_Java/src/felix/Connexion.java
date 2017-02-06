package felix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Classe de connexion au chat. 
 * 
 * @version 2.0.etu
 * @author Matthias Brun
 * 
 */
public class Connexion
{
	/**
	 * Encodage du protocole.
	 */
	public static final String ENCODAGE = "UTF8";
	
	/**
	 * Socket de connexion au chat.
	 */
	private Socket socket;

	/**
	 * Buffer d'écriture sur la socket de connexion au chat.
	 */
	private BufferedWriter bufferEcriture;

	/**
	 * Buffer de lecture de la socket de connexion au chat.
	 */
	public BufferedReader bufferLecture;

	/**
	 * Constructeur de la connexion au chat.
	 *
	 * @param adresse l'adresse du serveur du chat.
	 * @param port le port de connexion au chat.
	 *
	 * @throws IOException les exceptions d'entrée/sortie.
	 */
	public Connexion(String adresse, Integer port) throws IOException
	{
		try {
			// Initialisation de la socket.
			this.socket = new Socket(adresse, port);
		
			// Initialisation des buffers de lecture et d'écriture sur la socket de communication.
			this.bufferLecture = new BufferedReader(
					new InputStreamReader(this.socket.getInputStream(), Connexion.ENCODAGE));
			this.bufferEcriture = new BufferedWriter(
					new OutputStreamWriter(this.socket.getOutputStream(), Connexion.ENCODAGE));
		} 
		catch (IOException ex) {
			System.err.println("Problème de connexion au chat.");
			throw ex;
		}
	}

	/**
	 * Envoi d'un message au chat.
	 *
	 * @param message le message à envoyer au chat.
	 *
	 * @throws IOException les exceptions d'entrée/sortie.
	 */
	public void ecrire(String message) throws IOException
	{
		try {
			this.bufferEcriture.write(message, 0, message.length());
			this.bufferEcriture.newLine();
			this.bufferEcriture.flush();
		}
		catch (IOException ex) {
			System.err.println("Problème d'envoi de message au chat.");
			throw ex;
		}
	}

	/**
	 * Réception d'un message du chat.
	 * 
	 * @return le message provenant du chat.
	 *
	 * @throws IOException les exceptions d'entrée/sortie.
	 *
	 */
	public String lire() throws IOException
	{
		String message = null;

		try {
			message = this.bufferLecture.readLine();

			if (message == null) {
				// si le message est null alors la connexion n'est plus établie. On ferme donc la connexion et on engendre une exception qui permettra la fermeture de la fenêtre de chat
				this.ferme();
				throw new EOFException();
			}
		} 
		catch (EOFException ex) {
			System.out.println("Fin de connexion avec le serveur de chat");
			throw ex;
		}
		catch (IOException ex) {
			System.err.println("Problème de lecture d'un message du chat.");
			throw ex;
		}
		return message;
	}

	/**
	 * Fermeture de la connexion.
	 */
	public void ferme()
	{
		try {
			this.bufferEcriture.close();
			
		}
		catch (IOException ex) {
			System.err.println("Problème de fermeture du buffer d'écriture sur la socket.");
			System.err.println(ex.getMessage());
		}
	}		
	
}

