package camix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Classe de connexion réseau avec un client. 
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 * 
 */
public class ConnexionClient
{	
	/**
	 * Socket de connexion avec le client.
	 */
	private Socket socket;

	/**
	 * Buffer d'écriture sur la socket de connexion.
	 */
	private BufferedWriter bufferEcriture;

	/**
	 * Buffer de lecture de la socket de connexion.
	 */
	private BufferedReader bufferLecture;
	
	/**
	 * Boolean permettant de savoir si la connexion doit toujours être active ou non
	 */
	private boolean shouldReadOrWite;
	
	/**
	 * Accesseur à la socket de la connexion.
	 * 
	 * @return la socket de la connexion.
	 */
	public Socket donneSocket()
	{
		return this.socket;
	}
	
	/**
	 * Constructeur de la connexion avec le client.
	 *
	 * @param socket la socket de connexion avec le client.
	 *
	 * @throws IOException exception d'entrée/sortie.
	 */
	public ConnexionClient(Socket socket) throws IOException
	{
		try {
			// Initialisation de la socket.
			this.socket = socket;

			// Initialisation des buffers de lecture et d'écriture sur la socket.
			this.bufferLecture = new BufferedReader(
					new InputStreamReader(this.socket.getInputStream(), ProtocoleChat.ENCODAGE));
			this.bufferEcriture = new BufferedWriter(
					new OutputStreamWriter(this.socket.getOutputStream(), ProtocoleChat.ENCODAGE));	
			
			this.shouldReadOrWite = true;
		} 
		catch (IOException ex) {
			System.err.println("Problème de connexion avec un client.");
			throw ex;
		}
	}

		
	/**
	 * Envoi d'un message au client.
	 *
	 * @param message le message à envoyer au client.
	 *
	 * @throws IOException exception d'entrée/sortie.
	 */
	public void ecrire(String message) throws IOException
	{
		if(this.shouldReadOrWite) {
			try {
				this.bufferEcriture.write(message, 0, message.length());
				this.bufferEcriture.newLine();
				this.bufferEcriture.flush();
			}
			catch (IOException ex) {
				if (!(ex instanceof SocketException || "Broken pipe".equals(ex.getMessage()))) {
					// Si l'exception n'est pas due à une coupure de connexion du client.
					System.err.println("Problème de connexion avec le client (envoi de message).");
					throw ex;
				}
			}
		}
	}

	/**
	 * Réception d'un message du client.
	 *
	 * @return le message provenant du client.
	 *
	 * @throws IOException exception d'entrée/sortie.
	 */
	public String lire() throws IOException
	{
		String message = null;
		if(this.shouldReadOrWite) {
			try {
				message = this.bufferLecture.readLine();
			}
			catch (IOException ex) {
				if (!(ex instanceof SocketException || "Connection reset".equals(ex.getMessage()))) {
					// Si l'exception n'est pas due à une coupure de connexion du client.
					System.err.println("Problème de connexion avec Felix (lecture de message).");
					throw ex;
				}
			}
		}
		
		return message;
	}		
	
	/**
	 * Fermeture de la connexion.
	 * 
	 * Fermeture des buffers de lecture et d'écriture et fermeture de la socket.
	 * 
	 */
	public void ferme() 
	{
		// Fermeture du buffer d'écriture.
		try {
			this.bufferEcriture.close();
		}
		catch (IOException ex) {
			System.err.println("Problème de fermeture de connexion - buffer écriture : " + this.bufferEcriture);
			System.err.println(ex.getMessage()); 
		}
		
		// Fermeture du buffer de lecture.
		try {
			this.bufferLecture.close();
		}
		catch (IOException ex) {
			System.err.println("Problème de fermeture de connexion - buffer lecture : " + this.bufferLecture);
			System.err.println(ex.getMessage()); 
		}

		// Fermeture de la socket.
		try {
			this.socket.close();
		} 
		catch (IOException ex) {
			System.err.println("Problème de fermeture de connexion - fermeture socket : " + this.socket);
			System.err.println(ex.getMessage()); 
		}
		this.shouldReadOrWite = false;
	}
}
