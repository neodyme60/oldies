import java.awt.*;

/**
 * La classe <B>Une_Map</B> permet l'affichage de la carte ainsi que la position du joueur dans la carte.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 1.2
 */
public class Une_Map {

    static int X_REZO = 160;
    static int Y_REZO = 100;

    private int le_pas_x;
    private int le_pas_y;

    private Une_Carte ma_carte;
    private int x_map;
    private int y_map;
    private char[][] la_table;

    /**
     * Ce constructeur de la classe <B>Une_Map</B> prend en parametre une carte de type Une_Carte.
     *
     * @param la_carte - C'est objet de type Une_carte.
     */
    public Une_Map(Une_Carte la_carte) {
        ma_carte = la_carte;
        x_map = la_carte.donneLarg();
        y_map = la_carte.donneHaut();
        la_table = la_carte.donne_carte();

        le_pas_x = X_REZO / x_map;
        le_pas_y = Y_REZO / y_map;
    }

    /**
     * La methode <B>refresh</B> de la classe Une_Map affiche la carte ainsi que la position du joueur
     */
    public void refresh(double x_joueur, double y_joueur, Graphics g) {
        int i, j;
        int x = (int) x_joueur;
        int y = (int) y_joueur;

        for (i = 0; i < x_map; i++)
            for (j = 0; j < y_map; j++) {
                if (la_table[j][i] == '*')
                    g.setColor(new Color(0, 0, 255));
                else
                    g.setColor(new Color(0, 0, 0));

                g.fillRect(i * le_pas_x, j * le_pas_y, le_pas_x, le_pas_y);

                g.setColor(new Color(255, 255, 255));
                g.drawRect(x_map * le_pas_x * x / (x_map * 64), (y_map * le_pas_y) - (y_map * le_pas_y * y / (y_map * 64)) - 1, 1, 1);

            }
    }
}
