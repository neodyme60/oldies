import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * La classe <B>Un_Serveur_Jeu</B> est le serveur specifique qui gere le Jeu. Apres recuperation des donnees qui lui sont destinees aupres des canaux de connexion, il reemet les messages vers tous les clients.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 2.0
 * @see Un_Canal_Connexion
 * @see Un_Serveur
 */
public class Un_Serveur_Jeu extends Frame implements WindowListener, ActionListener, Runnable {

    private static final int PT_MARQUE = 1;
    private static final int COUT_VIE = 10;
    private static final int MA_VIE_DEP = 100;
    private static final int MON_NB_POS_INIT = 3;

    private static final int MON_TAB_ANGLES_DEP[] = {0, 45, 270};
    private static final double MON_TAB_POS_X_DEP[] = {90.0, 352.0, 8 * 64 + 32};
    private static final double MON_TAB_POS_Y_DEP[] = {90.0, 352.0, 14 * 64 + 32};

    private static final int ID_INFO_JOUEUR = 2;
    private static final int ID_SERVEUR_JEU = 1;
    private static final int ID_AJOUT_JOUEUR = 3;
    private static final int ID_MAJ_JOUEURS = 4;
    private static final int ID_DECONNECT = 5;
    private static final int ID_TIR = 6;
    private static final int ID_MAJ_SCORE = 7;
    private static final int ID_MAJ_VIE = 8;
    private static final int ID_MORT = 9;
    private static final int ID_FIN_JEU = 10;

    private static int MON_BUT_JEU;

    private Thread Mon_Thread;
    private PipedOutputStream Mes_Tubes_Sorties_Clients[];
    private PipedInputStream Mon_Tube_Entree_Clients;
    private DataOutputStream Mes_Flux_Sorties_Clients[];
    private DataOutputStream Mes_Flux_Sorties_Reseau[];
    private DataInputStream Mon_Flux_Entree_Clients;
    private TextArea Mon_Chp_Reception;
    private Button Mon_Btn_Quitter;
    private Panel Ma_Zone_Reception, Ma_Zone_Btn;
    private Un_Joueur Mes_Joueurs[];

    /**
     * Le constructeur de la classe <B>Un_Serveur_Jeu</B> prend en parametre le nombre maximum de clients qu'il doit pouvoir gerer. Il affiche une fenetre de reception des messages, initialise ses flux de communications internes et se met en <B>Thread</B>
     *
     * @param Tel_Nb_Max_Clients - Le nombre max de clients a gerer.
     * @param Tel_But_Jeu        - Le nombre de victimes a faire pour gagner.
     */
    public Un_Serveur_Jeu(int Tel_Nb_Max_Clients, int Tel_But_Jeu) {
        super("Serveur Jeu");
        MON_BUT_JEU = Tel_But_Jeu;
        Mes_Tubes_Sorties_Clients = new PipedOutputStream[Tel_Nb_Max_Clients];
        Mes_Flux_Sorties_Clients = new DataOutputStream[Tel_Nb_Max_Clients];
        Mes_Flux_Sorties_Reseau = new DataOutputStream[Tel_Nb_Max_Clients];
        Mon_Tube_Entree_Clients = new PipedInputStream();
        Mon_Flux_Entree_Clients = new DataInputStream(Mon_Tube_Entree_Clients);
        addWindowListener(this);
        setTitle("Serveur Jeu");
        setSize(300, 300);
        setResizable(false);
        Mon_Chp_Reception = new TextArea("Serveur Jeu active\n", 14, 30, TextArea.SCROLLBARS_NONE);
        Mon_Chp_Reception.setEditable(false);
        Mon_Chp_Reception.setEnabled(false);
        Ma_Zone_Reception = new Panel();
        Ma_Zone_Reception.add("Center", Mon_Chp_Reception);
        Mon_Btn_Quitter = new Button("Quitter");
        Mon_Btn_Quitter.addActionListener(this);
        Ma_Zone_Btn = new Panel();
        Ma_Zone_Btn.add("Center", Mon_Btn_Quitter);
        add("North", Ma_Zone_Reception);
        add("South", Ma_Zone_Btn);
        Mes_Joueurs = new Un_Joueur[Tel_Nb_Max_Clients];
        for (int Le_Cpt = 0; Le_Cpt < Tel_Nb_Max_Clients; Le_Cpt++) {
            Mes_Joueurs[Le_Cpt] = new Un_Joueur();
            Mes_Joueurs[Le_Cpt].estActif = false;
            synchronized (Mes_Joueurs[Le_Cpt].getClass()) {
                Mes_Joueurs[Le_Cpt].mon_nb_joueurs_actifs = 0;
            }
        }
        active();
        start();
    }

