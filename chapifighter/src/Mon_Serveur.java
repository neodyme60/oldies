/**
 * La classe <B>Mon_Serveur</B> sert a generer un serveur de type <B>Un_Serveur</B> autonome.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 2.0
 * @see Un_Serveur
 */

public class Mon_Serveur {

    /**
     * La methode <b>main</b> cree le serveur :<BR>
     * - Soit en fonction des parametres de la ligne de commande;<BR>
     * - Soit directement ( Celui-ci generera une fenetre de saisie du numero de port ).<BR>
     *
     * @param String[] "No_Port_Serveur_a_4_Chiffres" "Nb_Vict" "Nb Max Joueurs"
     * @see Un_Serveur#Un_Serveur(String, int, int)
     */
    public static void main(String argv[]) {
        Un_Serveur Mon_Serveur;
        try {
            Mon_Serveur = new Un_Serveur(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
        } catch (Exception Telle_E) {
            System.err.println("Ligne de Commande: java Mon_Serveur <No Port a 4 Chiffres> <Nb Kills pour ganger> <Nb Max Joueurs >");
        }

    }

}
