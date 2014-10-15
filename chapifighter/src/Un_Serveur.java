import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * La classe <B>Un_Serveur</B> est le tronc commun du serveur. Apres validation du numero de port de guet ( et eventuellement passage par une fenetre de saisie de numero de port ), elle possede :<BR>
 * - Un guetteur de demandes de connexions de type Un_Guetteur gerant les demandes de connexions d'eventuels nouveaux clients;<BR>
 * - Un tableau des canaux clients du type Un_Canal_Connexion[] gerant les connexions avec les clients sur le reseau;<BR>
 * - Un serveur de CHAT du type Un_Serveur_CHAT gerant les donnees du CHAT.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 1.2
 * @see Un_Guetteur
 * @see Un_Serveur_CHAT
 * @see Un_Canal_Connexion
 * @see Une_Fen_Saisie_No_Port
 */
public class Un_Serveur implements Runnable {

    private static final int MON_NB_POS_INIT = 3;
    private static final int MON_TAB_ANGLES_DEP[] = {0, 45, 270};
    private static final double MON_TAB_POS_X_DEP[] = {90.0, 352.0, 8 * 64 + 32};
    private static final double MON_TAB_POS_Y_DEP[] = {90.0, 352.0, 14 * 64 + 32};
    private static int MON_NB_MAX_CLIENTS;
    private Thread Mon_Thread = null;
    private int Mon_Port;
    private Un_Guetteur Mon_Guetteur;
    private Un_Serveur_CHAT Mon_Serveur_CHAT;
    private Un_Serveur_Jeu Mon_Serveur_Jeu;
    private Un_Canal_Connexion Mes_Canaux_Clients[];

    /**
     * Ce constructeur de la classe <B>Un_Serveur</B> prend en parametre une chaine de caracteres representant le numero de port de guet du serveur. Il genere une fenetre de saisie de numero de port si cette <B>String</B> est invalide.
     *
     * @param Tel_No_Port - String de 4 caracteres representant le port de guet.
     * @see Une_Fen_Saisie_No_Port
     */
    public Un_Serveur(String Tel_No_Port, int Tel_But_Jeu, int Tel_Nb_Max_Clients) {
        super();
        MON_NB_MAX_CLIENTS = Tel_Nb_Max_Clients;
        if ((Tel_No_Port.length() > 4) || (!initGuetteur(Tel_No_Port)))
            saisieNoPort();
        initCommune(Tel_But_Jeu);
    }

    /**
     * Ce constructeur de la classe <B>Un_Serveur</B> ne prend aucun parametre. Il genere une fenetre de saisie du numero de port et se bloque jusqu'a validation de la saisie.
     *
     * @see Une_Fen_Saisie_No_Port
     */
    public Un_Serveur() {
        super();
        saisieNoPort();
        initCommune(7);
    }

    void initCommune(int Tel_But_Jeu) {
        System.err.println("Un_Serveur : Guetteur cree...");
        System.err.println("Un_Serveur : Initialisation des " + MON_NB_MAX_CLIENTS + " canaux clients...");
        Mes_Canaux_Clients = new Un_Canal_Connexion[MON_NB_MAX_CLIENTS];
        for (int Le_Cpt = 0; Le_Cpt < MON_NB_MAX_CLIENTS; Le_Cpt++, System.err.print("*"))
            Mes_Canaux_Clients[Le_Cpt] = new Un_Canal_Connexion();
        System.err.println("\nUn_Serveur : Creation du serveur CHAT...");
        Mon_Serveur_CHAT = new Un_Serveur_CHAT(MON_NB_MAX_CLIENTS);
        System.err.println("\nUn_Serveur : Creation du serveur Jeu...");
        Mon_Serveur_Jeu = new Un_Serveur_Jeu(MON_NB_MAX_CLIENTS, Tel_But_Jeu);
        System.err.println("Un_Serveur : Serveur en attente de connexions...");
        start();
    }

    void saisieNoPort() {
        Une_Fen_Saisie_No_Port La_Fen_Saisie_No_Port = new Une_Fen_Saisie_No_Port();
        do {
            System.err.println("Un_Serveur : Saisie du numero de port...");
            La_Fen_Saisie_No_Port.active();
            while (!La_Fen_Saisie_No_Port.estPret()) ;
            La_Fen_Saisie_No_Port.desactive();
        }
        while (!initGuetteur(La_Fen_Saisie_No_Port.transfertNoPort()));
    }

