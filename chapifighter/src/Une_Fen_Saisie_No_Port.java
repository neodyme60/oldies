import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * La classe <B>Une_Fen_Saisie_No_Port</B> sert a saisir un numero de port a 4 chiffres.<BR>
 * Rq. La saisie n'est validee que lorsque un numero de port valide se trouve dans le champs prevu a cet effet.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 1.2
 * @see Un_Serveur
 */
public class Une_Fen_Saisie_No_Port extends Frame implements WindowListener, ActionListener {

    private Button Mon_Btn_Lance;
    private Label Mon_Lbl_No_Port;
    private TextField Mon_Chp_No_Port;
    private Panel Mon_Cnt_No_Port;
    private boolean Mon_Transfert;

    /**
     * Constructeur de la classe <B>Une_Fen_Saisie_No_Port</B>.
     */
    public Une_Fen_Saisie_No_Port() {
        super("Fenetre de Saisie du Numero de Port");
        addWindowListener(this);
        setSize(325, 80);
        setResizable(false);
        Mon_Lbl_No_Port = new Label("Numero de Port (4 Chiffres) :");
        Mon_Chp_No_Port = new TextField(4);
        Mon_Chp_No_Port.addActionListener(this);
        Mon_Cnt_No_Port = new Panel();
        Mon_Cnt_No_Port.add("West", Mon_Lbl_No_Port);
        Mon_Cnt_No_Port.add("East", Mon_Chp_No_Port);
        Mon_Btn_Lance = new Button("Lance !");
        Mon_Btn_Lance.setEnabled(false);
        Mon_Btn_Lance.addActionListener(this);
        add("North", Mon_Cnt_No_Port);
        add("South", Mon_Btn_Lance);
        Mon_Transfert = false;
    }

    void prendsFocus(Component Tel_Compt) {
        Tel_Compt.setEnabled(true);
        Tel_Compt.requestFocus();
    }

    /**
     * La methode <B>estPret</B> permet de mettre en attente le serveur par une boucle <B>while</B> jusqu'a la saisie d'un numero de port valide.
     *
     * @return true si le numero de port est pret pour le transfert, false sinon.
     * @see Un_Serveur
     */
    public boolean estPret() {
        return Mon_Transfert;
    }

    /**
     * La methode <B>transfertNoPort</B> permet de recuperer le numero de port saisie dans la fenetre.
     *
     * @return La chaine de caracteres representant le numero de port.
     * @see Un_Serveur
     */
    public String transfertNoPort() {
        return Mon_Chp_No_Port.getText();
    }

    /**
     * La methode <B>active</B> permet au serveur d'afficher la fenetre de saisie du numero de port.
     *
     * @see Un_Serveur
     */
    public void active() {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port reactivee...");
        show();
    }

    /**
     * La methode <B>desactive</B> permet de cacher le fenetre de saisie du numero de port des que celui-ci a ete entre.
     *
     * @see Un_Serveur
     */
    public void desactive() {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port desactivee...");
        removeNotify();
    }

    void quitter() {
        desactive();
        System.err.println("Une_Fen_Saisie_No_Port : Sortie du serveur...     bye!");
        System.exit(0);
    }

    /**
     * La methode <B>actionPerformed</B> de l'interface <B>ActionListener</B> permet gerer les actions :<BR>
     * - ENTREE: sur le champs de saisie du numero de port ( Test sur le nombre de chiffres );<BR>
     * - ESPACE: sur le bouton d'activation du serveur ( Activation du drapeau de transfert du numero de port).<BR>
     *
     * @see Une_Fen_Saisie_No_Port#estPret
     */
    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Mon_Btn_Lance)
            Mon_Transfert = true;
        if ((Tel_Evt.getSource() == Mon_Chp_No_Port)
                && (Mon_Chp_No_Port.getText().length() <= 4))
            prendsFocus(Mon_Btn_Lance);
    }

    /**
     * La methode <B>windowIconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'iconification de la fenetre.
     */
    public void windowIconified(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port iconified...");
    }

    /**
     * La methode <B>windowDeiconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de restauration de la fenetre.
     */
    public void windowDeiconified(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port restauree...");
    }

    /**
     * La methode <B>windowActivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'activation de la fenetre.
     */
    public void windowActivated(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port activee...");
    }

    /**
     * La methode <B>windowDeactivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de desactivation de la fenetre.
     */
    public void windowDeactivated(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port desactivee...");
    }

    /**
     * La methode <B>windowOpened</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'ouverture de la fenetre.
     */
    public void windowOpened(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port ouverte...");
    }

    /**
     * La methode <B>windowClosing</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de fermeture de la fenetre ( et du programme ).
     */
    public void windowClosing(WindowEvent Tel_Evt) {
        quitter();
    }

    /**
     * La methode <B>windowClosed</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'adieu de la fenetre.
     */
    public void windowClosed(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_Saisie_No_Port : Fenetre saisie port fermee...");
    }

}