    /**
     * La methode <B>activeFluxSortieReseau</B> sert a activer un flux de sortie reseau direct vers un client.
     *
     * @param Tel_No_Client          - Le numero du client a ajouter.
     * @param Tel_Flux_Sortie_Reseau - Le flux reseau a lier.
     * @see Un_Canal_Connexion#donneFluxEntreeReseau()
     */
    public void activeFluxSortieReseau(int Tel_No_Client, DataOutputStream Tel_Flux_Sortie_Reseau) {
        Mes_Flux_Sorties_Reseau[Tel_No_Client] = Tel_Flux_Sortie_Reseau;
    }


    /**
     * La methode <B>ajouteJoueur</B> sert a faire enregistrer un nouveau client aupres d un autre.
     *
     * @param Tel_Joueur_Ajoute - Le client a ajouter.
     * @param Tel_Joueur_Dest   - Le client a qui ajouter l autre client.
     */
    public void ajouteJoueur(int Tel_Joueur_Ajoute, int Tel_Joueur_Dest) {
        synchronized (Mes_Flux_Sorties_Reseau[Tel_Joueur_Dest]) {
            try {
                String Le_Msg_Rezo = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_AJOUT_JOUEUR) + ":" + Integer.toString(Tel_Joueur_Ajoute) + ":" + Integer.toString(Mes_Joueurs[Tel_Joueur_Ajoute].mon_angle) + ":" + Double.toString(Mes_Joueurs[Tel_Joueur_Ajoute].mon_x) + ":" + Double.toString(Mes_Joueurs[Tel_Joueur_Ajoute].mon_y) + ":" + Mes_Joueurs[Tel_Joueur_Ajoute].mon_pseudo + ":" + Mes_Joueurs[Tel_Joueur_Ajoute].pct_name);
                Mes_Flux_Sorties_Reseau[Tel_Joueur_Dest].writeUTF(Le_Msg_Rezo);
                Mes_Flux_Sorties_Reseau[Tel_Joueur_Dest].flush();
                System.err.println("Un_Serveur_Jeu : Ajout de " + Mes_Joueurs[Tel_Joueur_Ajoute].mon_pseudo + " aupres de " + Mes_Joueurs[Tel_Joueur_Dest].mon_pseudo + "...");
            } catch (Exception Telle_E) {
                System.err.println("Un_Serveur_Jeu : Erreur lors de l'ajout de " + Mes_Joueurs[Tel_Joueur_Ajoute].mon_pseudo + " aupres de " + Mes_Joueurs[Tel_Joueur_Dest].mon_pseudo + "...");
            }
        }
    }

    /**
     * La methode <B>donneFluxSortie</B> sert a envoyer a un canal de connexion client le flux auquel il doit se lier pour pouvoir recevoir les messages Jeu a emettre sur le reseau.
     *
     * @param Tel_No_Client - int representant le nombre maximum de clients a gerer.
     * @return Le flux de sortie dans lequel seront reemis les messages du Jeu pour le reseau.
     * @see Un_Canal_Connexion#activeFluxReceptionJeu(PipedOutputStream)
     */
    public PipedOutputStream donneFluxSortie(int Tel_No_Client) {

        Mes_Tubes_Sorties_Clients[Tel_No_Client] = new PipedOutputStream();
        Mes_Flux_Sorties_Clients[Tel_No_Client] = new DataOutputStream(Mes_Tubes_Sorties_Clients[Tel_No_Client]);
        return Mes_Tubes_Sorties_Clients[Tel_No_Client];
    }

    /**
     * La methode <B>donneFluxReception</B> sert a envoyer a un canal de connexion client le flux commun ( <B>static</B> ) auquel il doit se lier pouvoir retransmettre au serveur de Jeu les messages qui lui sont destines en provenance du reseau.
     *
     * @return Le flux <B>static</B> vers lequel vont emettre les canaux clients.
     * @see Un_Canal_Connexion#lance(Socket, PipedInputStream)
     */
    public PipedInputStream donneFluxReception() {
        return Mon_Tube_Entree_Clients;
    }

    public Un_Joueur donneJoueur(int Tel_Id) {
        return Mes_Joueurs[Tel_Id];
    }

    /**
     * La methode <B>active</B> permet au serveur de Jeu d'afficher sa fenetre de reception des messages.
     */
    public void active() {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu reactivee...");
        show();
    }

    /**
     * La methode <B>desactive</B> permet de cacher la fenetre de reception des messages des que celle-ci n'est plus utile.
     */
    public void desactive() {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu desactivee...");
        removeNotify();
    }

    void quitter() {
        desactive();
        System.err.println("Un_Serveur_Jeu : Sortie du serveur...     bye!");
        System.exit(0);
    }


    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer le serveur de Jeu en <B>Thread</B> des la fin de son initialisation.
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> contient la boucle permanente qui permet de retransmettre a tous les canaux clients les messages recus dans le flux commun de reception.
     */
    public synchronized void run() {
        int Le_Nb_Car;
        byte Le_Buffer[] = new byte[1024];
        System.err.println("Un_Serveur_Jeu : Serveur Jeu en ecoute...");
        while (true) {
            try {
                try {
                    Mon_Thread.sleep(750);
                } catch (Exception Telle_E) {
                    System.err.println("Un_Serveur_Jeu : Erreur pendant l attente...");
                }
                String La_Str = new String("");
                La_Str = La_Str.concat(Integer.toString(Mes_Joueurs[0].mon_nb_joueurs_actifs));
                for (int Le_Cpt_Orig = 0; Le_Cpt_Orig < Mes_Joueurs.length; Le_Cpt_Orig++)
                    if (Mes_Joueurs[Le_Cpt_Orig].estActif)
                        La_Str = La_Str.concat(":" + Integer.toString(Le_Cpt_Orig) + ":" + Integer.toString(Mes_Joueurs[Le_Cpt_Orig].mon_angle) + ":" + Double.toString(Mes_Joueurs[Le_Cpt_Orig].mon_x) + ":" + Double.toString(Mes_Joueurs[Le_Cpt_Orig].mon_y));
                for (int Le_Cpt_Dest = 0; Le_Cpt_Dest < Mes_Joueurs.length; Le_Cpt_Dest++)
                    if (Mes_Joueurs[Le_Cpt_Dest].estActif)
                        synchronized (Mes_Flux_Sorties_Reseau[Le_Cpt_Dest]) {
                            String Le_Msg_Rezo = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_MAJ_JOUEURS) + ":" + La_Str);
                            Mes_Flux_Sorties_Reseau[Le_Cpt_Dest].writeUTF(Le_Msg_Rezo);
                            Mes_Flux_Sorties_Reseau[Le_Cpt_Dest].flush();
                        }
                if (Mon_Flux_Entree_Clients.available() != 0) {
                    String Le_Msg_Rezo = Mon_Flux_Entree_Clients.readUTF();
                    Le_Msg_Rezo = Le_Msg_Rezo.concat(":");
                    int Le_Cpt_Str = 0;
                    switch (Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)))) {
                        case ID_DECONNECT:
                            Le_Cpt_Str++;
                            int L_Id = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                            Mes_Joueurs[L_Id].estActif = false;
                            for (int Le_Cpt = 0; Le_Cpt < Mes_Joueurs.length; Le_Cpt++)
                                if (Mes_Joueurs[Le_Cpt].estActif)
                                    synchronized (Mes_Flux_Sorties_Reseau[Le_Cpt]) {
                                        String Le_Msg = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_DECONNECT) + ":" + Integer.toString(L_Id));
                                        Mes_Flux_Sorties_Reseau[Le_Cpt].writeUTF(Le_Msg);
                                        Mes_Flux_Sorties_Reseau[Le_Cpt].flush();
                                        System.err.println("Un_Serveur_Jeu : Annonce de deconnexion de " + Mes_Joueurs[L_Id].mon_pseudo + " a " + Mes_Joueurs[Le_Cpt].mon_pseudo + "...");
                                    }
                            break;
                        case ID_TIR:
                            Le_Cpt_Str++;
                            int L_Id_Predateur = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                            Le_Cpt_Str++;
                            int L_Id_Proie = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                            if ((Mes_Joueurs[L_Id_Proie].ma_vie -= COUT_VIE) > 0) {
                                String Le_Msg = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_MAJ_VIE) + ":" + Integer.toString(Mes_Joueurs[L_Id_Proie].ma_vie));
                                System.err.println("Un_Serveur_Jeu : " + Mes_Joueurs[L_Id_Predateur].mon_pseudo + " a touche " + Mes_Joueurs[L_Id_Proie].mon_pseudo + "...");
                                Mes_Flux_Sorties_Reseau[L_Id_Proie].writeUTF(Le_Msg);
                                Mes_Flux_Sorties_Reseau[L_Id_Proie].flush();
                            } else {
                                if (Mes_Joueurs[L_Id_Proie].ma_vie == 0) {
                                    Mes_Joueurs[L_Id_Predateur].mon_score += PT_MARQUE;
                                    String Le_Msg = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_MAJ_SCORE) + ":" + Integer.toString(Mes_Joueurs[L_Id_Predateur].mon_score));
                                    Mes_Flux_Sorties_Reseau[L_Id_Predateur].writeUTF(Le_Msg);
                                    Mes_Flux_Sorties_Reseau[L_Id_Predateur].flush();
                                    System.err.println("Un_Serveur_Jeu : " + Mes_Joueurs[L_Id_Predateur].mon_pseudo + " a tue " + Mes_Joueurs[L_Id_Proie].mon_pseudo + "...");
                                }
                                Mes_Joueurs[L_Id_Proie].ma_vie = MA_VIE_DEP;
                                int Le_Nvl_Indice = (int) (Math.random() * MON_NB_POS_INIT) % MON_NB_POS_INIT;
                                Mes_Joueurs[L_Id_Proie].mon_angle = MON_TAB_ANGLES_DEP[Le_Nvl_Indice];
                                Mes_Joueurs[L_Id_Proie].mon_x = MON_TAB_POS_X_DEP[Le_Nvl_Indice];
                                Mes_Joueurs[L_Id_Proie].mon_y = MON_TAB_POS_Y_DEP[Le_Nvl_Indice];
                                String Le_Msg = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_MORT) + ":" + Integer.toString(MA_VIE_DEP) + ":" + Integer.toString(MON_TAB_ANGLES_DEP[Le_Nvl_Indice]) + ":" + Double.toString(MON_TAB_POS_X_DEP[Le_Nvl_Indice]) + ":" + Double.toString(MON_TAB_POS_Y_DEP[Le_Nvl_Indice]));
                                Mes_Flux_Sorties_Reseau[L_Id_Proie].writeUTF(Le_Msg);
                                Mes_Flux_Sorties_Reseau[L_Id_Proie].flush();
                                if (Mes_Joueurs[L_Id_Predateur].mon_score >= MON_BUT_JEU) {
                                    System.err.println("Un_Serveur_Jeu : Partie gangee par " + Mes_Joueurs[L_Id_Predateur].mon_pseudo);
                                    for (int Le_Cpt_Perdants = 0; Le_Cpt_Perdants < Mes_Joueurs.length; Le_Cpt_Perdants++)
                                        if (Mes_Joueurs[Le_Cpt_Perdants].estActif) {
                                            String Le_Msg2 = new String(Integer.toString(ID_SERVEUR_JEU) + ":" + Integer.toString(ID_FIN_JEU) + ":" + Integer.toString(L_Id_Predateur));
                                            Mes_Flux_Sorties_Reseau[Le_Cpt_Perdants].writeUTF(Le_Msg2);
                                            Mes_Flux_Sorties_Reseau[Le_Cpt_Perdants].flush();
                                        }
                                    System.err.println("Un_Serveur_Jeu : Attente de deconnexion de tous les joueurs...");
                                    while (Mes_Joueurs[0].mon_nb_joueurs_actifs != 0) ;
                                    quitter();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception Telle_E) {
                System.err.println("Un_Serveur_Jeu : Erreur lors de la gestion du message jeu..." + Telle_E);
            }
        }
    }

    /**
     * La methode <B>stop</B> de l'interface <B>Runnable</B> permet d'arreter le serveur de Jeu lors de la generation d'un evenement <B>windowClosing</B> dans une des fenetres affichees a l'ecran.
     */
    public synchronized void stop() {
        if (Mon_Thread != null) {
            Mon_Thread.stop();
            Mon_Thread = null;
        }
    }

    /**
     * La methode <B>actionPerformed</B> de l'interface <B>ActionListener</B> permet de gerer l'action de clic de la souris sur le bouton <B>Quitter</B>.
     */
    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Mon_Btn_Quitter) quitter();
    }

    /**
     * La methode <B>windowIconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'iconification de la fenetre.
     */
    public void windowIconified(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu iconified...");
    }

    /**
     * La methode <B>windowDeiconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de restauration de la fenetre.
     */
    public void windowDeiconified(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu restauree...");
    }

    /**
     * La methode <B>windowActivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'activation de la fenetre.
     */
    public void windowActivated(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu activee...");
    }

    /**
     * La methode <B>windowDeactivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de desactivation de la fenetre.
     */
    public void windowDeactivated(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu desactivee...");
    }

    /**
     * La methode <B>windowOpened</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'ouverture de la fenetre.
     */
    public void windowOpened(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu ouverte...");
    }

    /**
     * La methode <B>windowClosing</B> de l'interface <B>WindowListener</B> permet de
     * gerer l'evenement de fermeture de la fenetre ( et du programme ).
     */
    public void windowClosing(WindowEvent Tel_Evt) {
        quitter();
    }

    /**
     * La methode <B>windowClosed</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'adieu de la fenetre.
     */
    public void windowClosed(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_Jeu : Fenetre serveur Jeu fermee...");
    }

}
