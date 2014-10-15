import java.awt.*;


/**
 * classe <B>Un_Visu</B>
 * cette classe calcule la vision du joueur suivant les actions effectu�es
 * au clavier.
 *
 * @author Chapi-developpement group
 * @version 3.0
 * @see Une_Fenetre_Visu
 */


public class Un_Visu {

    private static int X_REZO = 320;
    private double[] preka_correction = new double[X_REZO];
    private int[] z_buffer = new int[X_REZO];
    private static int Y_REZO = 200;
    private static int COEF_ANGLE = 5;
    private static int NBR_ANGLE = 1024;
    private double[] preka_sinus = new double[NBR_ANGLE];
    private double[] preka_cosinus = new double[NBR_ANGLE];
    private double[] preka_tan = new double[NBR_ANGLE];
    private static int PROFONDEUR_FOV = 256;
    private static int FOV = 64;
    //(int)(2.0*Math.atan2(PROFONDEUR_FOV,X_REZO/(2.0*256.0)));
    /**
     * La variable tire permet de savoir si l'utilisateur a tire
     * Par defaut a false elle est mise a true chaque fois que
     * la touche 'CONTROL' est press�e
     */

    public boolean tire = false;
    /**
     * La variable IDPlayer est un entier qui contient l'identite
     * du joueur. Par defaut a 99 sa valeur est changee des que
     * le booleen tire est a true
     */
    public int IDPlayer_vise = 99;
    private Un_Joueur[] mes_joueurs;
    private int NB_MAX_J;
    private int mon_ID;
    //******************variables globales
    private double x1, y1;
    private double xx1, yy1;
    private double x_back, y_back;
    private double dx, dy;
    private Image fond = null;
    private Image fling = null;
    private Image flamme = null;

    private Une_Carte ma_carte;
    private MediaTracker Tr;
    private int TrId = 0;


    /**
     * Le constructeur de la classe <B>Un_Visu</B> prend en parametres
     *
     * @param int         tel_ID -l'identificateur du joueur
     * @param Une_Carte   la carte -la carte
     * @param int         nb -le nombre maximal de joueurs pouvant etre connectes
     * @param Un_Joueur[] les_joueurs -le tableau des joueurs
     * @param Frame       maman -la fenetre parente
     */


    public Un_Visu(int tel_ID, Une_Carte la_carte, int nb, Un_Joueur[] les_joueurs, Frame maman) {
        Tr = new MediaTracker(maman);
        NB_MAX_J = nb;
        mes_joueurs = les_joueurs;
        mon_ID = tel_ID;
        ma_carte = la_carte;

        Toolkit systemD = maman.getToolkit();

        System.err.println("Un_Visu : Chargement des Images ...");

        fond = systemD.getImage("back.jpg");
        Tr.addImage(fond, TrId++);

        fling = systemD.getImage("flingo.gif");
        Tr.addImage(fling, TrId++);

        flamme = systemD.getImage("flamme.gif");
        Tr.addImage(flamme, TrId++);

        System.err.println("Un_Visu : Chargement des Images OK!");

        precalcule();
        try {
            Tr.waitForAll();
        } catch (InterruptedException e) {
            return;
        }
    }


    /**
     * Methode <B>refresh</B>
     * Cette methode fait appel aux methodes <B>calcule_z_buffer()</B>
     * <B>drawWall(Graphics)</B> et <B>drawPlayer(Graphics)</B>
     *
     * @param Graphics g
     */


    public void refresh(Graphics g) {

        calcule_zbuffer();
        drawWall(g);
        drawPlayer(g);
    }


    private void drawWall(Graphics g) {
        int x, y;
        int i;
        int j;
        int k;
        int h;
        int color;

        g.drawImage(fond, 0, 0, null);

        for (i = 0; i < X_REZO; i++) {
            j = z_buffer[i] + 1;


            j = (int) (64 * PROFONDEUR_FOV / ((double) j * preka_correction[i]));

            color = j;

            if (j > Y_REZO)
                j = Y_REZO;

            if (color > 255)
                color = 255;

            g.setColor(new Color(0, 0, color));
            g.drawLine(i, (int) (Y_REZO / 2.0 - j / 2), i, (int) (Y_REZO / 2.0 + j / 2));

        }

    }//class drawWall


