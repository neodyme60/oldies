import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class Une_Fen_CHAT extends Frame implements WindowListener {
    public TextField Ma_Zone_Emission;
    public Button Mon_Btn_Quitter;
    private String Mon_Pseudo;
    private Thread Mon_Thread;
    private TextArea Ma_Zone_Reception;
    private Panel Mon_Cnt_Emission, Mon_Cnt_Btn;
    private Label Mon_Lbl_Emission;

    public Une_Fen_CHAT(String Tel_Pseudo) {
        super(Tel_Pseudo);
        addWindowListener(this);
        Ma_Zone_Reception = new TextArea("", 10, 40, TextArea.SCROLLBARS_NONE);
        Ma_Zone_Reception.setEditable(false);
        Ma_Zone_Reception.setEnabled(false);
        Mon_Lbl_Emission = new Label("Message :");
        Ma_Zone_Emission = new TextField(25);
        Ma_Zone_Emission.setEditable(false);
        Mon_Cnt_Emission = new Panel();
        Mon_Cnt_Emission.add("West", Mon_Lbl_Emission);
        Mon_Cnt_Emission.add("East", Ma_Zone_Emission);
        Mon_Btn_Quitter = new Button("Quitter");
        Mon_Cnt_Btn = new Panel();
        Mon_Cnt_Btn.add("Center", Mon_Btn_Quitter);
        add("North", Ma_Zone_Reception);
        add("Center", Mon_Cnt_Emission);
        add("South", Mon_Cnt_Btn);
        setSize(300, 260);
        setResizable(false);
        setLocation(330, 0);
//                active( );
    }

    public void active() {
        System.err.println("Une_Fen_CHAT : Fenetre de dialogue activee...");
        show();
    }

    public void desactive() {
        System.err.println("Une_Fen_CHAT : Fenetre de dialogue desactivee...");
        removeNotify();
    }

    public String lireEmission() {
        String La_Str = Ma_Zone_Emission.getText();
        Ma_Zone_Emission.setText("");
        return La_Str;
    }

    public void afficherMsg(String Tel_Msg) {
        System.err.println("Une_Fen_CHAT : Reception d'un message...");
        Ma_Zone_Reception.append(Tel_Msg);
    }

    void quitter() {
        desactive();
        System.err.println("Une_Fen_CHAT : Sortie du client...     bye!");
        System.exit(0);
    }

    public void ajouteCar(char Tel_Car) {
        Ma_Zone_Emission.setText(Ma_Zone_Emission.getText() + Tel_Car);
    }

    public void deleteCar() {
        String La_Str = Ma_Zone_Emission.getText();
        if (La_Str.length() > 0)
            La_Str = La_Str.substring(0, La_Str.length() - 1);
        Ma_Zone_Emission.setText(La_Str);
    }

    public void windowIconified(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Iconified...");
    }

    public void windowDeiconified(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Restaur�e...");
    }

    public void windowActivated(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Activ�e...");
    }

    public void windowDeactivated(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Desactiv�e...");
    }

    public void windowOpened(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Ouverte...");
    }

    public void windowClosing(WindowEvent Tel_Evt) {
        quitter();
    }

    public void windowClosed(WindowEvent Tel_Evt) {
        System.err.println("Une_Fen_CHAT : Fenetre CHAT Ferm�e...");
    }

}
