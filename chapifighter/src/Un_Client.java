import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;

public class Un_Client implements KeyListener, ActionListener, Runnable {

    private static final int ID_SERVEUR_CHAT = 0;
    private static final int ID_SERVEUR_JEU = 1;
    private static final int ID_AJOUT_JOUEUR = 3;
    private static final int ID_MAJ_JOUEURS = 4;
    private static final int ID_DECONNECT = 5;
    private static final int ID_MAJ_SCORE = 7;
    private static final int ID_MAJ_VIE = 8;
    private static final int ID_MORT = 9;
    private static final int ID_FIN_JEU = 10;

    private boolean jeudejafini = false;
    private int NB_MAX_JOUEURS;
    private int Mon_Id;
    private Une_Fen_CHAT Ma_Fen_CHAT;
    private Une_Fenetre_Visu Ma_Fen_Visu;
    private Un_Joueur Mes_Joueurs[];
    private Une_Connexion_Client Ma_Connexion_Client;
    private Thread Mon_Thread;
    private DataInputStream Mon_Flux_Entree_Reseau;


    public Un_Client(String Tel_Pseudo, String Telle_Adr_Serveur, String Tel_No_Port, String Tel_Pct_Name) {
        Ma_Connexion_Client = new Une_Connexion_Client(Tel_Pseudo, Telle_Adr_Serveur, Tel_No_Port, Tel_Pct_Name);
        if (!Ma_Connexion_Client.donneConnexionAcceptee()) {
            System.err.println("Un_Client : Connexion au serveur refusee => bye...");
            System.exit(0);
        }

        Ma_Fen_CHAT = new Une_Fen_CHAT(Ma_Connexion_Client.donnePseudo());
        Ma_Fen_CHAT.Mon_Btn_Quitter.addActionListener(this);
        Ma_Fen_CHAT.Ma_Zone_Emission.addActionListener(this);
        NB_MAX_JOUEURS = Ma_Connexion_Client.donneNbMaxClients();

        Mes_Joueurs = new Un_Joueur[NB_MAX_JOUEURS];
        for (int Le_Cpt = 0; Le_Cpt < NB_MAX_JOUEURS; Le_Cpt++) {
            Mes_Joueurs[Le_Cpt] = new Un_Joueur();
            Mes_Joueurs[Le_Cpt].estActif = false;
        }

        Mon_Id = Ma_Connexion_Client.donneId();
        Mes_Joueurs[Mon_Id].estActif = true;
        Ma_Fen_CHAT.active();

        Ma_Connexion_Client.recoitJoueur(Mes_Joueurs[Mon_Id]);
        Ma_Fen_Visu = new Une_Fenetre_Visu(Mon_Id, Mes_Joueurs);
        Ma_Fen_Visu.addKeyListener(this);


        Mon_Flux_Entree_Reseau = Ma_Connexion_Client.donneFluxEntreeReseau();
        start();
    }


    void quitter() {
        finJeu();
        Ma_Fen_Visu.desactive();
        Ma_Fen_CHAT.desactive();
        System.err.println("Un_Client : Sortie du client...     bye!");
        System.exit(0);
    }

