import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <B>La classe <U>Dialogue_serveur</U></B> herite d une window, et est utilisee par la classe Un_menu.<BR>
 * <BR>
 * C'est la creation d une boite de dialogue permettant au serveur de configurer le reseau. <BR>
 * L'utilisateur, voulant creer un jeu-serveur, va etre invite a saisir le port auquel il veut se connecter (champ tf_port),<BR>
 * le nombre de clients pouvant se connecter a sa partie(champ tf_nb_clts),ainsi que le nombre de joueur a tuer pour gagner une <BR>
 * partie (tf_kill) ensuite il pourra soit enregistrer les nouvelles valeurs attribuees a ces deux champs (par clic sur le <BR>
 * bouton Save) , soit quitter ( par clic sur le bouton Annule ).<BR>
 * <BR>
 * <B>Si le bouton Save est actionne:</B><BR>
 * On sauvegarde les nouvelles valeurs des champs
 * <BR>
 * <B>Si le bouton Annule est actionne:</B><BR>
 * Quitte la boite de dialogue et rendre la main a la fenetre parente, on detruit la boite de dialogue.<BR>
 * Pour des raisons de 'timing', on cache d'abord la boite de dialogue (removeNotify()), et on la detruit (dispose()). En <BR>
 * fait on ne sait pas quand le dispose() prendra son effet et detruira la boite, en la cachant d'abord on permet la <BR>
 * destruction quand elle le peut s'en poser des problemes d'affichage.<BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_info_rezo,Un_Canvas,Une_Aide,Un_panneau,Choix_joueur
 */

public class Dialogue_serveur extends Frame implements ActionListener {
    // Mis en public pour pouvoir etre 'touche' par la classe Un_menu
    public int nombre_clients, nombre_tues;
    public String port_serveur;
    public boolean mon_bouton_appuye = false;

    Label lb_port, lb_nb_clts, lb_kill1, lb_kill2;
    TextField tf_port, tf_nb_clts, tf_kill;
    Button Save, Annule;
    Panel pan_1, pan_2, pan_3, pan_4, pan_5;
    Frame la_fenetre_parente;
    Color la_couleur_fond;

    // Dimensions de la bo�te de dialogue
    int LG_DLG_SERV = 300;
    int HT_DLG_SERV = 150;

    /**
     * Le constructeur de cette classe prend en parametres :<BR>
     * <BR>
     *
     * @param le_parent   - la fenetre parente, c'est a dire la Frame de laquelle elle herite<BR>
     * @param tel_port    - le numero du port serveur stocke dans Un_menu<BR>
     * @param tel_nb_kill - nombre de joueur a tuer stocke dans Un_menu<BR>
     * @param tel_nb_clts - nombre de clients autorises stocke dans Un_menu<BR>
     *                    Gr�ce a ces trois derniers parametres, on peut conserver les troi
     *                    valeurs et les faire afficher comme anciennes valeurs dans les trois
     *                    champs
     */

    //Constructeur
    public Dialogue_serveur(Frame le_parent, String tel_port, int tel_nb_clts, int tel_nb_kill) {
        super("Info REZO serveur");//le_parent);
        // Fond de la Window -->gris
        la_couleur_fond = new Color(100, 40, 30);
        setBackground(la_couleur_fond);
        // Variables locales <-- variables parametrees
        port_serveur = new String(tel_port);
        nombre_clients = tel_nb_clts;
        nombre_tues = tel_nb_kill;
        la_fenetre_parente = new Frame();
        la_fenetre_parente = le_parent;

        // Initialisation des Label, Button, TextField
        // et mise en forme
        lb_port = new Label("Numero port serveur ");
        lb_nb_clts = new Label("Nombre max de clients ");
        lb_kill1 = new Label("Victoire : ");
        lb_kill2 = new Label("assassinats");

        tf_kill = new TextField(Integer.toString(nombre_tues), 3);
        tf_port = new TextField(port_serveur, 4);
        tf_nb_clts = new TextField(Integer.toString(nombre_clients), 2);

        Save = new Button("Sauvegarder");
        Save.addActionListener(this);

        Annule = new Button("Quitter");
        Annule.addActionListener(this);

        pan_1 = new Panel();
        pan_2 = new Panel();
        pan_3 = new Panel();
        pan_4 = new Panel();
        pan_5 = new Panel();

        pan_1.add("West", lb_port);
        pan_1.add("East", tf_port);
        pan_2.add("West", lb_nb_clts);
        pan_2.add("East", tf_nb_clts);
        pan_3.add(lb_kill1);
        pan_3.add(tf_kill);
        pan_3.add(lb_kill2);
        pan_4.add("Center", Save);
        pan_5.add("Center", Annule);

        setLayout(new GridLayout(5, 1));
        add(pan_1);
        add(pan_2);
        add(pan_3);
        add(pan_4);
        add(pan_5);

        setSize(LG_DLG_SERV, HT_DLG_SERV);
        setLocation(le_parent.getLocation().x + 252, le_parent.getLocation().y);
        show();
    }

    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Save) {
            port_serveur = tf_port.getText();
            nombre_clients = Integer.parseInt(tf_nb_clts.getText());
            nombre_tues = Integer.parseInt(tf_kill.getText());
            mon_bouton_appuye = true;
        }

        if (Tel_Evt.getSource() == Annule) {
            mon_bouton_appuye = true;
            removeNotify();
            dispose();
        }
    }
}

