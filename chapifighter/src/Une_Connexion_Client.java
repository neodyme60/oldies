import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * La classe <B>Une_Connexion_Client</B> sert a instancier, dans le serveur, les canaux de connexion des clients. Elle regroupe la connexion reseau avec un client, ses flux reseaux associes et les flux internes vers les serveurs specifiques.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 2.0
 * @see Un_Client
 * @see Une_Fenetre_Visu
 */
public class Une_Connexion_Client implements Runnable {

    private static final int ID_SERVEUR_JEU = 1;
    private static final int ID_INFO_JOUEUR = 2;
    private static final int ID_DECONNECT = 5;
    private static final int ID_TIR = 6;

    private Thread Mon_Thread;
    private Socket Ma_Socket_Connexion;
    private DataInputStream Mon_Flux_Entree_Reseau;
    private DataOutputStream Mon_Flux_Sortie_Reseau;
    private int Mon_Nb_Max_Clients, Mon_Id, Mon_Angle_Dep, Mon_Score, Ma_Vie, Ma_Derniere_Victime;
    private double Ma_Pos_X_Dep, Ma_Pos_Y_Dep;
    private String Mon_Pseudo, Mon_Pct_Name;
    private Un_Joueur Mon_Joueur;
    private boolean Ma_Connexion_Acceptee;