    void finJeu() {
        if (!jeudejafini) {
            jeudejafini = true;
            System.err.println("Un_Client : Envoi des messages de deconnexion...");
            Ma_Connexion_Client.deconnect();
            Ma_Connexion_Client.envoieMsg("bye!", ID_SERVEUR_CHAT);
            System.err.println("Un_Client : Sortie du client...     bye!");
            Mon_Thread.stop();
        }
    }

    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    public synchronized void run() {
        while (true)
            try {
                Mon_Thread.sleep(100);
                if (Mon_Flux_Entree_Reseau.available() != 0) {
                    String Le_Msg_Rezo = Mon_Flux_Entree_Reseau.readUTF();
                    Le_Msg_Rezo = Le_Msg_Rezo.concat(":");
                    int Le_Cpt_Str = 0;
                    switch (Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)))) {
                        case ID_SERVEUR_CHAT:
                            Le_Cpt_Str++;
                            Ma_Fen_CHAT.afficherMsg(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Msg_Rezo.length() - 1));
                            System.err.println("Un_Client : Reception d'un message CHAT...");
                            break;
                        case ID_SERVEUR_JEU:
                            Le_Cpt_Str++;
                            switch (Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)))) {
                                case ID_AJOUT_JOUEUR:
                                    Le_Cpt_Str++;
                                    int L_Id_Joueur = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[L_Id_Joueur].mon_angle = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[L_Id_Joueur].mon_x = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[L_Id_Joueur].mon_y = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[L_Id_Joueur].mon_pseudo = Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str));
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[L_Id_Joueur].pct_name = Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str));
                                    Le_Cpt_Str++;
                                    System.err.println("Un_Client : arrivee de " + Mes_Joueurs[L_Id_Joueur].mon_pseudo);
                                    Ma_Fen_Visu.addPlayer(L_Id_Joueur);
                                    Mes_Joueurs[L_Id_Joueur].estActif = true;
                                    System.err.println("Un_Client : Ajout de " + Mes_Joueurs[L_Id_Joueur].mon_pseudo + " a l ID " + L_Id_Joueur + "...");
                                    break;
                                case ID_MAJ_JOUEURS:
                                    Le_Cpt_Str++;
                                    String La_Str = Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.length());
                                    La_Str = La_Str.concat(":");
                                    int Le_Cpt_Str2 = 0;
                                    for (int Le_Cpt_Id = Integer.parseInt(La_Str.substring(Le_Cpt_Str2, Le_Cpt_Str2 = La_Str.indexOf(":", Le_Cpt_Str2))); Le_Cpt_Id > 0; Le_Cpt_Id--) {
                                        Le_Cpt_Str2++;
                                        int L_Id = Integer.parseInt(La_Str.substring(Le_Cpt_Str2, Le_Cpt_Str2 = La_Str.indexOf(":", Le_Cpt_Str2)));

                                        Le_Cpt_Str2++;
                                        int L_Angle = Integer.parseInt(La_Str.substring(Le_Cpt_Str2, Le_Cpt_Str2 = La_Str.indexOf(":", Le_Cpt_Str2)));

                                        Le_Cpt_Str2++;
                                        double La_Pos_X = Double.valueOf(La_Str.substring(Le_Cpt_Str2, Le_Cpt_Str2 = La_Str.indexOf(":", Le_Cpt_Str2))).doubleValue();

                                        Le_Cpt_Str2++;
                                        double La_Pos_Y = Double.valueOf(La_Str.substring(Le_Cpt_Str2, Le_Cpt_Str2 = La_Str.indexOf(":", Le_Cpt_Str2))).doubleValue();
                                        if (L_Id != Mon_Id) {
                                            Mes_Joueurs[L_Id].mon_angle = L_Angle;
                                            Mes_Joueurs[L_Id].mon_x = La_Pos_X;
                                            Mes_Joueurs[L_Id].mon_y = La_Pos_Y;
                                        }
                                    }
                                    break;
                                case ID_DECONNECT:
                                    Le_Cpt_Str++;
                                    int L_Id = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Mes_Joueurs[L_Id].estActif = false;
                                    System.err.println("Un_Client : Deconnexion de " + Mes_Joueurs[L_Id].mon_pseudo + "...");
                                    break;
                                case ID_MAJ_SCORE:
                                    Le_Cpt_Str++;
                                    int Le_Nv_Score = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Mes_Joueurs[Mon_Id].mon_score = Le_Nv_Score;
                                    Ma_Fen_Visu.majInfo();
                                    System.err.println("Un_Client : Nouveau score => " + Le_Nv_Score + "...");
                                    break;
                                case ID_MAJ_VIE:
                                    Le_Cpt_Str++;
                                    int La_Nvlle_Vie = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Mes_Joueurs[Mon_Id].ma_vie = La_Nvlle_Vie;
                                    Ma_Fen_Visu.majInfo();
                                    System.err.println("Un_Client : Nouvelle vie => " + La_Nvlle_Vie + "...");
                                    break;
                                case ID_MORT:
                                    Le_Cpt_Str++;
                                    System.err.println("Un_Client : Tu es mort...");
                                    for (int Le_Cpt_Attente = 0; Le_Cpt_Attente < 500; Le_Cpt_Attente++)
                                        Ma_Fen_Visu.mort();
                                    Mes_Joueurs[Mon_Id].ma_vie = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[Mon_Id].mon_angle = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[Mon_Id].mon_x = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    Le_Cpt_Str++;
                                    Mes_Joueurs[Mon_Id].mon_y = Double.valueOf(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str))).doubleValue();
                                    Ma_Fen_Visu.majInfo();
                                    break;
                                case ID_FIN_JEU:
                                    Le_Cpt_Str++;
                                    int Le_Vainqueur = Integer.parseInt(Le_Msg_Rezo.substring(Le_Cpt_Str, Le_Cpt_Str = Le_Msg_Rezo.indexOf(":", Le_Cpt_Str)));
                                    if (Le_Vainqueur != Mon_Id) {
                                        Ma_Fen_Visu.gagne(false);
                                        System.err.println("Un_Client : Partie perdu => " + Mes_Joueurs[Le_Vainqueur].mon_pseudo + " vainqueur...");
                                    } else {
                                        Ma_Fen_Visu.gagne(true);
                                        System.err.println("Un_Client : Partie gagnee...");
                                    }
                                    finJeu();
                                    break;
                                default:
                                    System.err.println("Un_Client : Reception d'un message Jeu non-identifiable...");
                                    break;
                            }
                            break;
                        default:
                            System.err.println("Un_Client : Message d'un serveur specifique inconnu...");
                            break;
                    }
                }
            } catch (Exception Telle_E) {
                System.err.println("Un_Client : Erreur lors de la reception reseau...");
            }

    }

    public synchronized void stop() {
        if (Mon_Thread != null) {
            Mon_Thread = null;
            Mon_Thread.stop();
        }
    }

    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Ma_Fen_CHAT.Mon_Btn_Quitter)
            quitter();
    }

    public void keyReleased(KeyEvent Tel_Evt) {
    }

    public void keyPressed(KeyEvent Tel_Evt) {
        int Le_Code = Tel_Evt.getKeyCode();
        switch (Le_Code) {

            case KeyEvent.VK_NUMPAD0:
                int L_Id_Victime = Ma_Fen_Visu.tire();
                if (L_Id_Victime != NB_MAX_JOUEURS) {
                    Ma_Connexion_Client.annonceTir(L_Id_Victime);
                    System.err.println("Un_Client : Shoot reussi sur " + Mes_Joueurs[L_Id_Victime].mon_pseudo + "...");
                }
                break;
            case KeyEvent.VK_NUMPAD8:
                Ma_Fen_Visu.avance();
                break;
            case KeyEvent.VK_NUMPAD6:
                Ma_Fen_Visu.droite();
                break;
            case KeyEvent.VK_NUMPAD4:
                Ma_Fen_Visu.gauche();
                break;
            case KeyEvent.VK_NUMPAD5:
                Ma_Fen_Visu.recule();
                break;
            case KeyEvent.VK_CONTROL:
                int L_Id_Victime2 = Ma_Fen_Visu.tire();
                if (L_Id_Victime2 != NB_MAX_JOUEURS) {
                    Ma_Connexion_Client.annonceTir(L_Id_Victime2);
                    System.err.println("Un_Client : Shoot reussi sur " + Mes_Joueurs[L_Id_Victime2].mon_pseudo + "...");
                }
                break;
            case KeyEvent.VK_UP:
                Ma_Fen_Visu.avance();
                break;
            case KeyEvent.VK_RIGHT:
                Ma_Fen_Visu.droite();
                break;
            case KeyEvent.VK_LEFT:
                Ma_Fen_Visu.gauche();
                break;
            case KeyEvent.VK_DOWN:
                Ma_Fen_Visu.recule();
                break;
            case KeyEvent.VK_BACK_SPACE:
                Ma_Fen_CHAT.deleteCar();
                break;
            case KeyEvent.VK_ENTER:
                Ma_Connexion_Client.envoieMsg(Ma_Fen_CHAT.lireEmission(), ID_SERVEUR_CHAT);
                break;
            default:
                Ma_Fen_CHAT.ajouteCar(Tel_Evt.getKeyChar());
                break;
        }
        try {
            Mon_Thread.sleep(10);
        } catch (Exception e) {
        }

    }

    public void keyTyped(KeyEvent Tel_Evt) {
    }

}
