package camix;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Classe principale du programme Camix. 
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 *
 */
public final class Camix
{
	/**
	 * Fichier de configuration du serveur.
	 */
	public static final ResourceBundle CONFIGURATION = ResourceBundle.getBundle("Configuration");

	/**
	 * Constructeur privé de Camix.
	 * 
	 * Ce constructeur privé assure la non-instanciation de Camix dans un programme.
	 * (Camix est la classe principale du programme Camix)
	 */
	private Camix() 
	{
		// Constructeur privé pour assurer la non-instanciation de Camix.
	}

	/**
	 * Main du programme.
	 *
	 * <p>
	 * Cette fonction main lance le programme serveur qui consiste à :
	 * <ul>
	 * <li> Ouvrir le service de chat.
	 * <li> Gérer le chat.
	 * </ul>
	 * </p>
	 *
	 * @param args aucun argument attendu.
	 *
	 */
	public static void main(String[] args)
	{	
		System.out.println("Camix v3.0.etu");

		try {
			// Création et lancement du service.
			ServiceChat service = new ServiceChat(
				Camix.CONFIGURATION.getString("CANAL_PAR_DEFAUT").trim(),
				Integer.parseInt(Camix.CONFIGURATION.getString("PORT_SERVICE_CHAT").trim())
			);
			service.lanceService();
			
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

}
