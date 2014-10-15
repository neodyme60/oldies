import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <B>La classe <U>Dialogue_info_rezo</U></B> herite d une window, et est utilisee par la classe Un_menu.<BR>
 * <BR>
 * C est la creation d une boite de dialogue permettant au client de configurer le reseau (c est a dire de choisir <BR>
 * un jeu-serveur). L utilisateur, voulant creer un jeu-client, va etre invite a saisir l adresse du serveur auquel <BR>
 * il veut se connecter (champ tf_ad_serveur), ainsi que le port guetteur (champ tf_port_guet), ensuite il pourra soit<BR>
 * enregistrer les nouvelles valeurs attribuees a ces deux champs (par clic sur le bouton Save) , soit quitter<BR>
 * (par clic sur le bouton Annule).<BR>
 * <BR>
 * <B>Si le bouton Save est actionne:</B><BR>
 * On sauvegarde les nouvelles valeurs des champs<BR>
 * <BR>
 * <B>Si le bouton Annule est actionne:</B><BR>
 * Quitter la boite de dialogue et rendre la main a la fenetre parente, on detruit la boite de dialogue.<BR>
 * Pour des raisons de  timing , on cache d abord la boite de dialogue (removeNotify()), et on la detruit (dispose()). En <BR>
 * fait on ne sait pas quand le dispose() prendra son effet et detruira la boite, en la cachant d abord on permet la <BR>
 * destruction quand elle le peut s en poser des problemes d affichage.<BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_serveur,Un_panneau,Choix_joueur,Un_Canvas,Une_Aide
 */
public class Dialogue_info_rezo extends Frame implements ActionListener {
    // Mis en public pour pouvoir etre  touche  par la classe Un_menu
    public String port_guet;
    public String ad_serveur;
    public boolean mon_save_appuye = false;
    Label lb_port_guet, lb_ad_serveur;
    TextField tf_port_guet, tf_ad_serveur;
    Button Save, Annule;
    Panel pan_1, pan_2, pan_3, pan_4;
    Frame la_fenetre_parente;
    Color la_couleur_fond;
    // Dimensions de la bo�te de dialogue
    int LG_DLG_SERV = 300;
    int HT_DLG_SERV = 150;

    /**
     * Le constructeur de cette classe prend en parametres :<BR>
     * <BR>
     *
     * @param le_parent      - la fenetre parente, c est a dire la Frame de laquelle elle herite<BR>
     * @param tel_no_port    - le numero du port guetteur stocke dans Un_menu<BR>
     * @param tel_ad_serveur - l adresse du serveur stocke dans Un_menu<BR>
     *                       Gr�ce a ces deux derniers parametres, on peut conserver les deux
     *                       valeurs et les faire afficher comme anciennes valeurs dans les deux
     *                       champs
     */


    //Constructeur
    public Dialogue_info_rezo(Frame le_parent, String tel_no_port, String tel_ad_serveur) {
        super("Info REZO");//le_parent);
        // Fond de la Window -->gris
        la_couleur_fond = new Color(100, 40, 30);
        setBackground(la_couleur_fond);
        // Variables locales <-- variables parametrees
        port_guet = new String(tel_no_port);
        ad_serveur = tel_ad_serveur;
        la_fenetre_parente = le_parent;

        // Initialisation des Label, Button, TextField
        // et mise en forme

        lb_port_guet = new Label("Numero port serveur :");
        tf_port_guet = new TextField(port_guet, 4);

        lb_ad_serveur = new Label("Adresse serveur :");
        tf_ad_serveur = new TextField(ad_serveur, 12);

        Save = new Button("Sauvegarder");
        Save.addActionListener(this);

        Annule = new Button("Quitter");
        Annule.addActionListener(this);

        pan_1 = new Panel();
        pan_2 = new Panel();
        pan_3 = new Panel();
        pan_4 = new Panel();

        pan_1.add("West", lb_port_guet);
        pan_1.add("East", tf_port_guet);
        pan_2.add("West", lb_ad_serveur);
        pan_2.add("East", tf_ad_serveur);
        pan_3.add("Center", Save);
        pan_4.add("Center", Annule);

        setLayout(new GridLayout(4, 1));
        add(pan_1);
        add(pan_2);
        add(pan_3);
        add(pan_4);

        setSize(LG_DLG_SERV, HT_DLG_SERV);

        setLocation(la_fenetre_parente.getLocation().x + 252, la_fenetre_parente.getLocation().y);

        show();
    }

    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Save) {
            port_guet = new String(tf_port_guet.getText());
            ad_serveur = new String(tf_ad_serveur.getText());
            mon_save_appuye = true;
        }

        if (Tel_Evt.getSource() == Annule) {
            mon_save_appuye = true;
            removeNotify();
            dispose();
        }
    }
}
