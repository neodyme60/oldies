import java.awt.*;


class Info_Joueur {
    private static int X_REZO = 160;
    private static int Y_REZO = 100;

    private Image mon_visage;
    private Image mon_fond;
    private MediaTracker Tr;

    public Info_Joueur(Image pic, Frame maman) {
        Tr = new MediaTracker(maman);
        mon_visage = pic;
        Toolkit systemD = maman.getToolkit();

        System.err.println("Info joueur : Chargement image de fond ...");
        mon_fond = systemD.getImage("fond_info.jpg");
        Tr.addImage(mon_fond, 0);

        try {
            Tr.waitForID(0);
        } catch (InterruptedException e) {
        }

        System.err.println("Info joueur : Chargement image de fond OK!");

    }//constructeur


    public void refresh(int nrj, int pts, Graphics g) {
        g.drawImage(mon_fond, 0, 0, null);
        g.drawImage(mon_visage, 10, 10, null);
        g.setColor(new Color(255, 255, 255));
        g.drawString("ENERGIE", 55, 85);
        g.drawString("SCORE", 90, 20);
        g.drawString(new String(Integer.toString(pts)), 90, 30);

        g.setColor(new Color(125, 125, 125));
        g.draw3DRect(29, 90, 102, 6, true);
        g.setColor(new Color(255 - nrj * 2, nrj * 2, nrj));//rouge au vert
        for (int i = 0; i <= nrj; i++) {
            g.drawLine(30 + i, 91, 30 + i, 95);
        }
    }
}