    private void drawPlayer(Graphics g) {
        int no_image, no_cadran;
        double angle;
        double angle_gauche;
        double angle_droit;
        double temp, coef;
        int i, cpt_j;
        int y_pos_des_pieds, x_pos_z, x_g_z, x_d_z;
        int x, y, h, l, Mx, My, Mh, Ml;
        double z_player;
        Pour_le_tri les_z[] = new Pour_le_tri[NB_MAX_J];
        Pour_le_tri aux;


        IDPlayer_vise = 99;

        for (i = 0; i < NB_MAX_J; i++)
            if (mes_joueurs[i].estActif)
                les_z[i] = new Pour_le_tri(i, Math.sqrt(Math.pow(mes_joueurs[i].mon_x - mes_joueurs[mon_ID].mon_x, 2.0) + Math.pow(mes_joueurs[i].mon_y - mes_joueurs[mon_ID].mon_y, 2.0)));
            else
                les_z[i] = new Pour_le_tri(i, 0);


        for (cpt_j = 0; cpt_j < NB_MAX_J - 1; cpt_j++)
            for (i = NB_MAX_J - 2; i >= cpt_j; i--)
                if (les_z[i].z < les_z[i + 1].z) {
                    aux = les_z[i + 1];
                    les_z[i + 1] = les_z[i];
                    les_z[i] = aux;
                }


        cpt_j = 0;

        while ((cpt_j < NB_MAX_J) && (les_z[cpt_j].z > 10)) {

            angle = Math.atan2(mes_joueurs[les_z[cpt_j].id].mon_y - mes_joueurs[mon_ID].mon_y, mes_joueurs[les_z[cpt_j].id].mon_x - mes_joueurs[mon_ID].mon_x) * 180.0 / Math.PI;

            if (angle < 0)
                angle = 360 + angle; //car atan2 donne entre -pi et +pi

            angle_gauche = ((mes_joueurs[mon_ID].mon_angle + FOV / 2) + 360) % 360;
            angle_droit = ((mes_joueurs[mon_ID].mon_angle - FOV / 2) + 360) % 360;

            angle = (angle - angle_droit + 360) % 360;

            if (FOV > angle) {
                //quel cadran & image?
                //no_cadran = (int)angle/45;
                no_cadran = ((int) (mes_joueurs[les_z[cpt_j].id].mon_angle - mes_joueurs[mon_ID].mon_angle) + 360) % 360;
                no_cadran /= 45;

                if (no_cadran == 7)
                    no_image = 0;
                else
                    no_image = (no_cadran + 1) / 2;

                if (no_image == 3)
                    no_image = 1;

                temp = (int) (FOV - angle);
                x_pos_z = (int) (temp * 320.0 / FOV);

                x = mes_joueurs[les_z[cpt_j].id].mes_sprites[no_image].getWidth(null);
                y = mes_joueurs[les_z[cpt_j].id].mes_sprites[no_image].getHeight(null);

                h = (int) (35 * PROFONDEUR_FOV / les_z[cpt_j].z);
                y_pos_des_pieds = (Y_REZO + (int) (64 * PROFONDEUR_FOV / les_z[cpt_j].z)) / 2;

                coef = (double) y / (double) h;

                l = (int) (x / coef);
                Mx = x / 2;
                Ml = l / 2;

                x_g_z = x_pos_z - Ml;
                x_d_z = x_pos_z + Ml;

                if (x_g_z < 0)
                    x_g_z = 0;

                if (x_d_z > X_REZO - 1)
                    x_d_z = X_REZO - 1;

                i = x_g_z;
                while (z_buffer[i] < les_z[cpt_j].z && i < x_d_z)
                    i++;
                x_g_z = i;

                i = x_d_z;
                while (z_buffer[i] < les_z[cpt_j].z && i > x_g_z)
                    i--;
                x_d_z = i;


                if (x_g_z < X_REZO / 2 + 3 && x_d_z > X_REZO / 2 - 3)
                    IDPlayer_vise = les_z[cpt_j].id;


                if (x_g_z < x_d_z) {
                    switch (no_cadran) {
                        //pour le profile droit il faut recopier a l'envers
                        //l image du profile gauche
                        case 5:
                            g.drawImage(mes_joueurs[les_z[cpt_j].id].mes_sprites[no_image], x_g_z, y_pos_des_pieds - h, x_d_z, y_pos_des_pieds, Mx + (int) ((x_pos_z - x_g_z) * coef), 0, Mx - (int) ((x_d_z - x_pos_z) * coef), y, null);
                            break;
                        case 6:
                            g.drawImage(mes_joueurs[les_z[cpt_j].id].mes_sprites[no_image], x_g_z, y_pos_des_pieds - h, x_d_z, y_pos_des_pieds, Mx + (int) ((x_pos_z - x_g_z) * coef), 0, Mx - (int) ((x_d_z - x_pos_z) * coef), y, null);
                            break;
                        //pour les autres faces on a les images
                        default:
                            g.drawImage(mes_joueurs[les_z[cpt_j].id].mes_sprites[no_image], x_g_z, y_pos_des_pieds - h, x_d_z, y_pos_des_pieds, Mx - (int) ((x_pos_z - x_g_z) * coef), 0, Mx + (int) ((x_d_z - x_pos_z) * coef), y, null);
                            break;
                    }
                }
            }
            cpt_j++;
        }

        //affiche le colt
        if (tire) {
            g.drawImage(flamme, 160 - 14, 143, null);
            g.setColor(new Color(255, 0, 0));
            g.drawLine(160, 142, 160, 100);
            tire = false;
        }
        g.drawImage(fling, 160 - 16, 160, null);

    }//drawPlayer


