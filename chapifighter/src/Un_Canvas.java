import java.awt.*;

/**
 * <B>Classe <U>Un_Canvas</U></B><BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_serveur,Dialogue_info_rezo,Choix_joueur,Un_panneau,Une_Aide
 */

public class Un_Canvas extends Canvas {

    Image Mon_Visage;


    Un_Canvas() {
        setSize(60, 60);
        setVisible(true);
    }


    public void refresh(String tel_nom) {
        MediaTracker tracker = new MediaTracker(this);
        Mon_Visage = getToolkit().getImage("CV_" + tel_nom + ".jpg");
        tracker.addImage(Mon_Visage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Probleme chargement image!!!!");
        }


        System.err.println("....Chargement image...");
        prepareImage(Mon_Visage, null);


        repaint();
    }


    public void paint(Graphics tel_g) {

        update(tel_g);
    }


    public void update(Graphics telle_zone) {
        System.err.println("...update...");
        telle_zone.drawImage(Mon_Visage, 0, 0, null);
    }

}
