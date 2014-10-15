import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe <B>Un_Guetteur</B> va guetter sur le port de guet les demandes de connexions des clients. Il va les retransmettre au serveur qui les acceptera si il lui reste des places libres dans le tableau des canaux de connexions clients.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 1.2
 * @see Un_Serveur
 * @see Un_Canal_Connexion
 */
public class Un_Guetteur implements Runnable {

    private int Mon_Port_Guet;
    private ServerSocket Ma_Socket_Guet;
    private Socket Ma_Socket_Connexion;
    private boolean Ma_Connexion_Demandee, Mon_Guetteur_Pret;
    private Thread Mon_Thread;

    /**
     * Le constructeur de la classe <B>Un_Guetteur</B> va initialiser la <B>ServerSocket</B> de guet sur le port passe en parametre.
     *
     * @param Tel_Port_Guet - int representant le port de guet.
     */
    public Un_Guetteur(int Tel_Port_Guet) {
        Mon_Port_Guet = Tel_Port_Guet;
        Mon_Guetteur_Pret = false;
        Mon_Thread = null;
        try {
            System.err.println("Un_Guetteur : Creation de la socket de guet...");
            Ma_Socket_Guet = new ServerSocket(Mon_Port_Guet);
            Mon_Guetteur_Pret = true;
            Ma_Connexion_Demandee = false;
        } catch (IOException Telle_E) {
            System.err.println("Un_Guetteur : Erreur lors de l'ouverture du guetteur...");
            return;
        }
    }

    /**
     * La methode <B>guetteurPret</B> sert a indiquer que le guetteur est pret a accepter les demandes de connexions.
     *
     * @return true si le guetteur a ete correctement initialise et est en attente, false sinon.
     * @see Un_Serveur
     */
    public boolean guetteurPret() {
        return Mon_Guetteur_Pret;
    }

    /**
     * La methode <B>connexionDemandee</B> sert au serveur a demander au guetteur si une nouvelle connexion a ete demandee.
     *
     * @return true si une connexion est demandee, false sinon.
     * @see Un_Serveur
     */
    public boolean connexionDemandee() {
        return Ma_Connexion_Demandee;
    }

    /**
     * La methode <B>connexionAccepte</B> sert a indiquer au guetteur que le serveur a gere la demande de connexion et qu'il peut se remettre en attente.
     *
     * @see Un_serveur
     */
    public void connexionAcceptee() {
        Ma_Connexion_Demandee = false;
    }

    /**
     * La methode <B>donneSocketConnexion</B> sert au serveur a recuperer la <B>Socket</B> avec laquelle il va initialiser le canal client pour la nouvelle connexion.
     *
     * @return La <B>Socket</B> avec laquelle va s'effectuer la connexion reseau avec le nouveau client.
     * @see Un_Serveur
     * @see Un_Canal_Connexion
     */
    public Socket donneSocketConnexion() {
        return Ma_Socket_Connexion;
    }

    /**
     * La methode <B>starts</B> de l'interface <B>Runnable</B> permet de faire passer le guetteur en <B>Thread</B> des la fin de son initialisation.
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            System.err.println("Un_Guetteur : Guetteur en tache de fond...");
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> contient la boucle permanente d'attente des demandes de connexion sur le port de guet.
     */
    public synchronized void run() {
        while (Mon_Thread != null)
            try {
                Mon_Thread.sleep(50);
                Ma_Socket_Connexion = null;
                System.err.println("Un_Guetteur : Attente d'une nouvelle connexion...");
                Ma_Socket_Connexion = Ma_Socket_Guet.accept();
                System.err.println("Un_Guetteur : Nouvelle connexion demandee...");
                Ma_Connexion_Demandee = true;
                while (Ma_Connexion_Demandee) ;
            } catch (Exception Telle_E) {
                System.err.println("Un_Guetteur : Erreur lors de l'etablissement de la connexion...");
                return;
            }
    }

    /**
     * La methode <B>stop</B> de l'interface <B>Runnable</B> permet d'arreter le guetteur a la fin de son execution.
     */
    public synchronized void stop() {
        if (Ma_Socket_Guet != null)
            try {
                System.err.println("Un_Guetteur : Fermeture de la socket de guet...");
                Ma_Socket_Guet.close();
                Ma_Socket_Guet = null;
            } catch (IOException Telle_E) {
                System.err.println("Un_Guetteur : Erreur lors de la fermeture du guetteur...");
                return;
            }
        if (Mon_Thread != null) {
            System.err.println("Un_Guetteur : Fermeture du guetteur...");
            Mon_Thread.stop();
            Mon_Thread = null;
        }
    }

}