    private void precalcule() {
        //************variable
        int i;

        //***********table sinus
        for (i = 0; i < NBR_ANGLE; i++)
            preka_sinus[i] = Math.sin(i * 2.0 * Math.PI / NBR_ANGLE);

        //***********table cosinus
        for (i = 0; i < NBR_ANGLE; i++)
            preka_cosinus[i] = Math.cos(i * 2.0 * Math.PI / NBR_ANGLE);

        //***********tan
        for (i = 0; i < 1024; i++)
            preka_tan[i] = Math.tan(i * 2.0 * Math.PI / NBR_ANGLE);

        //************correction
        for (i = 0; i < X_REZO; i++)
            preka_correction[X_REZO - i - 1] = Math.cos(Math.atan2((double) (i - X_REZO / 2), PROFONDEUR_FOV * X_REZO / 320));
    }


    private void calcule_zbuffer() {
        int i;
        int j;

        double angle_incr;
        double angle_interpol;

        double x_interpol_interne;
        double y_interpol_interne;

        int longueur_rayon;

        int x2, y2;
        double x3, y3;

        int tempi, t1, t2, t3, t4;
        double tempf;

        angle_incr = (NBR_ANGLE * FOV / 360.0) / X_REZO;
        angle_interpol = ((double) ((360 + mes_joueurs[mon_ID].mon_angle - FOV / 2)) * NBR_ANGLE / 360.0);

        for (i = 0; i < X_REZO; i++) {
            x_back = preka_cosinus[((int) (angle_interpol) & (NBR_ANGLE - 1))];
            y_back = preka_sinus[((int) (angle_interpol) & (NBR_ANGLE - 1))];

            switch ((int) angle_interpol >> 7) {

                case 0:
                    x2 = (int) mes_joueurs[mon_ID].mon_x;
                    x2 = x2 >> 6;
                    x2++;
                    x2 = x2 << 6;
                    x2 -= 1;
                    x1 = (double) x2;
                    yy1 = mes_joueurs[mon_ID].mon_y + (x1 - mes_joueurs[mon_ID].mon_x) * preka_tan[(int) angle_interpol];
                    dx = 64.0;
                    dy = (double) (64.0 * preka_tan[(int) angle_interpol]);
                    aller(0, 1);
                    back();
                    break;

                case 1:
                    y2 = (int) mes_joueurs[mon_ID].mon_y;
                    y2 = y2 >> 6;
                    y2++;
                    y2 = y2 << 6;
                    y2 -= 1;
                    yy1 = (double) y2;
                    x1 = mes_joueurs[mon_ID].mon_x + (yy1 - mes_joueurs[mon_ID].mon_y) * preka_tan[256 - (int) angle_interpol];
                    dy = 64.0;
                    dx = (double) (64.0 * preka_tan[256 - (int) angle_interpol]);
                    aller(-1, 0);
                    back();
                    break;

                case 2:
                    y2 = (int) mes_joueurs[mon_ID].mon_y;
                    y2 = y2 >> 6;
                    y2++;
                    y2 = y2 << 6;
                    y2 -= 1;
                    yy1 = (double) y2;
                    x1 = mes_joueurs[mon_ID].mon_x - (yy1 - mes_joueurs[mon_ID].mon_y) * preka_tan[(int) angle_interpol - 256];
                    dy = 64.0;
                    dx = -(double) (64.0 * preka_tan[(int) angle_interpol - 256]);
                    aller(-1, 0);
                    back();
                    break;

                case 3:
                    x2 = (int) mes_joueurs[mon_ID].mon_x;
                    x2 = x2 >> 6;
                    x2 = x2 << 6;
                    x1 = (double) x2;
                    yy1 = mes_joueurs[mon_ID].mon_y + (mes_joueurs[mon_ID].mon_x - x1) * preka_tan[512 - (int) angle_interpol];
                    dx = -64.0;
                    dy = (double) (64.0 * preka_tan[512 - (int) angle_interpol]);
                    aller(0, -1);
                    back();
                    break;

                case 4:
                    x2 = (int) mes_joueurs[mon_ID].mon_x;
                    x2 = x2 >> 6;
                    x2 = x2 << 6;
                    x1 = (double) x2;
                    yy1 = mes_joueurs[mon_ID].mon_y + (x1 - mes_joueurs[mon_ID].mon_x) * preka_tan[(int) angle_interpol];
                    dx = -64.0;
                    dy = -(double) (64.0 * preka_tan[(int) angle_interpol]);
                    aller(0, -1);
                    back();
                    break;

                case 5:
                    y2 = (int) mes_joueurs[mon_ID].mon_y;
                    y2 = y2 >> 6;
                    y2 = y2 << 6;
                    yy1 = (double) y2;
                    x1 = mes_joueurs[mon_ID].mon_x - (mes_joueurs[mon_ID].mon_y - yy1) * preka_tan[256 * 3 - (int) angle_interpol];
                    dy = -64.0;
                    dx = -(double) (64.0 * preka_tan[256 * 3 - (int) angle_interpol]);
                    aller(1, 0);
                    back();
                    break;

                case 6:
                    y2 = (int) mes_joueurs[mon_ID].mon_y;
                    y2 = y2 >> 6;
                    y2 = y2 << 6;
                    yy1 = (double) y2;
                    x1 = mes_joueurs[mon_ID].mon_x + (mes_joueurs[mon_ID].mon_y - yy1) * preka_tan[(int) angle_interpol - 768];
                    dy = -64.0;
                    dx = (double) (64.0 * preka_tan[(int) angle_interpol - 768]);
                    aller(1, 0);
                    back();
                    break;

                case 7:
                    x2 = (int) mes_joueurs[mon_ID].mon_x;
                    x2 = x2 >> 6;
                    x2++;
                    x2 = x2 << 6;
                    x2 -= 1;
                    x1 = (double) x2;
                    yy1 = mes_joueurs[mon_ID].mon_y - (x1 - mes_joueurs[mon_ID].mon_x) * preka_tan[1024 - (int) angle_interpol];
                    dx = 64.0;
                    dy = -(double) (64.0 * preka_tan[1024 - (int) angle_interpol]);
                    aller(0, 1);
                    back();
                    break;
            }

            //pythagore pour avoir la longueur du ryon en cours
            z_buffer[X_REZO - i - 1] = (int) Math.sqrt((x1 - mes_joueurs[mon_ID].mon_x) * (x1 - mes_joueurs[mon_ID].mon_x) + (mes_joueurs[mon_ID].mon_y - yy1) * (mes_joueurs[mon_ID].mon_y - yy1));

            angle_interpol += angle_incr;
            if (angle_interpol >= 1024.0)
                angle_interpol = angle_interpol - 1024.0;
            if (angle_interpol <= 0.0)
                angle_interpol = angle_interpol + 1024.0;
        }
    }//class calcule_zbuffer


