package camix;

/**
 * Classe du protocole de communication du chat. 
 * 
 * @version 3.0.etu
 * @author Matthias Brun
 *
 */
public final class ProtocoleChat
{
	/**
	 * Encodage du protocole.
	 */
	public static final String ENCODAGE = "UTF8";
	
	/**
	 * Taille d'une commande (en caractères).
	 */
	private static final int TAILLE_COMMANDE = 2;
	
	/**
	 * Caractère de début de commande.
	 */
	public static final char COMMANDE_CARACTERE = '/';
	
	/**
	 * Commande de changement de surnom d'un client du chat.
	 */
	public static final char COMMANDE_CHANGE_SURNOM_CLIENT = 'n';

	/**
	 * Commande de changement de canal d'un client.
	 */
	public static final char COMMANDE_CHANGE_CANAL_CLIENT = 'c';

	/**
	 * Commande d'ajout d'un canal dans le chat.
	 */
	public static final char COMMANDE_AJOUTE_CANAL = 'a';

	/**
	 * Commande de suppression d'un canal dans le chat.
	 */
	public static final char COMMANDE_SUPPRIME_CANAL = 'r';

	/**
	 * Commande d'affichage des canaux disponibles dans le chat.
	 */
	public static final char COMMANDE_AFFICHE_CANAUX = 'l';

	/**
	 * Commande d'affichage des informations personnelles du client.
	 */
	public static final char COMMANDE_AFFICHE_CLIENT = '?';

	/**
	 * Commande d'affichage de l'aide sur les commandes disponibles.
	 */
	public static final char COMMANDE_AFFICHE_AIDE = 'h';
	
	/**
	 * Commande de déconnexion d'un client du chat.
	 */
	public static final char COMMANDE_DECONNEXION_CHAT = 'q';
	
	/**
	 * Message à transmettre dans le chat de la part d'un client.
	 * 
	 * <p><ul>
	 * <li>Premier %s : Renseigné par le nom du client lors de l'utilisation du message.
	 * <li>Premier %s : Renseigné par le message à transmettre lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_PREFIXE_MESSAGE = "%s > %s";
	
	/**
	 * Message d'arrivée d'un client dans le chat.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal par défaut à lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_ARRIVEE_CHAT = "* Un nouveau client est dans le chat (%s).%n";
	
	/**
	 * Message d'accueil d'un client dans le chat.
	 */
	public static final String MESSAGE_ACCUEIL_CHAT = "* Taper /" + ProtocoleChat.COMMANDE_AFFICHE_AIDE 
						+ " pour avoir de l'aide sur les commandes du chat.%n";
	
	/** 
	 * Message de départ d'un client du chat.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du client lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_DEPART_CHAT = "* %s quitte le chat.%n";
	
	/**
	 * Message qui informe un client de son départ du serveur de chat.
	 */
	public static final String MESSAGE_DEPART_CLIENT = "* Sortie du chat.";
	
	/**
	 * Message de changement de surnom d'un client.
	 * 
	 * <p><ul>
	 * <li>Premier %s : Renseigné par l'ancien surnom du client lors de l'utilisation du message.
	 * <li>Deuxième %s : Renseigné par le nouveau surnom du client lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_CHANGEMENT_SURNOM = "* %s devient %s.%n";
	
	/**
	 * Message de départ d'un client d'un canal.
	 * 
	 * <p><ul>
	 * <li>Premier %s : Renseigné par le nom du client lors de l'utilisation du message.
	 * <li>Deuxième %s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_DEPART_CANAL = "* %s quitte le canal %s.%n";
	
	/**
	 * Message d'arrivée d'un client dans un canal.
	 * 
	 * <p><ul>
	 * <li>Premier %s : Renseigné par le nom du client lors de l'utilisation du message.
	 * <li>Deuxième %s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_ARRIVEE_CANAL = "* %s rejoint le canal %s.%n";
	
	/**
	 * Message de non existence d'un canal demandé (lors d'un changement de canal).
	 */
	public static final String MESSAGE_NON_EXISTENCE_CANAL_DEMANDE = "* Le canal demandé n'existe pas.%n";
	
