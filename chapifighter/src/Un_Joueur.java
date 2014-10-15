import java.awt.*;

/**
 * La classe <B>Un_Joueur</B> est utilisee comme un article :<BR>
 * Elle est composee de toutes les informations sur un joueur
 * et tout le monde s en sert. Un tableau de Un_Joueur representera par exemple tous les joueurs connectes pour le serveur.
 * (pas de constructeur)
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 */

public class Un_Joueur {
    public static int mon_nb_joueurs_actifs;
    /**
     * pour determiner si cet article est actif
     */
    public boolean estActif;

    /**
     * nom du joueur
     */
    public String mon_pseudo;

    /**
     * vie du joueur
     */
    public int ma_vie;

    /**
     * score du joueur
     */
    public int mon_score;

    /**
     * angle du joueur dans la carte
     */
    public int mon_angle;

    /**
     * position horizontale du joueur dans la carte
     */
    public double mon_x;

    /**
     * position verticale du joueur dans la carte
     */
    public double mon_y;

    /**
     * tableau des image du joueur : graph de face, de profile, de dos et du visage
     */
    public Image[] mes_sprites;

    /**
     * nom des fichiers image de mes_sprites
     */
    public String pct_name;
}
