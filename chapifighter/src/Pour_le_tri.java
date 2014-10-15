/**
 * classe <B>Pour_le_tri</B>
 * cette classe est une structure utlisee pour
 * l'affichage des personnages
 *
 * @see Un_Visu
 */


public class Pour_le_tri {

    /**
     * la variable z est un double qui represente la distance entre
     * le joueur et l'adveraire considere
     */
    public double z;

    /**
     * la variable id est un entier qui contient l'identificateur
     * du joueur joueur considere
     */


    public int id;

    /**
     * Le contructeur de la classe <B>Pour_le_tri</B> prend en parametres
     *
     * @param double l_z -distance adversaire joueur
     * @param int    l_id -identificateur du joueur considere
     */

    public Pour_le_tri(int l_id, double le_z) {
        z = le_z;
        id = l_id;
    }
}
