import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * La classe <B>Un_Serveur_CHAT</B> est le serveur specifique qui gere le CHAT. Apres recuperation des donnees qui lui sont destinees aupres des canaux de connexion, il reemet les messages vers tous les clients.
 *
 * @author Chapi-Fighter Developpement Group
 * @version 2.0
 * @see Un_Canal_Connexion
 * @see Un_Serveur
 * @see Un_Serveur_Jeu
 */
public class Un_Serveur_CHAT extends Frame implements WindowListener, ActionListener, Runnable {
    private Thread Mon_Thread;
    private PipedOutputStream Mes_Tubes_Sorties_Clients[];
    private PipedInputStream Mon_Tube_Entree_Clients;
    private DataOutputStream Mes_Flux_Sorties_Clients[];
    private DataInputStream Mon_Flux_Entree_Clients;
    private TextArea Mon_Chp_Reception;
    private Button Mon_Btn_Quitter;
    private Panel Ma_Zone_Reception, Ma_Zone_Btn;

    /**
     * Le constructeur de la classe <B>Un_Serveur_CHAT</B> prend en parametre le nombre maximum de clients qu'il doit pouvoir gerer. Il affiche une fenetre de reception des messages, initialise ses flux de communications internes et se met en <B>Thread</B>
     */
    public Un_Serveur_CHAT(int Tel_Nb_Max_Clients) {
        super("Serveur CHAT");
        Mes_Tubes_Sorties_Clients = new PipedOutputStream[Tel_Nb_Max_Clients];
        Mes_Flux_Sorties_Clients = new DataOutputStream[Tel_Nb_Max_Clients];
        Mon_Tube_Entree_Clients = new PipedInputStream();
        Mon_Flux_Entree_Clients = new DataInputStream(Mon_Tube_Entree_Clients);
        addWindowListener(this);
        setTitle("Serveur CHAT");
        setSize(300, 300);
        setResizable(false);
        Mon_Chp_Reception = new TextArea("Serveur CHAT active\n", 14, 30, TextArea.SCROLLBARS_NONE);
        Mon_Chp_Reception.setEditable(false);
        Mon_Chp_Reception.setEnabled(false);
        Ma_Zone_Reception = new Panel();
        Ma_Zone_Reception.add("Center", Mon_Chp_Reception);
        Mon_Btn_Quitter = new Button("Quitter");
        Mon_Btn_Quitter.addActionListener(this);
        Ma_Zone_Btn = new Panel();
        Ma_Zone_Btn.add("Center", Mon_Btn_Quitter);
        add("North", Ma_Zone_Reception);
        add("South", Ma_Zone_Btn);
        active();
        start();
    }

    /**
     * La methode <B>donneFluxSortie</B> sert a envoyer a un canal de connexion client le flux auquel il doit se lier pour pouvoir recevoir les messages CHAT a emettre sur le reseau.
     *
     * @param Tel_No_Client - int representant le nombre maximum de clients a gerer.
     * @return Le flux de sortie dans lequel seront reemis les messages du CHAT pour le reseau.
     * @see Un_Canal_Connexion#activeFluxReceptionCHAT(PipedOutputStream)
     */
    public PipedOutputStream donneFluxSortie(int Tel_No_Client) {

        Mes_Tubes_Sorties_Clients[Tel_No_Client] = new PipedOutputStream();
        Mes_Flux_Sorties_Clients[Tel_No_Client] = new DataOutputStream(Mes_Tubes_Sorties_Clients[Tel_No_Client]);
        return Mes_Tubes_Sorties_Clients[Tel_No_Client];
    }

    /**
     * La methode <B>donneFluxReception</B> sert a envoyer a un canal de connexion client le flux commun ( <B>static</B> ) auquel il doit se lier pouvoir retransmettre au serveur de CHAT les messages qui lui sont destines en provenance du reseau.
     *
     * @return Le flux <B>static</B> vers lequel vont emettre les canaux clients.
     * @see Un_Canal_Connexion#lance(Socket, PipedInputStream)
     */
    public PipedInputStream donneFluxReception() {
        return Mon_Tube_Entree_Clients;
    }

    /**
     * La methode <B>active</B> permet au serveur de CHAT d'afficher sa fenetre de reception des messages.
     */
    public void active() {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT reactivee...");
        show();
    }