    /**
     * Le constructeur de la classe <B>Une_Connexion_Client</B> sert simplement a creer l'objet. Pour reellement initialiser une connexion avec un client, il faut utiliser en plus la methode <B>lance</B>.
     *
     * @see Une_Connexion_Client#lance(Socket, PipedInputStream)
     */
    public Une_Connexion_Client(String Tel_Pseudo, String Telle_Adr_Serveur, String Tel_No_Port, String Tel_Pct_Name) {
        int Le_Nb_Bytes;
        byte Le_Buffer[] = new byte[1024];
        String La_Str_Reception;
        try {
            Ma_Socket_Connexion = new Socket(InetAddress.getByName(Telle_Adr_Serveur), Integer.parseInt(Tel_No_Port));
            Mon_Flux_Sortie_Reseau = new DataOutputStream(Ma_Socket_Connexion.getOutputStream());
            Mon_Flux_Entree_Reseau = new DataInputStream(Ma_Socket_Connexion.getInputStream());
            while (Mon_Flux_Entree_Reseau.available() == 0) ;
            Ma_Connexion_Acceptee = Mon_Flux_Entree_Reseau.readBoolean();
            if (Ma_Connexion_Acceptee)
                synchronized (Mon_Flux_Entree_Reseau) {
                    System.err.println("Une_Connexion_Client : Connexion etablie...");
                    // Reception de la trame de recuperation des donnees d initialisation du client
                    String La_Str = Mon_Flux_Entree_Reseau.readUTF();
                    La_Str = La_Str.concat(":");
                    int Le_Cpt_Str = 0;
                    Mon_Nb_Max_Clients = Integer.parseInt(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str)));
                    Ma_Derniere_Victime = Mon_Nb_Max_Clients;
                    System.err.println("Une_Connexion_Client : Nombre maximum d'autres joueurs => " + Mon_Nb_Max_Clients);
                    Le_Cpt_Str++;
                    Mon_Id = Integer.parseInt(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str)));
                    System.err.println("Une_Connexion_Client : Mon ID => " + Mon_Id);
                    Le_Cpt_Str++;
                    Mon_Angle_Dep = Integer.parseInt(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str)));
                    System.err.println("Une_Connexion_Client : Angle de depart => " + Mon_Angle_Dep);
                    Le_Cpt_Str++;
                    Ma_Pos_X_Dep = Double.valueOf(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str))).doubleValue();
                    System.err.println("Une_Connexion_Client : X de depart => " + Ma_Pos_X_Dep);
                    Le_Cpt_Str++;
                    Ma_Pos_Y_Dep = Double.valueOf(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str))).doubleValue();
                    System.err.println("Une_Connexion_Client : Y de depart => " + Ma_Pos_Y_Dep);
                    Le_Cpt_Str++;
                    Ma_Vie = Integer.parseInt(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str)));
                    System.err.println("Une_Connexion_Client : Points de vie => " + Ma_Vie);
                    Le_Cpt_Str++;
                    Mon_Score = Integer.parseInt(La_Str.substring(Le_Cpt_Str, Le_Cpt_Str = La_Str.indexOf(":", Le_Cpt_Str)));
                    System.err.println("Une_Connexion_Client : Score => " + Mon_Score);
                    Le_Cpt_Str++;
                    Mon_Pseudo = Tel_Pseudo;
                    Mon_Pct_Name = Tel_Pct_Name;
                    // Emission de la trame de validation d initialisation du client
                    La_Str = new String("");
                    La_Str = La_Str.concat(Mon_Pseudo + ":");
                    La_Str = La_Str.concat(Mon_Pct_Name);
                    Mon_Flux_Sortie_Reseau.writeUTF(La_Str);
                    Mon_Flux_Sortie_Reseau.flush();
                }
            else System.err.println("Une_Connexion_Client : Connexion refusee...");
        } catch (Exception Telle_E) {
            System.err.println("Une_Connexion_Client : Erreur lors de l'etablissement de la connexion avec le serveur...");
            return;
        }
        if (Ma_Connexion_Acceptee) start();
    }

    /**
     * La methode <B>recoitJoueur</B> sert a valider un pointeur sur le joueur correspondant au canal et a initialiser ses champs
     */
    public void recoitJoueur(Un_Joueur Tel_Joueur) {
        Mon_Joueur = Tel_Joueur;
        Mon_Joueur.mon_angle = Mon_Angle_Dep;
        Mon_Joueur.mon_x = Ma_Pos_X_Dep;
        Mon_Joueur.mon_y = Ma_Pos_Y_Dep;
        Mon_Joueur.mon_pseudo = Mon_Pseudo;
        Mon_Joueur.pct_name = Mon_Pct_Name;
        Mon_Joueur.ma_vie = Ma_Vie;
        Mon_Joueur.mon_score = Mon_Score;
    }


    /**
     * La methode <B>donneConnexionAcceptee</B> sert a renvoyer au client etablissant la connexion si celle-ci est validee.
     *
     * @see Un_Client
     */
    public boolean donneConnexionAcceptee() {
        return Ma_Connexion_Acceptee;
    }


    /**
     * La methode <B>donneId</B> sert a transferer au client correspondant l identificateur du joueur ( sa place dans le tableau des joueurs ).
     *
     * @see Un_Joueur
     * @see Un_Client
     */
    public int donneId() {
        return Mon_Id;
    }

    /**
     * La methode <B>donneNbMaxClients</B> sert a renvoyer au client le nombre maximum de clients qui pourront se connecter avec lui au serveur.
     *
     * @see Un_Joueur
     * @see Un_Client
     */
    public int donneNbMaxClients() {
        return Mon_Nb_Max_Clients;
    }

    /**
     * La methode <B>donnePseudo</B> sert a envoyer son pseudo au client.
     *
     * @see Un_Client
     */
    public String donnePseudo() {
        return Mon_Pseudo;
    }

    /**
     * La methode <B>donneFluxEntreeReseau</B> sert a renvoyer au client le flux direct de reception du reseau.
     *
     * @see Un_Client
     */
    public DataInputStream donneFluxEntreeReseau() {
        return Mon_Flux_Entree_Reseau;
    }

    /**
     * La methode <B>envoieMsg</B> sert a emettre un message en UTF ( string ) a un serveur specifique.
     *
     * @param Tel_Msg - Le message a envoyer, Tel_Id_Serveur - Le serveur specifique vise.
     * @see Un_Client
     * @see Un_Serveur_Jeu
     * @see Un_Serveur_CHAT
     */
    public void envoieMsg(String Tel_Msg, int Tel_Id_Serveur) {
        System.err.println("Une_Connexion_Client : Envoi d'un message CHAT...");
        try {
            synchronized (Mon_Flux_Sortie_Reseau) {
                String Le_Msg_Rezo = new String(Integer.toString(Tel_Id_Serveur) + ":" + Tel_Msg);
//				Mon_Flux_Sortie_Reseau.writeByte( Tel_Id_Serveur );
//				Mon_Flux_Sortie_Reseau.writeUTF( Tel_Msg );
                Mon_Flux_Sortie_Reseau.writeUTF(Le_Msg_Rezo);
                Mon_Flux_Sortie_Reseau.flush();
            }
        } catch (IOException Telle_E) {
            System.err.println("Une_Connexion_Client : Erreur lors de l'envoie d'un message sur le reseau...");
        }
    }

    /**
     * La methode <B>deconnect</B> sert a envoyer une trame de deconnexion au serveur.
     *
     * @see Un_Serveur
     */
    public void deconnect() {
        try {
            synchronized (Mon_Flux_Sortie_Reseau) {
                String Le_Msg_Rezo = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_DECONNECT) + ":" + Integer.toString(Mon_Id));
                Mon_Flux_Sortie_Reseau.writeUTF(Le_Msg_Rezo);
                Mon_Flux_Sortie_Reseau.flush();
            }
        } catch (Exception Telle_E) {
            System.err.println("Une_Connexion_Client : Envoi du message de deconnexion...");
        }
        Mon_Thread.stop();

    }


    /**
     * La methode <B>annonceTir</B> sert au client a valider un tir reussi sur un autre client.
     *
     * @param Telle_Victime - La victime du tir.
     * @see Un_Client
     */
    public void annonceTir(int Telle_Victime) {
        Ma_Derniere_Victime = Telle_Victime;
    }

    private void valideTir(int Telle_Victime) {
        try {
            synchronized (Mon_Flux_Sortie_Reseau) {
                String Le_Msg_Rezo = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_TIR) + ":" + Integer.toString(Telle_Victime));
                Mon_Flux_Sortie_Reseau.writeUTF(Le_Msg_Rezo);
                Mon_Flux_Sortie_Reseau.flush();
            }
        } catch (Exception Telle_E) {
            System.err.println("Une_Connexion_Client : Erreur lors de l'annonce d un tir reussi au serveur...");
        }
    }

    /**
     * La methode <B>miseAJourJoueur</B> sert a envoyer une trame de mise a jour du joueur sur le reseau.
     *
     * @param Tel_Joueur - Le joueur a remettre a jour.
     * @see Un_Joueur
     */
    public void miseAJourJoueur(Un_Joueur Tel_Joueur) {
        try {
            synchronized (Mon_Flux_Sortie_Reseau) {
                String Le_Msg_Rezo = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_INFO_JOUEUR) + ":" + Integer.toString(Tel_Joueur.mon_angle) + ":" + Double.toString(Tel_Joueur.mon_x) + ":" + Double.toString(Tel_Joueur.mon_y));
                Mon_Flux_Sortie_Reseau.writeUTF(Le_Msg_Rezo);
                Mon_Flux_Sortie_Reseau.flush();
            }
            if (Ma_Derniere_Victime != Mon_Nb_Max_Clients)
                valideTir(Ma_Derniere_Victime);
        } catch (Exception Telle_E) {
            System.err.println("Une_Connexion_Client : Erreur lors de l'envoi des donnees du perso...");
        }
    }

    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer le canal de connexion en <B>Thread</B> a la fin de la methode lance.
     *
     * @see Une_Connexion_Client#lance(Socket, PipedInputStream)
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
        while (true) {
            try {
                Mon_Thread.sleep(750);
                miseAJourJoueur(Mon_Joueur);
                Ma_Derniere_Victime = Mon_Nb_Max_Clients;
            } catch (Exception Telle_E) {
                System.err.println("Une_Connexion_Client : Erreur lors de l'attente....");
            }
        }
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
                System.err.println("Une_Connexion_Client : Erreur lors de la fermeture de la socket de connexion...");
            }
        if (Mon_Thread != null) {
            Mon_Thread = null;
            Mon_Thread.stop();
        }
    }

}
