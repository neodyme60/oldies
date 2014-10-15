/**
 * <B>La classe <U>Un_panneau</U></B> <BR>
 * C est en fait la structure des menus et sous_menus<BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_serveur,Dialogue_info_rezo,Choix_joueur,Une_Aide,Un_Canvas
 */


public class Un_panneau {
    int la_taille;// taille = nombre des options
    String le_titre;
    String[] le_tab_options;

    /**
     * Le constructeur de cette classe prend en parametres :<BR>
     * <BR>
     *
     * @param titre       - le titre du menu <BR>
     * @param les_options - tableau des options du menu<BR>
     * @param taille      - taille de ce tableau<BR>
     */


    // Constructeur
    public Un_panneau(String titre, String[] les_options, int taille) {
        le_titre = titre;
        la_taille = taille;
        le_tab_options = les_options;
    }

    //retourne le titre du menu en cours
    public String donne_titre() {
        return le_titre;
    }

    // compare un String avec le titre d'un menu
    public boolean compare(String la_chaine_a_cmp) {
        if (la_chaine_a_cmp.compareTo(le_titre) == 0)
            return true;
        else {
            return false;
        }
    }

    // retourne l'option sous forme de String
    public String donne_option(int l_indice) {
        return le_tab_options[l_indice];
    }

    // retourne la taille du tableau d'options
    public int donne_taille() {
        return la_taille;
    }
}