    private void aller(int toto, int titi) {
        int t1, t2;

        while (true) {
            t1 = ((int) x1) >> 6;
            t2 = ma_carte.donneHaut() - (((int) yy1) >> 6);
            if (!((ma_carte.donneCase(t1, t2 - 1) != '*') && (ma_carte.donneCase(t1 + titi, t2 + toto - 1) != '*')))
                break;
            x1 += dx;
            yy1 += dy;
        }
    }


    private void back() {
        int t1, t2;

        t1 = ma_carte.donneHaut() - (((int) yy1) >> 6);
        t2 = ((int) x1) >> 6;

        while (ma_carte.donneCase(t2, t1 - 1) == '*') {
            x1 -= x_back;
            yy1 -= y_back;
            t1 = ma_carte.donneHaut() - (((int) yy1) >> 6);
            t2 = ((int) x1) >> 6;
        }
    }

    /**
     * Methode <B>gestionClavier(char)</B>
     *
     * @param char tel_c - caractere presse par l'utilisateur
     * @see Une_Fenetre_Visu#avance()
     * @see Une_Fenetre_Visu#recule()
     * @see Une_Fenetre_Visu#droite()
     * @see Une_Fenetre_Visu#gauche()
     * @see Une_Fenetre_Visu#tire()
     */


