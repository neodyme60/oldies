import java.io.*;
import java.net.Socket;

/**
 * La classe <B>Un_Canal_Connexion</B> sert a instancier, dans le serveur, les canaux de connexion des clients. Elle regroupe la connexion reseau avec un client, ses flux reseaux associes et les flux internes vers les serveurs specifiques.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 1.2
 * @see Un_Serveur
 * @see Un_Serveur_CHAT
 */
public class Un_Canal_Connexion implements Runnable {

    private static final int VIE_DEP = 100;
    private static final int SCORE_DEP = 0;

    private static final int ID_INFO_JOUEUR = 2;
    private static final int ID_SERVEUR_CHAT = 0;
    private static final int ID_SERVEUR_JEU = 1;
    private static final int ID_DECONNECT = 5;
    private static final int ID_TIR = 6;
    private static PipedOutputStream Mon_Tube_Commun_Sortie_CHAT;
    private static DataOutputStream Mon_Flux_Commun_Sortie_CHAT;
    private static PipedOutputStream Mon_Tube_Commun_Sortie_Jeu;
    private static DataOutputStream Mon_Flux_Commun_Sortie_Jeu;
    private Thread Mon_Thread;
    private Socket Ma_Socket_Connexion;
    private DataInputStream Mon_Flux_Entree_Reseau;
    private DataOutputStream Mon_Flux_Sortie_Reseau;
    private PipedInputStream Mon_Tube_Entree_CHAT;
    private DataInputStream Mon_Flux_Entree_CHAT;
    private PipedInputStream Mon_Tube_Entree_Jeu;
    private DataInputStream Mon_Flux_Entree_Jeu;
    private boolean Ma_Liberte, Mon_Utilisation;
    private int Mon_Id;
    private Un_Joueur Mon_Joueur;

    /**
     * Le constructeur de la classe <B>Un_Canal_Connexion</B> sert simplement a creer l'objet. Pour reellement initialiser une connexion avec un client, il faut utiliser en plus la methode <B>lance</B>.
     *
     * @see Un_Canal_Connexion#lance(Socket, PipedInputStream)
     */
    public Un_Canal_Connexion() {
        Ma_Liberte = true;
        Mon_Utilisation = false;
    }

