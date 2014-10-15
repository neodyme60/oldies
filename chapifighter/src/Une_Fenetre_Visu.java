import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * La classe <B>Une_Fenetre_Visu</B> est la fenetre de visualisation du jeu. elle possede :<BR>
 * - Une representation de la carte (en haut a gauche) avec un petit point blanc qui indique ou on se trouve dans la carte.<BR>
 * - Les informations sur un joueur (en haut a droite) tel que sa vie,sa tete ...<BR>
 * - Le rendu 3D lui meme.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_Visu
 * @see Une_Map
 * @see Info_Joueur
 */

public class Une_Fenetre_Visu extends Frame implements Runnable, WindowListener//KeyListener,
{

    private boolean gagneperdu = false;
    private Image de_fin;
    private int bougetoncorps = 0;

    private Thread Mon_Thread;

    private int mon_ID;
    private int NB_MAX_J;
    private Un_Joueur[] mes_joueurs;

    private Une_Carte Sa_Carte;
    private Une_Map Sa_Map;
    private Info_Joueur son_info_joueur;
    private Un_Visu Son_Visu;

    private Image Buffer_map = null;
    private Graphics gBuffer_map;

    private Image Buffer_visu = null;
    private Graphics gBuffer_visu;

    private Image Buffer_info = null;
    private Graphics gBuffer_info;
    private MediaTracker Tr;
    private int TrId = 0;
    private Toolkit systemD;

    /**
     * Ce constructeur de la classe <B>Une_Fenetre_Visu</B> prend en parametre l ID du joueur dans le taleau des joueurs.
     *
     * @param tel_ID - l ID du joueur
     * @param tels_j - le tableau des joueurs
     */

    public Une_Fenetre_Visu(int tel_ID, Un_Joueur[] tels_j) {
        super("Visu de " + tels_j[tel_ID].mon_pseudo);
        addWindowListener(this);
        systemD = getToolkit();
        mon_ID = tel_ID;
        mes_joueurs = tels_j;
        NB_MAX_J = mes_joueurs.length;
        Sa_Carte = new Une_Carte();

        System.err.println("PLEASE WAIT.....");
        Tr = new MediaTracker(this);
        addPlayer(mon_ID);

        Son_Visu = new Un_Visu(tel_ID, Sa_Carte, NB_MAX_J, mes_joueurs, this);
        son_info_joueur = new Info_Joueur(mes_joueurs[mon_ID].mes_sprites[3], this);
        Sa_Map = new Une_Map(Sa_Carte);

        setSize(320, 320);
        setResizable(false);
        setVisible(true);
        setEnabled(true);

        show();

        start();

    }//constructeur


    /**
     * <B>addPlayer</B> permet de charger les 4 images d un joueur d apres son ID et son nom d image.<BR>
     * (le nom de limage a charger est un champ de la structure <B>Un_Joueur</B> )<BR>
     *
     * @param ID - l ID du joueur
     * @see Un_Joueur
     */
    public void addPlayer(int ID) {
        //TrId = 0;

        System.err.println("VISU : Chargement des images du joueur " + ID + " : *" + mes_joueurs[ID].pct_name + "*");

        mes_joueurs[ID].mes_sprites = new Image[4];

        mes_joueurs[ID].mes_sprites[0] = systemD.getImage("D_" + mes_joueurs[ID].pct_name + ".gif");
        prepareImage(mes_joueurs[ID].mes_sprites[0], null);
        Tr.addImage(mes_joueurs[ID].mes_sprites[0], 0);
        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }

