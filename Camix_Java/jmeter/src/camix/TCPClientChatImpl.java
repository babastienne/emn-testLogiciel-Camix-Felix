package camix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.jmeter.protocol.tcp.sampler.TCPClient;

/**
 * Classe du client TCP à utiliser avec JMeter.
 * 
 * <p>Peut être utilisé pour tester le serveur chat Camix v3.0.etu 
 * implémenté avec BufferedReader et BufferedWriter.</p>
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 *
 */
public class TCPClientChatImpl implements TCPClient
{
	/**
	 * Encodage pour la communication.
	 */
	private static final String ENCODAGE = "UTF8";
	
	/** 
	 * La chaîne de fin-de-ligne.
	 */
	private String eol;
	
	/**
	 * Constructeur d'un client TCP à utiliser avec JMeter.
	 */
	public TCPClientChatImpl()
	{
		super();
		this.eol = System.getProperty("line.separator");
	}


	/**
	 * Méthode invoquée au démarrage du thread (JMeter > 2.3.2).
	 */
	public void setupTest() 
	{
		// rien à faire.
	}
	
	/**
	 * Méthode invoquée à la terminaison du thread (JMeter > 2.3.2).
	 */
	public void teardownTest() 
	{
		// rien à faire.
	}

	/**
	 * Écrire une chaîne de caractères sur un flux d'écriture.
	 *
	 * <p>Une chaîne vide ne sera pas écrite.</p>
	 * <p>La chaîne écrite s'arrête avant le premier retour à la ligne (<tt>\n</tt>),
	 * si un retour à la ligne est présent.</p>
	 * <p>Remarque : l'envoi d'un chaîne vide est ainsi possible en saisissant seulement un retour à la ligne.</p> 
	 * 
	 * @param os le flux d'écriture de la socket. 
	 * @param s la chaîne de caractères.
	 */
	public void write(OutputStream os, String s) 
	{
		try {
			if (s.length() != 0) {
				final BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(os, TCPClientChatImpl.ENCODAGE));

				// Le message s'arrête avant le dernier retour à la ligne (par exemple \n, \r ou \r\n, etc.), 
				// si un retour à la ligne termine le message.
				int limite = s.length();
				if (s.endsWith(this.eol)) {
					limite = s.indexOf(this.eol, s.length() - this.eol.length());
				}
				buffer.write(s, 0, limite);
				buffer.newLine();
				buffer.flush();
			}
		}
		catch (IOException ex) {
			System.err.println("Problème d'écriture sur le flux.");
			System.err.println(ex.getMessage());
		}
	}


 	/**
	 * Lire des chaînes de caractères sur un flux de lecture.
	 *
	 * <p>La lecture se fait jusqu'à la levée d'une interruption d'expiration (timeout, sortie attendue)
	 * ou que la lecture retourne <tt>null</tt> (coupure de connexion).</p>
	 * 
	 * @param is le flux de lecture de la socket. 
	 * @return s la chaîne de caractères.
	 */
	public String read(InputStream is)
	{
		BufferedReader buffer;
		String s = "";
		
		try {
			buffer = new BufferedReader(new InputStreamReader(is, TCPClientChatImpl.ENCODAGE));

			String sr; 

			while ((sr = buffer.readLine()) != null) {
				s = s.concat(sr.concat(this.eol));
			}
		}
		catch (InterruptedIOException ex) {
			// Sortie attendue, par interruption de l'expiration (timeout) (JDK 1.3).
			// Remarque : 
			// Utilisation possible de SocketTimeoutException 
			// (extends InterruptedIOException) pour JDK 1.4.
			System.out.print("[info] TCPClientChat : Interruption d'expiration sur le flux ");
			System.out.println("(" + ex.getMessage() + ")");
		}
		catch (UnsupportedEncodingException ex) {
			System.err.println("Problème de lecture sur le flux, encodage UTF8 non supporté.");
			System.err.println(ex.getMessage());
		}
		catch (IOException ex) {
			System.err.println("Problème de lecture sur le flux.");
			System.err.println(ex.getMessage());
		}
		return s;
	}


	/**
	 * Transférer un flux de lecture sur un flux d'écriture.
	 * 
	 * @param os le flux d'écriture d'une socket. 
	 * @param is le flux de lecture d'une socket.
	 */
	public void write(OutputStream os, InputStream is)
	{
		// non implémentée.
	}

	/**
	 * Donne le caractère de fin-de-ligne/fin-de-message (non utilisé dans notre cas).
	 *
	 * @return le caractère eolByte (0 dans notre cas).
	 */
	public byte getEolByte()
	{
		return 0;
	}

	/**
	 * Fixe le caractère de fin-de-ligne/fin-de-message (non utilisé dans notre cas).
	 *
	 * @param eolByte le caractère à fixer.
	 */
	public void setEolByte(int eolByte)
	{
		// non utilisé.
	}

	/**
	 * Donne l'encodage (non utilisé dans notre cas).
	 * 
	 * @return l'encodage (null dans notre cas).
	 */
	public String getCharset() 
	{
		return null;
	}
}
