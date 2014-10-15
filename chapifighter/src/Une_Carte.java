/**
 * La classe <B>Une_Carte</B> determine la carte du jeu (le labyrinthe) :<BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 */

public class Une_Carte {

    private int haut_map = 16;
    private int larg_map = 10;
    private int Mon_Num;

    private char[][] map1 =
            {
                    {'*', '*', '*', '*', '*', '*', '*', '*', '*', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', '*', ' ', '*'},
                    {'*', ' ', '*', '*', ' ', ' ', '*', '*', ' ', '*'},
                    {'*', ' ', '*', ' ', ' ', ' ', ' ', '*', ' ', '*'},
                    {'*', ' ', ' ', ' ', '*', ' ', '*', '*', ' ', '*'},
                    {'*', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', ' ', '*', '*', ' ', ' ', '*', ' ', ' ', '*'},
                    {'*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*'},
                    {'*', '*', '*', '*', '*', '*', '*', '*', '*', '*'}
            };

    /**
     * Le constructeur <B>Une_Carte</B> cree une carte :<BR>
     */
    public Une_Carte() {
    }

    /**
     * retourne la table ASCII qui represente la carte.
     * '*' = un mur<BR>
     * ' ' = un vide<BR>
     */
    public char[][] donne_carte() {
        return map1;
    }

    /**
     * retourne le caractere ASCII situe en x , y .
     */
    public char donneCase(int pos_x, int pos_y) {
        return map1[pos_y][pos_x];
    }

    /**
     * retourne la largeur de la carte ASCII.
     */
    public int donneLarg() {
        return larg_map;
    }

    /**
     * retourne la hauteur de la carte ASCII.
     */
    public int donneHaut() {
        return haut_map;
    }
}
