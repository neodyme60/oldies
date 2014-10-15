import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * <B>Classe <U>Un_menu</U> :</B><BR><BR>
 * <p/>
 * Le principe consiste a creer un buffer que l on affichera dans la Frame. Ce buffer contient le menu par lui meme.<BR>
 * <BR>
 * Sur la fenetre on affiche une image de fond et un bouton d AIDE (selon le menu dans lequel on est).<BR>
 * <BR>
 * Tout le reste n est en fait que la gestion de ce qui est clique, de ce que cela engendre (scrolling montee-descente<BR>
 * du menu et affichage d un autre menu, affichage d une boite de dialogue, Quitter ,etc.).<BR>
 * <BR>
 * <B>Les options cliquees :</B><BR>
 * <BR>
 * <U>MENU Chapi Fighter (principal)</U><BR>
 * <I># Lancer serveur :</I><BR>
 * affichage du sous-menu Lancer serveur remontee du menu principale + creation du menu suivant dans le buffer + descente<BR>
 * du sous-menu Lancer serveur.<BR>
 * <I># Lancer client  :</I><BR>
 * affichage du sous-menu Lancer client remontee du menu principale + creation du menu suivant dans le buffer + descente<BR>
 * du sous-menu Lancer client.<BR>
 * <I># Quitter        :</I><BR>
 * affichage du sous-menu Quitter remontee du menu principale + creation du menu suivant dans le buffer + descente<BR>
 * du sous-menu Quitter<BR>
 * <BR>
 * <U>MENU Lancer Serveur</U><BR>
 * <I># Info serveur   :</I><BR>
 * appel a la classe Dialogue_serveur on precise que l on est dans une boite de dialogue pour que les clics dans la classe mere<BR>
 * n interferent pas et que l on est rentre au moins une fois dans cette boite (voir option Lancer). On lance une boite de<BR>
 * dialogue-serveur, et quand on en a fini avec cette boite (que l on quitte par clic sur un des boutons de la boite), on stocke les<BR>
 * differentes valeurs modifiees puis la boite est detruite.<BR>
 * On rafraichit l affichage de la fenetre du menu (niet=true --> affichage d un menu statique)<BR>
 * <I># Lancer         :</I><BR>
 * lance un serveur. Cette option n est activee que si la boite de dialogue d info du rezo serveur a ete visitee au moins une fois<BR>
 * <I># Annuler        :</I><BR>
 * retour au menu precedent, indice du menu =indice du menu precedent, montee du menu Lancer serveur + descente du menu principale.<BR>
 * <BR>
 * <U>MENU Lancer Client</U><BR>
 * <I># Choix Joueur   :</I><BR>
 * appel a la classe Choix_joueur on precise que l on est dans une boite de dialogue pour que les clics dans la classe mere<BR>
 * n interferent pas et que l on est rentre au moins une fois dans cette boite (voir option Lancer). On lance une boite de <BR>
 * dialogue-choix joueur, et quand on en a fini avec cette boite (que l on quitte par clic sur un des boutons de la boite), on stocke<BR>
 * les differentes valeurs modifiees puis la boite est detruite.<BR>
 * On rafraichit l affichage de la fenetre du menu (niet=true --> affichage d un menu statique)<BR>
 * <I># Info REZO      :</I><BR>
 * appel a la classe Dialogue_info_rezo on precise que l on est dans une boite de dialogue pour que les clics dans la classe mere<BR>
 * n interferent pas et que l on est rentre au moins une fois dans cette boite (voir option Lancer).On lance une boite de dialogue-client,
 * et quand on en a fini avec cette boite (que l on quitte par clic sur un des boutons de la boite), on stocke les differentes valeurs
 * modifiees puis la boite est detruite.<BR>
 * On rafraichit l affichage de la fenetre du menu (niet=true --> affichage d un menu statique)<BR>
 * <I>     # Lancer         :</I><BR>
 * lance un client. Cette option n est activee que si la boite de dialogue d info du rezo et la boite de dialogue de choix du joueur<BR>
 * ont etes visitees au moins une fois chacune.<BR>
 * <I>     # Annuler        :</I><BR>
 * retour au menu precedent indice du menu =indice du menu precedent, montee du menu Lancer client + descente du menu principale.<BR>
 * <BR>
 * <U>MENU Quitter</U><BR>
 * <I>     # Adios          :</I><BR>
 * arrete le programme, c est la fin du MONDE, c est quitter un jeu d une ambition extraordinaire.<BR>
 * <I>     # Annuler        :</I><BR>
 * retour au menu precedent indice du menu =indice du menu precedent, remontee du menu Quitter + creation du menu suivant dans le buffer
 * + descente menu principale.<BR>
 * <BR>
 * <U>BOUTON AIDE</U><BR>
 * Ce bouton n est apparent et activable que dans 3 menus ( le principal, le Lancer serveur, Le Lancer client).
 * Appel a la classe Une_Aide; on precise que l on est dans une boite de dialogue pour que les clics dans la classe mere n interferent pas. <BR>
 * On lance une boite de dialogue-aide, et quand on en a fini avec cette boite (que l on quitte par clic sur le bouton Quiiter puis la boite est detruite.<BR>
 * On rafraichit l affichage de la fenetre du menu (niet=true --> affichage d un menu statique)
 * <BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Dialogue_info_rezo,Dialogue_serveur,Un_Canvas,Une_Aide,Un_panneau,Choix_joueur
 */

public class Un_menu extends Frame implements MouseListener, Runnable {
    public int mon_indice = 0,
            mes_tues = 10,
            mon_nombre_clts = 5;

    public String mon_port_serveur = "5000", mon_no_port = "5000", mon_adr_serveur = "venus28", Le_Pseudo = "tutu", Le_Perso = "apsi";

    private MediaTracker tracker;

    private Thread Mon_thread = null;

    private int mouse_x,
            mouse_y,
            indice_menu = 0,
            prec = 0,
            LG_SCREEN = 250,
            HT_SCREEN = 250;

    private Image flingo = null;
    private Image buffer = null;
    private Graphics gbuffer;

    // Dimensions des cases d un menu
    private int LG = 100;
    private int HT = 20;

    private boolean au_moins_une_fois_CJ = false,
            au_moins_une_fois_S = false,
            au_moins_une_fois_C = false,
            dans_dlg_box = false,
            clic = false,
            clic_aide = false,
            niet = false,
            montee = false,
            descente = false,
            debut = false,
            images_ok = false;


    private String[] Mon_menu_princ = {"Lancer serveur", "Lancer client", "Quitter"};
    private String[] Mon_menu_server = {"Info serveur", "Lancer", "Annuler"};
    private String[] Mon_menu_client = {"Choix joueur", "Info REZO", "Lancer", "Annuler"};
    private String[] Mon_menu_quitter = {"Adios", "Annuler"};
    private Un_panneau Chapi, Server, Client, Quitter;
    private Un_panneau[] Mon_tab_menu;

    private Dialogue_serveur dlg_serv;
    private Dialogue_info_rezo dlg_info_rezo;
    private Choix_Joueur Mon_Choix_Joueur;
    private Une_Aide mon_aide;


    public Un_menu() {
        super("Le menu");
        Chapi = new Un_panneau("Chapi Fighter", Mon_menu_princ, Mon_menu_princ.length);
        Server = new Un_panneau("Lancer serveur", Mon_menu_server, Mon_menu_server.length);
        Client = new Un_panneau("Lancer client", Mon_menu_client, Mon_menu_client.length);
        Quitter = new Un_panneau("Quitter", Mon_menu_quitter, Mon_menu_quitter.length);

        Mon_tab_menu = new Un_panneau[4];
        Mon_tab_menu[0] = Chapi;
        Mon_tab_menu[1] = Server;
        Mon_tab_menu[2] = Client;
        Mon_tab_menu[3] = Quitter;

        addMouseListener(this);

        setSize(LG_SCREEN, HT_SCREEN);
        setResizable(false);
        show();
        start();
    }

    public static void main(String argv[]) {
        Un_menu le_menu = new Un_menu();
    }

    public synchronized void start() {
        if (Mon_thread == null) {
            Mon_thread = new Thread(this);
            Mon_thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_thread.start();
        }
    }

    public synchronized void stop() {
        if (Mon_thread != null) {
            Mon_thread.stop();
            Mon_thread = null;
        }
    }

    public synchronized void run() {
        while (!images_ok) ;
        while (true) {
            if (clic_aide == true) {
                dans_dlg_box = true;
                switch (indice_menu) {
                    case 0:
                        mon_aide = new Une_Aide(this, "aide_principale");
                        break;
                    case 1:
                        mon_aide = new Une_Aide(this, "aide_serveur");
                        break;
                    case 2:
                        mon_aide = new Une_Aide(this, "aide_client");
                        break;
                    default:
                        break;
                }
                while (!mon_aide.pret) {
                    try {
                        Mon_thread.sleep(50);
                    } catch (Exception e) {
                    }
                }

                mon_aide.pret = false;
                niet = true;
                repaint();
                while (niet) ;
                clic_aide = false;
                dans_dlg_box = false;
            }
            // si il y a eu clic d une option
            if (clic == true && dans_dlg_box == false) {
                if ((Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Lancer") == 0) && (Mon_tab_menu[indice_menu].donne_titre().compareTo("Lancer serveur") == 0) && (au_moins_une_fois_S == true)) {
                    System.out.println("Lancer partie serveur ................");
                    Un_Serveur go = new Un_Serveur(mon_port_serveur, mes_tues, mon_nombre_clts);
                    removeNotify();
                    dispose();

                }

                if ((Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Lancer") == 0) && (Mon_tab_menu[indice_menu].donne_titre().compareTo("Lancer client") == 0) && (au_moins_une_fois_C == true) && (au_moins_une_fois_CJ == true)) {
                    System.out.println("Lancer partie client ................");
                    System.err.println(Le_Pseudo + " " + mon_adr_serveur + " " + mon_no_port + " " + Le_Perso);
                    Un_Client go = new Un_Client(Le_Pseudo, mon_adr_serveur, mon_no_port, Le_Perso);
                    removeNotify();
                    dispose();
                }

                if (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Choix joueur") == 0) {
                    dans_dlg_box = true;
                    au_moins_une_fois_CJ = true;
                    Mon_Choix_Joueur = new Choix_Joueur(this, Le_Pseudo, Le_Perso, mon_indice);
                    while (!Mon_Choix_Joueur.Pret) {
                        try {
                            Mon_thread.sleep(50);
                        } catch (Exception e) {
                        }
                    }
                    Le_Pseudo = Mon_Choix_Joueur.Le_Pseudo;
                    Le_Perso = Mon_Choix_Joueur.Le_Perso;
                    mon_indice = Mon_Choix_Joueur.Indice;
                    Mon_Choix_Joueur.Pret = false;
                    setEnabled(true);
                    dans_dlg_box = false;
                    niet = true;
                    repaint();
                }

                if (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Info serveur") == 0) {
                    dans_dlg_box = true;
                    au_moins_une_fois_S = true;
                    dlg_serv = new Dialogue_serveur(this, mon_port_serveur, mon_nombre_clts, mes_tues);
                    while (!dlg_serv.mon_bouton_appuye) {
                        try {
                            Mon_thread.sleep(50);
                        } catch (Exception e) {
                        }
                    }
                    mon_port_serveur = dlg_serv.port_serveur;
                    mon_nombre_clts = dlg_serv.nombre_clients;
                    mes_tues = dlg_serv.nombre_tues;
                    dlg_serv.mon_bouton_appuye = false;
                    setEnabled(true);
                    dans_dlg_box = false;
                    niet = true;
                    repaint();
                }
                if (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Info REZO") == 0) {
                    dans_dlg_box = true;
                    au_moins_une_fois_C = true;
                    dlg_info_rezo = new Dialogue_info_rezo(this, mon_no_port, mon_adr_serveur);
                    while (!dlg_info_rezo.mon_save_appuye) {
                        try {
                            Mon_thread.sleep(50);
                        } catch (Exception e) {
                        }
                    }
                    mon_no_port = dlg_info_rezo.port_guet;
                    mon_adr_serveur = dlg_info_rezo.ad_serveur;
                    dlg_info_rezo.mon_save_appuye = false;
                    System.out.println("reprise main");
                    setEnabled(true);
                    System.out.println("setEnabled(true)");
                    dans_dlg_box = false;
                    niet = true;
                    repaint();
                }

                if (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Adios") == 0) {
                    System.exit(0);
                }

                if ((Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Lancer") != 0)
                        && (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Choix joueur") != 0)
                        && (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Info REZO") != 0)
                        && (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Info serveur") != 0)
                        && (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Adios") != 0)) {
                    montee = true;
                    repaint();
                    while (montee) ;

                    if (Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT)).compareTo("Annuler") == 0) {
                        indice_menu = prec;
                    }//if
                    else {
                        //calcul menu suivant
                        for (int e = 0; e < Mon_tab_menu.length; e++) {
                            if (Mon_tab_menu[e].compare(Mon_tab_menu[indice_menu].donne_option((int) ((double) (mouse_y - 105 - HT) / (double) HT))) == true) {
                                prec = indice_menu;
                                indice_menu = e;
                                break;
                            }//if
                        }//for
                    }//else
                    //descente menu suivant
                    gbuffer.drawImage(flingo, 0, 0, LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), 96, 102, 96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), null);

                    gbuffer.setColor(Color.black);
                    try {
                        Mon_thread.sleep(50);
                    } catch (Exception e) {
                    }
                    gbuffer.drawRect(0, 0, LG, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 1);
                    gbuffer.setColor(Color.green);
                    gbuffer.drawString(Mon_tab_menu[indice_menu].donne_titre(), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_titre().length()) / 2.0), HT);
                    gbuffer.setColor(Color.black);
                    for (int p = 1; p <= Mon_tab_menu[indice_menu].donne_taille(); p++) {
                        gbuffer.drawLine(0, p * HT + p, LG + 1, p * HT + p);
                    }//for
                    gbuffer.setColor(Color.red);
                    for (int b = 0; b < Mon_tab_menu[indice_menu].donne_taille(); b++) {
                        if ((indice_menu == 1 && b == 1 && au_moins_une_fois_S == false) | (indice_menu == 2 && b == 2 && (au_moins_une_fois_C == false | au_moins_une_fois_CJ == false))) {
                            gbuffer.setColor(Color.black);
                            gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
                            gbuffer.setColor(Color.red);
                        } else {
                            gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
                        }

                    }//for
                    gbuffer.setColor(Color.red);
                    gbuffer.drawLine(0, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2, 101, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2);
                    descente = true;
                    repaint();
                    while (descente) ;

                }//if
                clic = false;
            }//if
        }

    }

    public void paint(Graphics g) {
        tracker = new MediaTracker(this);
        flingo = getToolkit().getImage("Fling_menu.jpg");
        tracker.addImage(flingo, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Probleme chargement image!!!!");
        }
        buffer = createImage(LG + 2, 106);
        tracker.addImage(buffer, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Probleme chargement image!!!!");
        }
        images_ok = true;

        gbuffer = buffer.getGraphics();

        System.out.println("...Initialisation menu...");
        System.out.println("Image flingo OK ..........");
        try {
            Mon_thread.sleep(50);
        } catch (Exception e) {
        }
        gbuffer.drawImage(flingo, 0, 0, LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), 96, 102, 96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), null);
        gbuffer.setColor(Color.black);
        gbuffer.drawRect(0, 0, LG, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 1);
        gbuffer.setColor(Color.green);
        gbuffer.drawString(Mon_tab_menu[indice_menu].donne_titre(), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_titre().length()) / 2.0), HT);
        gbuffer.setColor(Color.black);
        //lignes separatrices
        for (int p = 1; p <= Mon_tab_menu[indice_menu].donne_taille(); p++) {
            gbuffer.drawLine(0, p * HT + p, LG + 1, p * HT + p);
        }//for
        //options du menu
        gbuffer.setColor(Color.red);
        for (int b = 0; b < Mon_tab_menu[indice_menu].donne_taille(); b++) {
            if ((indice_menu == 1 && b == 1 && au_moins_une_fois_S == false) | (indice_menu == 2 && b == 2 && (au_moins_une_fois_C == false | au_moins_une_fois_CJ == false))) {
                gbuffer.setColor(Color.black);
                gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
                gbuffer.setColor(Color.red);
            } else {
                gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
            }
        }//for
        gbuffer.setColor(Color.black);
        gbuffer.drawLine(0, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), 101, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2));

        niet = true;
        repaint();
    }

    public void update(Graphics tel_g) {
        tel_g.drawImage(flingo, 0, 0, null);
        tel_g.setColor(Color.black);

        if (niet == true) {
            if ((au_moins_une_fois_S == true) || ((au_moins_une_fois_CJ == true) && (au_moins_une_fois_C == true))) {
                try {
                    Mon_thread.sleep(200);
                } catch (Exception e) {
                }
                gbuffer.drawImage(flingo, 0, 0, LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), 96, 102, 96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), null);
                gbuffer.setColor(Color.black);
                gbuffer.drawRect(0, 0, LG, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 1);
                gbuffer.setColor(Color.green);
                gbuffer.drawString(Mon_tab_menu[indice_menu].donne_titre(), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_titre().length()) / 2.0), HT);
                gbuffer.setColor(Color.black);
                //lignes separatrices
                for (int p = 1; p <= Mon_tab_menu[indice_menu].donne_taille(); p++) {
                    gbuffer.drawLine(0, p * HT + p, LG + 1, p * HT + p);
                }//for
                //options du menu
                gbuffer.setColor(Color.red);
                for (int b = 0; b < Mon_tab_menu[indice_menu].donne_taille(); b++) {
                                        /* si l option a ecrire est Lancer (soit serveur
                                                                            soit client)
                                           selon que l option est activee ou pas
                                           on l ecrit reciproquement en noir ou en rouge
                                           les autres options sont toujours en rouge
                                        */
                    if ((indice_menu == 1 && b == 1 && au_moins_une_fois_S == false) | (indice_menu == 2 && b == 2 && (au_moins_une_fois_C == false | au_moins_une_fois_CJ == false))) {
                        gbuffer.setColor(Color.black);
                        gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
                        gbuffer.setColor(Color.red);
                    } else {
                        gbuffer.drawString(Mon_tab_menu[indice_menu].donne_option(b), (int) ((double) (LG - 5 * Mon_tab_menu[indice_menu].donne_option(b).length()) / 2.0), (b + 2) * HT);
                    }
                }//for
                gbuffer.setColor(Color.black);
                gbuffer.drawLine(0, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2), 101, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2));
                tel_g.drawImage(buffer,
                        96, 102,
                        96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2,
                        0, 0,
                        LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2,
                        null);

            } else {
                try {
                    Mon_thread.sleep(200);
                } catch (Exception e) {
                }
                tel_g.drawImage(buffer,
                        96, 102,
                        96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2,
                        0, 0,
                        LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2,
                        null);
            }
            niet = false;
        }
        if (indice_menu == 0 | indice_menu == 1 | indice_menu == 2) {
            tel_g.drawRect(210, 220, 30, 10);
            tel_g.setColor(Color.red);
            tel_g.drawString("AIDE", 212, 230);
        }
        if (montee == true) {
            //-->remontee
            for (int r = 0; r <= (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT; r++) {
                for (int la_bcl = 0; la_bcl < 10000; la_bcl++) {
                }//for
                tel_g.drawImage(buffer,
                        96, 102,
                        96 + LG + 1, 102 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2 - r,
                        0, r,
                        LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + Mon_tab_menu[indice_menu].donne_taille() + 2,
                        null);
            }//for
            montee = false;
        }//if
        if (descente == true) {
            for (int rr = 0; rr <= (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT; rr++) {
                for (int la_bcl2 = 0; la_bcl2 < 10000; la_bcl2++) {
                }//for
                tel_g.drawImage(buffer,
                        96, 102,
                        96 + LG + 1, 102 + rr,
                        0, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2) - rr,
                        LG + 1, (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT + (Mon_tab_menu[indice_menu].donne_taille() + 2),
                        null);

            }//for
            descente = false;
        }//if
    }//update

    public void mouseReleased(MouseEvent Tel_Evt) {
        if (!dans_dlg_box) {
            mouse_x = Tel_Evt.getX();
            mouse_y = Tel_Evt.getY();
            if ((mouse_x >= 100) && (mouse_x <= 200) && (mouse_y >= (105 + HT)) && (mouse_y <= (105 + (Mon_tab_menu[indice_menu].donne_taille() + 1) * HT))) {
                clic = true;
            } else {
                clic = false;
            }
            if (mouse_x >= 210 && mouse_x <= 240 && mouse_y >= 220 && mouse_y <= 230 && (indice_menu == 0 | indice_menu == 1 | indice_menu == 2)) {
                clic_aide = true;
            } else {
                clic_aide = false;
            }
        }
    }

    public void mousePressed(MouseEvent Tel_Evt) {
    }

    public void mouseClicked(MouseEvent Tel_Evt) {
    }

    public void mouseEntered(MouseEvent Tel_Evt) {
    }

    public void mouseExited(MouseEvent Tel_Evt) {
    }
}