    /**
     * La methode <B>lance</B> permet d'initialiser la connexion reseau vers le client, ainsi que les flux internes d'emission vers les serveurs specifiques. Elle ne rend pas la main sans avoir recupere le pseudo du client par le reseau, puis lance le canal en <B>Thread</B>.
     *
     * @param Telle_Socket_Connexion  - La <B>Socket</B> retournee par le guetteur.
     * @param Tel_Flux_Reception_CHAT - Le flux <B>static</B> retourne par le serveur de CHAT.
     * @see Un_Guetteur#donneSocketConnexion()
     * @see Un_Serveur_CHAT#donneFluxReception()
     * @see Un_Serveur
     */
    public void lance(Socket Telle_Socket_Connexion, PipedInputStream Tel_Tube_Reception_CHAT, PipedInputStream Tel_Tube_Reception_Jeu, int Tel_Nb_Max_Clients, int Tel_Id_Connecte, int Tel_Angle_Dep, double Telle_Pos_X_Dep, double Telle_Pos_Y_Dep) {
        int Le_Nb_Bytes;
        byte Le_Buffer[] = new byte[1024];
        Ma_Socket_Connexion = Telle_Socket_Connexion;
        Ma_Liberte = false;
        Mon_Utilisation = true;
        try {
            Mon_Flux_Sortie_Reseau = new DataOutputStream(Ma_Socket_Connexion.getOutputStream());
            Mon_Flux_Entree_Reseau = new DataInputStream(Ma_Socket_Connexion.getInputStream());
            if (Mon_Flux_Commun_Sortie_CHAT == null) {
                Mon_Tube_Commun_Sortie_CHAT = new PipedOutputStream(Tel_Tube_Reception_CHAT);
                Mon_Flux_Commun_Sortie_CHAT = new DataOutputStream(Mon_Tube_Commun_Sortie_CHAT);
            }
            if (Mon_Flux_Commun_Sortie_Jeu == null) {
                Mon_Tube_Commun_Sortie_Jeu = new PipedOutputStream(Tel_Tube_Reception_Jeu);
                Mon_Flux_Commun_Sortie_Jeu = new DataOutputStream(Mon_Tube_Commun_Sortie_Jeu);
            }
            // Trame d'acceptation de connexion
            Mon_Flux_Sortie_Reseau.writeBoolean(true);
            Mon_Flux_Sortie_Reseau.flush();
            // Initialisation de Mon_Joueur pour ce canal de connexion
            Mon_Id = Tel_Id_Connecte;
            Mon_Joueur.mon_angle = Tel_Angle_Dep;
            Mon_Joueur.mon_x = Telle_Pos_X_Dep;
            Mon_Joueur.mon_y = Telle_Pos_Y_Dep;
            Mon_Joueur.ma_vie = VIE_DEP;
            Mon_Joueur.mon_score = SCORE_DEP;
            // Emission de la trame d'initialisation du joueur
            String La_Str = new String("");
            La_Str = La_Str.concat(Integer.toString(Tel_Nb_Max_Clients) + ":");
            La_Str = La_Str.concat(Integer.toString(Mon_Id) + ":");
            La_Str = La_Str.concat(Integer.toString(Mon_Joueur.mon_angle) + ":");
            La_Str = La_Str.concat(Double.toString(Mon_Joueur.mon_x) + ":");
            La_Str = La_Str.concat(Double.toString(Mon_Joueur.mon_y) + ":");
            La_Str = La_Str.concat(Integer.toString(Mon_Joueur.ma_vie) + ":");
            La_Str = La_Str.concat(Integer.toString(Mon_Joueur.mon_score));
            Mon_Flux_Sortie_Reseau.writeUTF(La_Str);
            Mon_Flux_Sortie_Reseau.flush();
            // Reception de la trame de validation d initialisation du client
            La_Str = Mon_Flux_Entree_Reseau.readUTF();
            La_Str = La_Str.concat(":");
            int Le_Cpt_Str = 0;
            Mon_Joueur.mon_pseudo = La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str));
            Le_Cpt_Str++;
            Mon_Joueur.pct_name = La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str));
            System.err.println("Un_Canal_Connexion : Pseudo recu => " + Mon_Joueur.mon_pseudo.trim());
            System.err.println("Un_Canal_Connexion : Nom d image recu => " + Mon_Joueur.pct_name.trim());
            Mon_Joueur.estActif = true;
            synchronized (Mon_Joueur.getClass()) {
                Mon_Joueur.mon_nb_joueurs_actifs += 1;
            }
        } catch (Exception Telle_E) {
            System.err.println("Un_Canal_Connexion : Erreur lors de l'etablissement des flux du canal...");
            return;
        }
        start();
    }

    /**
     * La methode <B>activeFluxReceptionCHAT</B> sert a activer le flux de reception des messages du serveur de CHAT.
     *
     * @Param Tel_Flux_Emission_CHAT - Le flux de sortie retourne par le serveur de CHAT.
     * @see Un_Serveur_CHAT#donneFluxSortie(int)
     */
    public void activeFluxReceptionCHAT(PipedOutputStream Tel_Tube_Emission_CHAT) {
        try {
            Mon_Tube_Entree_CHAT = new PipedInputStream(Tel_Tube_Emission_CHAT);
            Mon_Flux_Entree_CHAT = new DataInputStream(Mon_Tube_Entree_CHAT);
        } catch (IOException Telle_E) {
            System.err.println("Un_Canal_Connexion : Erreur lors de l'activation du flux de reception du CHAT...");
            return;
        }
    }

    /**
     * La methode <B>activeFluxReceptionJeu</B> sert a activer le flux de reception des messages du serveur de Jeu.
     *
     * @Param Tel_Flux_Emission_Jeu - Le flux de sortie retourne par le serveur de Jeu.
     * @see Un_Serveur_Jeu#donneFluxSortie(int)
     */
    public void activeFluxReceptionJeu(PipedOutputStream Tel_Tube_Emission_Jeu) {
        try {
            Mon_Tube_Entree_Jeu = new PipedInputStream(Tel_Tube_Emission_Jeu);
            Mon_Flux_Entree_Jeu = new DataInputStream(Mon_Tube_Entree_Jeu);
        } catch (IOException Telle_E) {
            System.err.println("Un_Canal_Connexion : Erreur lors de l'activation du flux de reception du Jeu...");
            return;
        }
    }

    /**
     * La methode <B>activeJoueur</B> sert a activer le joueur relie au canal de connexion.
     *
     * @Param Tel_Joueur - Le joueur a ajouter.
     */
    public void activeJoueur(Un_Joueur Tel_Joueur) {
        Mon_Joueur = Tel_Joueur;
    }

    /**
     * La methode <B>donneFluxSortieReseau</B> sert envoyer le flux de sortie direct sur le reseau au serveur de jeu.
     *
     * @see Un_Serveur_Jeu#activeFluxSortieReseau(int, int)
     */
    public DataOutputStream donneFluxSortieReseau() {
        return Mon_Flux_Sortie_Reseau;
    }


    /**
     * La methode <B>estLibre</B> sert a indiquer au serveur que le canal est libre pour une assignation de connexion avec un client.
     *
     * @see Un_Serveur
     */
    public boolean estLibre() {
        return Ma_Liberte;
    }

    /**
     * La methode <B>dejaUtilise</B> sert a indiquer au serveur que le canal a deja ete utilise, puis libere par un client. Le serveur devra donc reinitialiser le canal.
     *
     * @see Un_Serveur
     */
    public boolean dejaUtilise() {
        return Mon_Utilisation;
    }

    void repeteCHAT(String Telle_Str) {
        synchronized (Mon_Flux_Commun_Sortie_CHAT) {
            try {
                String La_Str = new String(Mon_Joueur.mon_pseudo + " : " + Telle_Str + "\n");
                Mon_Flux_Commun_Sortie_CHAT.writeUTF(La_Str);
                Mon_Flux_Commun_Sortie_CHAT.flush();
            } catch (Exception Telle_E) {
                System.err.println("Un_Canal_Connexion : Erreur lors de la repetition du message CHAT...");
                return;
            }
        }
    }


    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer le canal de connexion en <B>Thread</B> a la fin de la methode lance.
     *
     * @see Un_Canal_Connexion#lance(Socket, PipedInputStream)
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> contient la boucle permanente qui va guetter l'arriver de messages sur les flux d'entree et les retransmettres dans les flux de sortie auquels ils sont destines.
     */
    public synchronized void run() {
        try {
            int Le_Nb_Bytes;
            byte Le_Buffer[] = new byte[1024];
            String La_Str;
            boolean La_Sortie = false;
            while (!La_Sortie) {
                Mon_Thread.sleep(10);
                if (Mon_Flux_Entree_Reseau.available() != 0) {
                    String Le_Msg_Rezo = Mon_Flux_Entree_Reseau.readUTF();
                    Le_Msg_Rezo = Le_Msg_Rezo.concat(":");
                    int Le_Cpt_Str = 0;
                    switch (Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)))) {
                        // Msg pour le CHAT
                        case ID_SERVEUR_CHAT:
                            Le_Cpt_Str++;
                            La_Str = Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str));
                            System.err.println("Un_Canal_Connexion : Acquittement du message CHAT...");
                            if (La_Str.trim().compareTo("bye!") == 0) {
                                repeteCHAT(La_Str.trim() + " de " + Ma_Socket_Connexion.getInetAddress() + '\n');
                                Mon_Joueur.estActif = false;
                                synchronized (Mon_Joueur.getClass()) {
                                    Mon_Joueur.mon_nb_joueurs_actifs -= 1;
                                }
                                La_Sortie = true;
                            } else repeteCHAT(La_Str.trim());
                            break;
                        // Msg pour le Jeu
                        case ID_SERVEUR_JEU:
                            Le_Cpt_Str++;
                            switch (Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)))) {
                                case ID_INFO_JOUEUR:
                                    Le_Cpt_Str++;
                                    Mon_Joueur.mon_angle = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Le_Cpt_Str++;
                                    Mon_Joueur.mon_x = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    Le_Cpt_Str++;
                                    Mon_Joueur.mon_y = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    System.err.println("Un_Canal_Connexion : " + Mon_Joueur.mon_pseudo + " => " + Integer.toString(Mon_Joueur.mon_angle) + " / " + Double.toString(Mon_Joueur.mon_x) + " / " + Double.toString(Mon_Joueur.mon_y));
                                    break;
                                case ID_DECONNECT:
                                    Le_Cpt_Str++;
                                    System.err.println("Un_Canal_Connexion : Reception d'un message de deconnexion de " + Mon_Joueur.mon_pseudo + "...");
//									Mon_Flux_Commun_Sortie_Jeu.writeByte( ID_DECONNECT );
//									Mon_Flux_Commun_Sortie_Jeu.writeInt( Integer.parseInt( Le_Msg_Rezo.substring( Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf( ":", Le_Cpt_Str ) ) ) );
                                    Mon_Flux_Commun_Sortie_Jeu.writeUTF(Integer.toString(ID_DECONNECT) + ":" + Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Mon_Flux_Commun_Sortie_Jeu.flush();
                                    break;
                                case ID_TIR:
                                    Le_Cpt_Str++;
                                    System.err.println("Un_Cannal_Connexion : Reception d un tir reussi de " + Mon_Joueur.mon_pseudo + "...");
                                    Mon_Flux_Commun_Sortie_Jeu.writeUTF(Integer.toString(ID_TIR) + ":" + Integer.toString(Mon_Id) + ":" + Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Mon_Flux_Commun_Sortie_Jeu.flush();
                                    break;
                                default:
                                    System.err.println("Un_Canal_Connexion : Reception d'un message Jeu non identifiable...");
                                    break;
                            }
                            break;
                        default:
                            System.err.println("Un_Canal_Connexion : Message non-attribuable a un serveur specifque...");
                            break;
                    }
                }
                if (Mon_Flux_Entree_CHAT.available() != 0) {
                    La_Str = Mon_Flux_Entree_CHAT.readUTF();
                    String Le_Msg_Rezo = new String("");
                    System.err.println("Un_Canal_Connexion : Emission d'un message CHAT...");
                    synchronized (Mon_Flux_Sortie_Reseau) {
                        Le_Msg_Rezo = Le_Msg_Rezo.concat(Integer.toString(ID_SERVEUR_CHAT) + ":" + La_Str);
//                                        	Mon_Flux_Sortie_Reseau.writeByte( ID_SERVEUR_CHAT );
//						Mon_Flux_Sortie_Reseau.writeUTF( La_Str );
                        Mon_Flux_Sortie_Reseau.writeUTF(Le_Msg_Rezo);
                        Mon_Flux_Sortie_Reseau.flush();
                    }
                }
