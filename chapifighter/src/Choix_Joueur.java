import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <B>Classe <U>Choix_Joueur<U/></B><BR>
 * la classe Choix_Joueur herite d une window, et est utilise<BR>
 * par la classe Un_menu<BR>
 * <BR>
 * C est la creation d une boite de dialogue permettant au client<BR>
 * de choisir un personnage et de lui attribuer un nom.<BR>
 * L utilisateur, va etre invite a saisir le pseudonyme de son personnage<BR>
 * (champ Saisie_Pseudo) et de choisir son apparence dans plusieurs<BR>
 * proposees ( gr�ce a une Canvas, et un tableau de personnages)<BR>
 * ensuite il pourra soit enregistrer les nouvelles valeurs attribuees<BR>
 * a ces deux champs (par clic sur le bouton Save) , soit quitter<BR>
 * (par clic sur le bouton Annule)<BR>
 * <BR>
 * <I>Si le bouton Confirme est actionne:</I><BR>
 * On sauvegarde les nouvelles valeurs des champs<BR>
 * <BR>
 * <I>Si le bouton Annule est actionne:</I><BR>
 * Quitter la boite de dialogue et rendre la main a la fenetre parente<BR>
 * Pour des raisons de  timing , on cache d abord la boite de dialogue<BR>
 * (removeNotify()), et on la detruit (dispose()). En fait on ne sait <BR>
 * pas quand le dispose() prendra son effet et detruira la boite, en <BR>
 * la cachant d abord on permet la destruction quand elle le peut s en <BR>
 * poser des problemes d affichage.<BR>
 * <BR>
 * <I>Si le bouton Precedent est actionne:</I><BR>
 * Affichage de la photo precedente (dans le tableau) a celle affichee.<BR>
 * <BR>
 * <I>Si le bouton Suivant est actionne:</I><BR>
 * Affichage de la photo suivante (dans le tableau) a celle affichee.<BR>
 * <BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_serveur,Un_panneau,Une_Aide,Un_Canvas,Dialogue_info_rezo
 */
public class Choix_Joueur extends Frame implements ActionListener {
    // Mis en public pour pouvoir etre  touche  par la classe Un_menu
    public int Indice;
    public boolean Pret = false;
    public String Le_Pseudo;
    public String Le_Perso;
    Color la_couleur_fond;
    Un_Canvas Son_Canvas;
    Frame la_fenetre_parente;
    Button Precedent, Suivant;
    Button Annuler, Confirmer;
    TextField Saisie_Pseudo;
    Label Label_Pseudo;
    Panel pan_1, pan_2, pan_3, pan_4;
    int NB_PERSO;
    // Dimensions de la bo�te de dialogue
    int LG_CJ = 300;
    int HT_CJ = 200;
    private Image Mon_Visage;
    private String[] Le_Tableau = {"apsi", "bat", "eog", "riquet", "ce", "nico", "mig", "mgr", "rog"};

    /**
     * Le constructeur de cette classe prend en parametre :<BR>
     * <BR>
     *
     * @param la_parente - la Frame de laquelle elle herite
     * @param tel_pseudo - pseudonyme du personnage stocke dans Un_menu
     * @param tel_perso  - fichier image du personnage (fichier *.jpg)stocke dans Un_menu
     * @param tel_indice - indice de la photo choisie dans le tableau de photos stocke dans Un_menu
     *                   Gr�ce a ces trois derniers parametres, on peut conserver les trois <BR>
     *                   valeurs et les faire afficher comme anciennes valeurs dans les trois<BR>
     *                   champs.<BR>
     */

    //Constructeur
    public Choix_Joueur(Frame la_parente, String tel_pseudo, String tel_perso, int tel_indice) {
        super("Choix joueur");//la_parente);
        // Fond de la Window -->gris
        la_couleur_fond = new Color(100, 40, 30);
        setBackground(la_couleur_fond);
        NB_PERSO = Le_Tableau.length;
        // Variables locales <-- variables parametrees
        Le_Pseudo = tel_pseudo;
        Le_Perso = tel_perso;
        Indice = tel_indice;
        la_fenetre_parente = la_parente;

        // Initialisation des Label, Button, TextField
        // et mise en forme

        pan_1 = new Panel();
        pan_2 = new Panel();
        pan_3 = new Panel();
        pan_4 = new Panel();

        Precedent = new Button("Precedent");
        Suivant = new Button("Suivant");
        Confirmer = new Button("Sauvegarder");
        Annuler = new Button("Quitter");

        Precedent.addActionListener(this);
        Suivant.addActionListener(this);
        Confirmer.addActionListener(this);
        Annuler.addActionListener(this);

        Saisie_Pseudo = new TextField(Le_Pseudo, 15);
        Label_Pseudo = new Label("Mon pseudonyme :");

        Son_Canvas = new Un_Canvas();

        setLayout(new GridLayout(4, 1));

        Son_Canvas.refresh(Le_Tableau[Indice]);

        pan_1.add(Precedent);
        pan_1.add(Son_Canvas);
        pan_1.add(Suivant);

        pan_2.add("West", Label_Pseudo);
        pan_2.add("East", Saisie_Pseudo);
        pan_3.add(Confirmer);
        pan_4.add(Annuler);

        add(pan_1);
        add(pan_2);
        add(pan_3);
        add(pan_4);

        setSize(LG_CJ, HT_CJ);

        setLocation(la_fenetre_parente.getLocation().x + 252, la_fenetre_parente.getLocation().y);
        show();
    }

    public boolean donne_pret() {
        return Pret;
    }

    public void fixe_pret(boolean le_pret) {
        Pret = le_pret;
    }

    public String donne_pseudo() {
        return Le_Pseudo;
    }

    public String donne_perso() {
        return Le_Perso;
    }

    public int donne_indice() {
        return Indice;
    }

    public void actionPerformed(ActionEvent tel_evt) {

        if (tel_evt.getSource() == Suivant) {
            Indice = (Indice + 1) % NB_PERSO;
            Son_Canvas.refresh(Le_Tableau[Indice]);
        }

        if (tel_evt.getSource() == Precedent) {
            Indice = (Indice - 1 + NB_PERSO) % NB_PERSO;
            System.err.println(Indice);
            Son_Canvas.refresh(Le_Tableau[Indice]);
        }

        if (tel_evt.getSource() == Confirmer) {
            Le_Pseudo = Saisie_Pseudo.getText();
            Le_Perso = Le_Tableau[Indice];
            Pret = true;
        }

        if (tel_evt.getSource() == Annuler) {
            Pret = true;
            removeNotify();
            dispose();
        }
    }
}


