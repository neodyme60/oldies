import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * <B>Classe <U>Un_Aide</U></B><BR>
 * C est la boite de dialogue contenant l aide.<BR>
 *
 * @author Chapi-Fighter Developpement Group
 * @version 3.0
 * @see Un_menu,Dialogue_serveur,Dialogue_info_rezo,Choix_joueur,Un_panneau,Un_Canvas
 */


public class Une_Aide extends Frame implements ActionListener

{
    public boolean pret = false;
    FileInputStream Son_Flux_Fichier;
    File Son_Fichier;
    byte[] Son_Tab;
    private TextArea Son_Texte;
    private Color la_couleur_fond;
    private Button Quitter;
    private Panel Son_Conteneur_Haut;
    private Panel Son_Conteneur_Bas;
    private String Son_Contenu;
    private Frame La_Fenetre_Parente;
    private Point Son_Point_Init;

    /**
     * Le constructeur de cette classe prend en parametres :<BR>
     *
     * @param La_Parente      - Frame parente<BR>
     * @param tel_nom_fichier - Nom du fichier d aide
     */
    public Une_Aide(Frame La_Parente, String tel_nom_fichier) {
        super("Aide");//La_Parente);
        la_couleur_fond = new Color(100, 40, 30);
        setBackground(la_couleur_fond);

        try {
            //cr�ation d'un flux entre la classe et le fichier voulu
            Son_Flux_Fichier = new FileInputStream(tel_nom_fichier + ".txt");
            //ouverture du fichier
            Son_Fichier = new File(tel_nom_fichier + ".txt");
            //On initialise la taille du tableau de byte
            //apr�s avoir compte� son nombre de caract�res du fichier
            Son_Tab = new byte[(int) Son_Fichier.length()];
        } catch (FileNotFoundException e) {
            System.err.println("Erreur lors de l'ouverture du fichier d'aide.....");
        }

        try {
            //Lecture et stockage dans le tableau de byte du contenu du fichier
            Son_Flux_Fichier.read(Son_Tab);
            //Convertion du contenu du fichier en string
            Son_Contenu = new String(Son_Tab);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture dans le fichier d'aide.....");
        }

        Son_Texte = new TextArea(Son_Contenu, 10, 30);
        Son_Texte.setEditable(false);

        Quitter = new Button("Quitter");
        Quitter.addActionListener(this);

        Son_Conteneur_Haut = new Panel();
        Son_Conteneur_Bas = new Panel();

        Son_Conteneur_Haut.add(Son_Texte);
        Son_Conteneur_Bas.add(Quitter);

        add("North", Son_Conteneur_Haut);
        add("South", Son_Conteneur_Bas);

        setSize(300, 250);
        setLocation(La_Parente.getLocation().x + 252, La_Parente.getLocation().y);

        show();
    }//constructeur

    public void actionPerformed(ActionEvent tel_evt) {

        //Quitter la boite de dialogue d'aide
        if (tel_evt.getSource() == Quitter) {
            pret = true;
            while (pret)
                removeNotify();
            dispose();
        }//if
    }//actionPerformed

}//Une_Aide_Client