    boolean initGuetteur(String Tel_No_Port) {
        System.err.println("Un_Serveur : Creation du guetteur...");
        Mon_Port = Integer.parseInt(Tel_No_Port);
        Mon_Guetteur = new Un_Guetteur(Mon_Port);
        Mon_Guetteur.start();
        return Mon_Guetteur.guetteurPret();
    }

    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer le serveur en <B>Thread</B> des la fin de son initialisation.
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> est la boucle permanente qui permet de valider les demandes de connexions retransmises par le guetteur aupres des differents serveurs specifiques.
     *
     * @see Un_Guetteur
     * @see Un_Canal_Connexion
     */
    public synchronized void run() {
        while (true) {
            try {
                Mon_Thread.sleep(100);
                if (Mon_Guetteur.connexionDemandee()) {
                    try {
                        Socket La_Socket_Connexion = Mon_Guetteur.donneSocketConnexion();
                        int Le_Cpt;
                        System.err.println("Un_Serveur : Demande de connexion de " + La_Socket_Connexion.getInetAddress()
                                + ":" + La_Socket_Connexion.getPort());
                        for (Le_Cpt = 0; (Le_Cpt < MON_NB_MAX_CLIENTS) && (!Mes_Canaux_Clients[Le_Cpt].estLibre()); Le_Cpt++)
                            ;
                        if (Le_Cpt < MON_NB_MAX_CLIENTS) {
                            System.err.println("Un_Serveur : Demande acceptee pour le client " + Le_Cpt + "...");
                            if (Mes_Canaux_Clients[Le_Cpt].dejaUtilise())
                                Mes_Canaux_Clients[Le_Cpt] = new Un_Canal_Connexion();
                            Mes_Canaux_Clients[Le_Cpt].activeFluxReceptionCHAT(Mon_Serveur_CHAT.donneFluxSortie(Le_Cpt));
                            Mes_Canaux_Clients[Le_Cpt].activeFluxReceptionJeu(Mon_Serveur_Jeu.donneFluxSortie(Le_Cpt));
                            Mes_Canaux_Clients[Le_Cpt].activeJoueur(Mon_Serveur_Jeu.donneJoueur(Le_Cpt));
                            int L_Indice_Init = (int) (Math.random() * MON_NB_POS_INIT) % MON_NB_POS_INIT;
                            Mes_Canaux_Clients[Le_Cpt].lance(La_Socket_Connexion, Mon_Serveur_CHAT.donneFluxReception(), Mon_Serveur_Jeu.donneFluxReception(), MON_NB_MAX_CLIENTS, Le_Cpt, MON_TAB_ANGLES_DEP[L_Indice_Init], MON_TAB_POS_X_DEP[L_Indice_Init], MON_TAB_POS_Y_DEP[L_Indice_Init]);
                            Mon_Serveur_Jeu.activeFluxSortieReseau(Le_Cpt, Mes_Canaux_Clients[Le_Cpt].donneFluxSortieReseau());
                            System.err.println("Un_Serveur : Canal de connexion " + Le_Cpt + " actif...");
                            System.err.println("Un_Serveur : Annonce de l'arrivee du nouveau joueur aux autres joueurs...");
                            for (int Le_Cpt2 = 0; Le_Cpt2 < MON_NB_MAX_CLIENTS; Le_Cpt2++)
                                if ((!Mes_Canaux_Clients[Le_Cpt2].estLibre()) && (Le_Cpt != Le_Cpt2)) {
                                    Mon_Serveur_Jeu.ajouteJoueur(Le_Cpt, Le_Cpt2);
                                    Mon_Serveur_Jeu.ajouteJoueur(Le_Cpt2, Le_Cpt);
                                }
                            System.err.println("Un_Serveur : Connexion etablie avec " + La_Socket_Connexion.getInetAddress() + "...");
                        } else {
                            DataOutputStream Mon_Flux_Acquittement = new DataOutputStream(La_Socket_Connexion.getOutputStream());
//                                                        Mon_Flux_Acquittement.writeUTF( "Connexion impossible...\n" );
                            Mon_Flux_Acquittement.writeBoolean(false);
                            Mon_Flux_Acquittement.flush();
                            System.err.println("Un_Serveur : Connexion impossible...");
                        }
                        Mon_Guetteur.connexionAcceptee();
                    } catch (IOException Telle_E) {
                        System.err.println("Un_Serveur : Erreur d'E/S lors de l'etablissement du nouveau canal...");
                    }
                }
            } catch (Exception Telle_E) {
                System.err.println("Un_Serveur : Erreur lors de la gestion d'une nouvelle connexion...");
            }
        }
    }

    /**
     * La methode <B>stop</B> de l'interface <B>Runnable</B> permet d'arreter le serveur lors de la generation d'un evenement <B>windowClosing</B> dans un des serveurs specifiques.
     *
     * @see Un_Serveur_CHAT
     */
    public synchronized void stop() {
        if (Mon_Thread != null) {
            Mon_Thread.stop();
            Mon_Thread = null;
        }
    }

}