//				if ( Mon_Flux_Entree_Jeu.available( ) != 0 )
//                                {
//					La_Str = Mon_Flux_Entree_Jeu.readUTF( );
//                                        System.err.println( "Un_Canal_Connexion : Emission d'un message Jeu..." );
//                                        Mon_Flux_Sortie_Reseau.writeByte( ID_SERVEUR_JEU );
//					Mon_Flux_Sortie_Reseau.writeUTF( La_Str );
//                                        Mon_Flux_Sortie_Reseau.flush( );
//                                }
            }
        } catch (Exception Telle_E) {
            System.err.println("Un_Canal_Connexion : Erreur d'E/S du canal : " + Telle_E);
        }
        Ma_Liberte = true;
    }

    /**
     * La methode <B>stop</B> de l'interface </B>Runnable</B> permet d'arreter l'execution du canal lors de la fin du programme.
     */
    public synchronized void stop() {
        if (Ma_Socket_Connexion != null)
            try {
                Ma_Socket_Connexion.close();
                Ma_Socket_Connexion = null;
            } catch (IOException Telle_E) {
                System.err.println("Un_Canal_Connexion : Erreur lors de la fermeture de la socket de connexion...");
            }
        if (Mon_Thread != null) {
            Mon_Thread = null;
            Mon_Thread.stop();
        }
    }

}