    /**
     * La methode <B>desactive</B> permet de cacher la fenetre de reception des messages des que celle-ci n'est plus utile.
     */
    public void desactive() {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT desactivee...");
        removeNotify();
    }

    void quitter() {
        desactive();
        System.err.println("Un_Serveur_CHAT : Sortie du serveur...     bye!");
        System.exit(0);
    }


    /**
     * La methode <B>start</B> de l'interface <B>Runnable</B> permet de faire passer le serveur de CHAT en <B>Thread</B> des la fin de son initialisation.
     */
    public synchronized void start() {
        if (Mon_Thread == null) {
            Mon_Thread = new Thread(this);
            Mon_Thread.setPriority(Thread.MAX_PRIORITY / 4);
            Mon_Thread.start();
        }
    }

    /**
     * La methode <B>run</B> de l'interface <B>Runnable</B> contient la boucle permanente qui permet de retransmettre a tous les canaux clients les messages recus dans le flux commun de reception.
     */
    public synchronized void run() {
        int Le_Nb_Car;
        byte Le_Buffer[] = new byte[1024];
        String La_Str;
        System.err.println("Un_Serveur_CHAT : Serveur CHAT en ecoute...");
        while (true) {
            try {
                Mon_Thread.sleep(100);
                if ((Le_Nb_Car = Mon_Flux_Entree_Clients.available()) != 0) {
                    La_Str = Mon_Flux_Entree_Clients.readUTF();
//                                        System.err.println( La_Str );
                    Mon_Chp_Reception.append(La_Str);
                    for (int Le_Cpt = 0; Le_Cpt < Mes_Flux_Sorties_Clients.length; Le_Cpt++)
                        if (Mes_Flux_Sorties_Clients[Le_Cpt] != null) {
                            Mes_Flux_Sorties_Clients[Le_Cpt].writeUTF(La_Str);
                            Mes_Flux_Sorties_Clients[Le_Cpt].flush();
                        }
                }
            } catch (Exception Telle_E) {
                System.err.println("Un_Serveur_CHAT : Erreur lors de la retransmission du message recu...");
            }
        }
    }

    /**
     * La methode <B>stop</B> de l'interface <B>Runnable</B> permet d'arreter le serveur de CHAT lors de la generation d'un evenement <B>windowClosing</B> dans une des fenetres affichees a l'ecran.
     */
    public synchronized void stop() {
        if (Mon_Thread != null) {
            Mon_Thread.stop();
            Mon_Thread = null;
        }
    }

    /**
     * La methode <B>actionPerformed</B> de l'interface <B>ActionListener</B> permet de gerer l'action de clic de la souris sur le bouton <B>Quitter</B>.
     */
    public void actionPerformed(ActionEvent Tel_Evt) {
        if (Tel_Evt.getSource() == Mon_Btn_Quitter) quitter();
    }

    /**
     * La methode <B>windowIconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'iconification de la fenetre.
     */
    public void windowIconified(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT iconified...");
    }

    /**
     * La methode <B>windowDeiconified</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de restauration de la fenetre.
     */
    public void windowDeiconified(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT restauree...");
    }

    /**
     * La methode <B>windowActivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'activation de la fenetre.
     */
    public void windowActivated(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT activee...");
    }

    /**
     * La methode <B>windowDeactivated</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement de desactivation de la fenetre.
     */
    public void windowDeactivated(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT desactivee...");
    }

    /**
     * La methode <B>windowOpened</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'ouverture de la fenetre.
     */
    public void windowOpened(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT ouverte...");
    }

    /**
     * La methode <B>windowClosing</B> de l'interface <B>WindowListener</B> permet de
     * gerer l'evenement de fermeture de la fenetre ( et du programme ).
     */
    public void windowClosing(WindowEvent Tel_Evt) {
        quitter();
    }

    /**
     * La methode <B>windowClosed</B> de l'interface <B>WindowListener</B> permet de gerer l'evenement d'adieu de la fenetre.
     */
    public void windowClosed(WindowEvent Tel_Evt) {
        System.err.println("Un_Serveur_CHAT : Fenetre serveur CHAT fermee...");
    }

}