        System.err.print(".");
        mes_joueurs[ID].mes_sprites[1] = systemD.getImage("P_" + mes_joueurs[ID].pct_name + ".gif");
        prepareImage(mes_joueurs[ID].mes_sprites[1], null);
        Tr.addImage(mes_joueurs[ID].mes_sprites[1], 0);
        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }

        System.err.print(".");
        mes_joueurs[ID].mes_sprites[2] = systemD.getImage("F_" + mes_joueurs[ID].pct_name + ".gif");
        prepareImage(mes_joueurs[ID].mes_sprites[2], null);
        Tr.addImage(mes_joueurs[ID].mes_sprites[2], 0);
        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }

        System.err.print(".");
        mes_joueurs[ID].mes_sprites[3] = systemD.getImage("V_" + mes_joueurs[ID].pct_name + ".gif");
        prepareImage(mes_joueurs[ID].mes_sprites[3], null);
        Tr.addImage(mes_joueurs[ID].mes_sprites[3], 0);
        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }

        System.err.print(".");
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("VISU : Images OK...");

        //mes_joueurs[ID].estActif = true ;

    }


    public void paint(Graphics telle_zone) {

        //init des buffer offscreen
        if (Buffer_visu == null) {
            Buffer_visu = createImage(320, 200);
            gBuffer_visu = Buffer_visu.getGraphics();
            prepareImage(Buffer_visu, null);
            System.err.println("INIT buffer_visu");
        }
        if (Buffer_map == null) {
            Buffer_map = createImage(160, 100);
            gBuffer_map = Buffer_map.getGraphics();
            prepareImage(Buffer_map, null);
            System.err.println("INIT buffer_map");
        }
        if (Buffer_info == null) {
            Buffer_info = createImage(160, 100);
            gBuffer_info = Buffer_info.getGraphics();
            prepareImage(Buffer_info, null);
            System.err.println("INIT buffer_info_joueur");
        }

        son_info_joueur.refresh(mes_joueurs[mon_ID].ma_vie, mes_joueurs[mon_ID].mon_score, gBuffer_info);
        Sa_Map.refresh(mes_joueurs[mon_ID].mon_x, mes_joueurs[mon_ID].mon_y, gBuffer_map);
        Son_Visu.refresh(gBuffer_visu);

        update(telle_zone);
    }


    public void update(Graphics telle_zone) {
        Son_Visu.refresh(gBuffer_visu);

        if (gagneperdu) {
            //System.err.println( de_fin.getWidth(null)+","+de_fin.getHeight(null) );

            gBuffer_visu.drawImage(de_fin, 0, 0, null);
        }
        telle_zone.drawImage(Buffer_visu, 0, 120, null);
        telle_zone.drawImage(Buffer_map, 0, 20, null);
        telle_zone.drawImage(Buffer_info, 160, 20, null);

    }

    public void desactive() {
        removeNotify();
    }

    /**
     * <B>avance</B> permet de faire avancer le joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public void avance() {
        Son_Visu.gestionClavier('8');
        Sa_Map.refresh(mes_joueurs[mon_ID].mon_x, mes_joueurs[mon_ID].mon_y, gBuffer_map);
    }

    /**
     * <B>recule</B> permet de faire reculer le joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public void recule() {
        Son_Visu.gestionClavier('5');
        Sa_Map.refresh(mes_joueurs[mon_ID].mon_x, mes_joueurs[mon_ID].mon_y, gBuffer_map);
    }

    /**
     * <B>droite</B> permet une rotation a droite du joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public void droite() {
        Son_Visu.gestionClavier('6');
        Sa_Map.refresh(mes_joueurs[mon_ID].mon_x, mes_joueurs[mon_ID].mon_y, gBuffer_map);
    }

    /**
     * <B>gauche</B> permet une rotation a gauche du joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public void gauche() {
        Son_Visu.gestionClavier('4');
        Sa_Map.refresh(mes_joueurs[mon_ID].mon_x, mes_joueurs[mon_ID].mon_y, gBuffer_map);
    }

    /**
     * <B>tire</B> permet de faire tirer le joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public int tire() {
        int dumy;
        Son_Visu.tire = true;
        dumy = Son_Visu.IDPlayer_vise;
        if (dumy == 99) dumy = NB_MAX_J;
        return dumy;
    }

    /**
     * <B>tire</B> permet de faire tirer le joueur.
     * appelle <B>gestionClavier</B> de la class Un_Visu.
     *
     * @see Un_Visu#gestionClavier(char)
     */
    public void mort() {
        gBuffer_info.setColor(new Color(255, 0, 0));
        gBuffer_info.fillRect(0, 0, 160, 100);
    }

    /**
     * <B>majInfo</B> permet la mise a jour de la visualisation des informations sur le joueur.
     *
     * @see Info_joueur#refresh(int, int, Graphics)
     */
    public void majInfo() {
        son_info_joueur.refresh(mes_joueurs[mon_ID].ma_vie, mes_joueurs[mon_ID].mon_score, gBuffer_info);
    }

    /**
     * <B>gagne</B> permet d afficher si le joueur a gagne (true) ou perdu (false).
     */
    public void gagne(boolean quoi) {
        if (quoi)
            de_fin = systemD.getImage("gagne.gif");
        else
            de_fin = systemD.getImage("perdu.gif");

        prepareImage(de_fin, null);
        Tr.addImage(de_fin, 0);
        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }
        gagneperdu = true;
    }

    public void windowIconified(WindowEvent tel_Evt) {
    }

    public void windowDeiconified(WindowEvent tel_Evt) {
    }

    public void windowActivated(WindowEvent tel_Evt) {
    }

    public void windowDeactivated(WindowEvent tel_Evt) {
    }

    public void windowClosed(WindowEvent tel_Evt) {
    }

    public void windowOpened(WindowEvent tel_Evt) {
        System.err.println("Opened");
    }

    public void windowClosing(WindowEvent tel_Evt) {
        System.exit(0);
    }


    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer la fenetre en <B>Thread</B> des la fin de son initialisation.
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> est la boucle permanente qui permet de rafrechir le rendu 3D toutes les 0.2sec .
     */
    public synchronized void run() {

        System.err.println("_.oO GO! Oo._");

        while (true) {
            repaint();
            try {
                Mon_Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }

    }

    /**
     * La methode <B>stop</B> de l'interface <B>Runnable</B> permet d'arreter la tache.
     *
     * @see Un_Serveur_CHAT
     */
    public synchronized void stop() {
        if (Mon_Thread != null) {
            Mon_Thread = null;
            Mon_Thread.stop();
        }
    }

}//class Une_Fenetre_Visu