    public void gestionClavier(char tel_c) {
        double xj;
        double yj;
        int yjj;
        int xii;

        switch (tel_c) {
            case '8':
                xj = preka_cosinus[((int) (((double) (mes_joueurs[mon_ID].mon_angle)) * NBR_ANGLE / 360.0) & (NBR_ANGLE - 1))] * 10;
                yj = preka_sinus[((int) (((double) (mes_joueurs[mon_ID].mon_angle)) * NBR_ANGLE / 360.0) & (NBR_ANGLE - 1))] * 10;

                yjj = ma_carte.donneHaut() - (((int) (mes_joueurs[mon_ID].mon_y + yj)) >> 6) - 1;
                xii = ((int) (mes_joueurs[mon_ID].mon_x + xj)) >> 6;

                if (ma_carte.donneCase(xii, yjj) != '*') {
                    mes_joueurs[mon_ID].mon_x += xj;
                    mes_joueurs[mon_ID].mon_y += yj;
                }
                break;

            case '5':
                xj = -preka_cosinus[((int) (((double) (mes_joueurs[mon_ID].mon_angle)) * NBR_ANGLE / 360.0) & (NBR_ANGLE - 1))] * 10;
                yj = -preka_sinus[((int) (((double) (mes_joueurs[mon_ID].mon_angle)) * NBR_ANGLE / 360.0) & (NBR_ANGLE - 1))] * 10;

                yjj = ma_carte.donneHaut() - (((int) (mes_joueurs[mon_ID].mon_y + yj)) >> 6) - 1;
                xii = ((int) (mes_joueurs[mon_ID].mon_x + xj)) >> 6;

                if (ma_carte.donneCase(xii, yjj) != '*') {
                    mes_joueurs[mon_ID].mon_x += xj;
                    mes_joueurs[mon_ID].mon_y += yj;
                }
                break;

            case '4':
                mes_joueurs[mon_ID].mon_angle += COEF_ANGLE;
                mes_joueurs[mon_ID].mon_angle = (mes_joueurs[mon_ID].mon_angle + 360) % 360;
                break;

            case '6':
                mes_joueurs[mon_ID].mon_angle -= COEF_ANGLE;
                mes_joueurs[mon_ID].mon_angle = (mes_joueurs[mon_ID].mon_angle + 360) % 360;
                break;
        }
    }
}