	/**
	 * Message de création de canal.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_CREATION_CANAL = "* Le canal %s a été créé.%n";
	
	/**
	 *  Message d'impossibilité de création de canal (le canal existe déjà).
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_CREATION_IMPOSSIBLE_CANAL = "* Le canal %s existe déjà.%n";

	/**
	 * Message de suppression d'un canal.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_SUPPRESSION_CANAL = "* Le canal %s a été supprimé.%n";
	
	/**
	 * Message d'impossibilité de suppression d'un canal non vide.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_SUPPRESSION_CANAL_NON_VIDE = "* Le canal %s n'est pas vide.%n";

	/**
	 * Message d'impossibilité de suppression d'un canal inexistant.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_SUPPRESSION_CANAL_INEXISTANT = "* Le canal %s n'existe pas.%n";
	
	/**
	 * Message d'impossibilité de supprimer le canal par défaut du chat.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal par défaut lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_SUPPRESSION_CANAL_PAR_DEFAUT 
		= "* Impossible de supprimer le canal par défaut du chat (%s).%n";

	/**
	 * Message d'en-tête des canaux disponibles.
	 */
	public static final String MESSAGE_CANAUX_DISPONIBLES_EN_TETE = "* Canaux disponibles : %n";
	
	/**
	 * Message d'affichage d'un canal disponible.
	 * 
	 * <p><ul>
	 * <li>%s : Renseigné par le nom du canal lors de l'utilisation du message.
	 * <li>%d : Renseigné par le nombre d'utilisateurs du canal lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_CANAUX_DISPONIBLES_CANAL = "   - %s (%d)%n";
	
	/**
	 * Message des informations personnelles d'un client.
	 * 
	 * <p><ul>
	 * <li>Premier %s : Renseigné par le surnom du client lors de l'utilisation du message.
	 * <li>Deuxième %s : Renseigné par le nom du canal du client lors de l'utilisation du message.
	 * </ul></p>
	 */
	public static final String MESSAGE_INFORMATIONS_PERSONNELLES = "* Informations personnelles : %n"
																	+ "   Surnom : %s ;%n"
																	+ "   Canal  : %s ;%n";
	
	/**
	 * Espacement pour le message d'aide.
	 */
	private static final String ESPACE_MESSAGE_AIDE = "   ";
	
	/**
	 * Message d'aide sur les commandes du chat.
	 */
	public static final String MESSAGE_AIDE = "* Commandes disponibles : %n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE 
			+ ProtocoleChat.COMMANDE_CHANGE_SURNOM_CLIENT + " : changer de surnom ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_CHANGE_CANAL_CLIENT + " : changer de canal ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_AFFICHE_CANAUX + " : afficher les canaux ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_AJOUTE_CANAL + " : créer un canal ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_SUPPRIME_CANAL + " : supprimer un canal ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_AFFICHE_CLIENT + " : afficher ses informations ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_DECONNEXION_CHAT + " : deconnexion du chat ;%n"
			
			+ ProtocoleChat.ESPACE_MESSAGE_AIDE + ProtocoleChat.COMMANDE_CARACTERE
			+ ProtocoleChat.COMMANDE_AFFICHE_AIDE + " : afficher l'aide ;%n";
	
	
	/**
	 * Constructeur privé d'un protocole de communication du chat.
	 * 
	 * Ce constructeur privé assure la non-instanciation d'un
	 * protocole de communication dans un programme
	 * (le protocole n'offre que des attributs et des méthodes statiques).
	 */
	private ProtocoleChat() 
	{
		// Constructeur privé pour assurer la non-instanciation d'un protocole de communication.
	}

	
	/**
	 * Valide qu'un message respecte bien le format d'une commande.
	 *
	 * @param message le message à analyser.
	 * 
	 * @return vrai (<i>true</i>) si le message est une commande, faux (<i>false</i>) sinon.
	 */
	public static final Boolean estUneCommande(String message)
	{
		return (message.length() > 1) && (message.charAt(0) == ProtocoleChat.COMMANDE_CARACTERE);
	}
	
	/**
	 * Donne la commande d'un message.
	 * 
	 * <p>
	 * La commande est identifiée par le second caractère d'un message
	 * (le premier caractère étant ProtocoleChat.COMMANDE_CARACTERE).
	 * </p>
	 * 
	 * @param message le message d'où extraire la commande.
	 * 
	 * @return la commande du message.
	 */
	public static final char commandeDuMessage(String message)
	{
		return message.charAt(1);
	}
	
	/**
	 * Retourne le paramètre d'une commande au serveur.
	 * 
	 * @param commande la commande concernée.
	 * @return le paramètre de la commande.
	 *
	 */
	public static final String parametreCommande(String commande)
	{
		String parametre = "";

		if (commande.length() > ProtocoleChat.TAILLE_COMMANDE) {
			parametre = commande.substring(ProtocoleChat.TAILLE_COMMANDE + 1, commande.length());
		}
		return parametre;			
	}
}

