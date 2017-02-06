package felix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Classe de la fenetre de connexion de felix
 * 
 * @version 1.0
 * @author Bastien Potiron
 *
 */
public class FenetreConnexion extends JFrame implements ActionListener {
	
	/**
	 * UID Auto-généré
	 */
	private static final long serialVersionUID = -8163510254444097402L;
	
	/**
	 * Le champ de saisi de l'adresse IP du serveur
	 */
	private JTextField txtIpDuFichier;
	
	/**
	 * Expression régulière permettant de vérifier la bonne syntaxe de l'adresse IP rentrée
	 */
	private static final Pattern PATTERN_VERIFICATION_IP = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	/**
	 * Le champ de saisie du port du serveur
	 */
	private JTextField txtPortDuFichier;
	
	/**
	 * Le bouton pour lancer la connexion au serveur
	 */
	private JButton btnConnexionAuChat;
	
	/**
	 * Le champ d'information de connexion
	 */
	private JLabel labelInformationConnexion;
	
	/**
	 * Message de connexion au chat
	 */
	private static final String MESSAGE_CONNEXION = Felix.MESSAGES.getString("MESSAGE_CONNEXION");
	
	/**
	 * Message initialement visible sur la fenetre de connexion
	 */
	private static final String MESSAGE_INITIAL = Felix.MESSAGES.getString("MESSAGE_INITIAL");
	
	/**
	 * Message informant que l'adresse IP rentrée ne correspond pas aux
	 * critères de la norme IPv4.
	 */
	private static final String ADRESSE_IP_INVALIDE = Felix.MESSAGES.getString("ADRESSE_IP_INVALIDE");
	
	/**
	 * Connexion au chat
	 */
	private Connexion connexion;
	
	/**
	 * Fenetre de chat qui sera lancée si la connexion aboutie
	 */
	private Fenetre fenetre;
	
	/**
	 * Constructeur pour la fenêtre de connexion. C'est celle-ci qui permettra
	 * la connexion à un serveur de chat. 
	 * @param largeur largeur de la fenêtre pour la construction
	 * @param hauteur hauteur de la fenêtre pour la construction
	 * @param titre titre de la fenêtre de connexion
	 */
	public FenetreConnexion(Integer largeur, Integer hauteur, String titre) {
		// Construction de la fenêtre.
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(largeur, hauteur);

		setTitle(titre);
		setResizable(false);
		
		// construction de la fenetre
		construireFenetre();
	}

	/**
	 * Méthode de création de la fenetre. Les elements sont instanciés et
	 * les champs prenant des valeurs sont complétés. 
	 */
	private void construireFenetre() {
		getContentPane();
		
		JLabel lblIp = new JLabel("IP :");
		lblIp.setBounds(44, 35, 30, 26);
		getContentPane().add(lblIp);
		
		txtIpDuFichier = new JTextField();
		txtIpDuFichier.setBounds(86, 36, 158, 25);
		txtIpDuFichier.setText(
				Felix.CONFIGURATION.getString("ADRESSE_CHAT"));
		getContentPane().add(txtIpDuFichier);
		txtIpDuFichier.setColumns(10);
		
		txtPortDuFichier = new JTextField();
		txtPortDuFichier.setText(
				Felix.CONFIGURATION.getString("PORT_CHAT"));
		txtPortDuFichier.setColumns(10);
		txtPortDuFichier.setBounds(86, 85, 158, 25);
		getContentPane().add(txtPortDuFichier);
		
		JLabel lblPort = new JLabel("Port :");
		lblPort.setBounds(34, 84, 40, 26);
		getContentPane().add(lblPort);
		
		btnConnexionAuChat = new JButton(MESSAGE_CONNEXION);
		// ajout d'un action listener sur le bouton de connexion
		btnConnexionAuChat.addActionListener(this);
		btnConnexionAuChat.setBounds(57, 141, 158, 23);
		getContentPane().add(btnConnexionAuChat);
		
		labelInformationConnexion = new JLabel(MESSAGE_INITIAL);
		labelInformationConnexion.setBounds(12, 177, 258, 43);
		getContentPane().add(labelInformationConnexion);		
	}
	
	/**
	 * Méthode vérifiant que l'adresse IP rentrée est valable (selon la description IPv4)
	 * @param s l'adresse IP a vérifier
	 * @return true si l'adresse correspond à une adresse IPv4 valable. False sinon.
	 */
	private boolean verificationAdresseIP(String s) {
		return PATTERN_VERIFICATION_IP.matcher(s).matches();
	}

	/**
	 * Méthode appelée lors de la tentative de connexion au serveur
	 * @throws IOException
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		labelInformationConnexion.setText(MESSAGE_CONNEXION + " @" + txtIpDuFichier.getText() + ":" + txtPortDuFichier.getText());
		
		if(!verificationAdresseIP(txtIpDuFichier.getText())) {
			System.err.println(ADRESSE_IP_INVALIDE);
			labelInformationConnexion.setText(ADRESSE_IP_INVALIDE);
		} else {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					lancementChat();
				}
			}).start();
			
		}
	}	
	
	/**
	 * Methode de lancement du chat (utilisé dans le thread lors du clic sur le bouton
	 * de connexion). En cas de non connexion, la fenetre de chat n'est pas affichée
	 * et un message indique à l'utilisateur l'impossibilité de se connecter.
	 */
	private void lancementChat() {
		try {
			connexion = new Connexion(
					txtIpDuFichier.getText(),
					Integer.parseInt(txtPortDuFichier.getText()));
			
			fenetre = new Fenetre(
					Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_LARGEUR")),
					Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR")),
					Felix.CONFIGURATION.getString("FENETRE_TITRE"),
					connexion,
					this
				);
			
			this.setVisible(false);
			
			fenetre.setVisible(true);
			
			labelInformationConnexion.setText(MESSAGE_INITIAL);
		} catch (NumberFormatException | IOException e) {
			String MESSAGE_ERREUR_CONNEXION = MESSAGE_CONNEXION + " @" + txtIpDuFichier.getText() + ":" + txtPortDuFichier.getText() + " impossible.";
			
			System.err.println(MESSAGE_ERREUR_CONNEXION);
			labelInformationConnexion.setText(MESSAGE_ERREUR_CONNEXION);
		} catch (Exception e) {
			// permet de prévenir certaines erreurs type mauvais port (ex : -5218) pour la connexion.
			String MESSAGE_ERREUR_CONNEXION = MESSAGE_CONNEXION + " @" + txtIpDuFichier.getText() + ":" + txtPortDuFichier.getText() + " impossible.";
			
			System.err.println(MESSAGE_ERREUR_CONNEXION + "\nOrigine de l'erreur inconnue.");
			labelInformationConnexion.setText(MESSAGE_ERREUR_CONNEXION);
		}
		
	}

	/**
	 * Fermer la fenêtre de la vue
	 */
	public void ferme() {
		if(this.fenetre != null)
			this.fenetre.dispose();
		this.dispose();		
	}
	
}
