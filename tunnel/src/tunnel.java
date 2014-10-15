import java.applet.Applet;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class tunnel extends Applet implements Runnable {
    Image offScreenImage;
    int width = 512;
    int height = 512;
    int[] buff2 = new int[width * height * 4];
    int winwidth = 512;
    int winheight = 512;
    int[] screen = new int[winwidth * winheight];
    int a;    int ofsb;
    double aa, bb, cc, dd;
    Thread thread1;
    int nbframes, thetimer, fpstimer, fps;
    float framespd, ofstexf, ofstexf2;
    boolean makegfxflag, loadflag;
    MediaTracker M;
    Image teximg;
    int index, index2;
    int ofstex;
    int[] tex1 = new int[256 * 256 * 4 * 2];

    ColorModel TargetCM;
    Image TargetImage;
    MemoryImageSource TargetMIS;
    Graphics DoubleBufferGraphics;

    public void init() {

        setBackground(Color.black);
        TargetCM = new DirectColorModel(32, 0x00FF0000, 0x000FF00, 0x000000FF, 0);
        offScreenImage = createImage(width, height);
        DoubleBufferGraphics = offScreenImage.getGraphics();
        TargetMIS = new MemoryImageSource(width, height, TargetCM, screen, 0, width);
        TargetMIS.setAnimated(true);
        TargetMIS.setFullBufferUpdates(true);
        TargetImage = createImage(TargetMIS);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        M = new MediaTracker(this);
        teximg = getImage(getCodeBase(), "tex101.jpg");
        M.addImage(teximg, 0);
        makegfxflag = true;
        loadflag = true;

        for (int x = 0; x < winheight * winwidth; x++) {
            screen[x] = 0xFF000000;
        }

        aa = 10.5;
        bb = 4.2;
        ofstexf = 0;
        ofstexf2 = 0;

    }

    public void update(Graphics g) {

        paint(g);
    }

    public void paint(Graphics g) {
        if (!loadflag) {

            nbframes++;
            moveall();

            TargetMIS.newPixels();
            g.drawImage(TargetImage, 0, 0, this);
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.white);
            g.drawString("Loading...", 15, 25);
            loaderscreen();
        }

    }

    public void moveall() {
        aa += 0.01 * framespd;
        bb += 0.013 * framespd;
        ofstexf -= 4.1f * framespd;
        if (ofstexf >= 256.0f)
            ofstexf -= 256.0f;
        if (ofstexf < 0)
            ofstexf += 256.0f;
        ofstexf2 += 1.1f * framespd;
        if (ofstexf2 >= 256.0f)
            ofstexf2 -= 256.0f;
        if (ofstexf2 < 0)
            ofstexf2 += 256.0f;

        ofstex = 256 * (int) ofstexf2 + (int) ofstexf;

        cc = width * 0.5 + width * 0.5 * Math.cos(aa * 0.9) + width * 2 * (int) (height * 0.5 + height * 0.5 * Math.sin(bb));
        a = (int) cc;
        index = 0;
        index2 = a;


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ofsb = buff2[index2++];
                screen[index++] = tex1[ofsb + ofstex];
            }
            index2 += width;
        }

    }

    public void loaderscreen() {
        if (makegfxflag) {
            while (!M.checkAll(true)) {
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                }
            }
        }

        PixelGrabber imagegrabber;
        imagegrabber = new PixelGrabber(teximg, 0, 0, 256, 256, tex1, 0, 256);
        try {
            imagegrabber.grabPixels();
        } catch (InterruptedException e) {
        }

        for (int x = 0; x < 256 * 256; x++) {
            tex1[x + 256 * 256] = tex1[x];
        }

        index = 0;
        for (int y = 0; y < height * 2; y++) {
            for (int x = 0; x < width * 2; x++) {
                cc = x - width;
                dd = y - height;
                aa = 2 * 4096 * Math.atan((Math.sqrt(cc * cc + dd * dd)));
                bb = 2 * (256 * Math.atan2(cc, dd)) / Math.PI;
                buff2[index] = ((int) aa & 0xFF) + 256 * ((int) bb & 0xFF);
                index++;
            }
        }

        loadflag = false;
        teximg = null;
    }

    public void start() {
        thread1 = new Thread(this);
        thread1.start();
    }

    public void run() {

        fpstimer = (int) System.currentTimeMillis();
        nbframes = 0;
        framespd = 0.002f;
        repaint();

        while (Thread.currentThread() == thread1) {
            repaint();

            thetimer = (int) System.currentTimeMillis();

            if ((thetimer - fpstimer) > 1000) {
                fps = nbframes;
                fpstimer = thetimer;
                nbframes = 0;
                framespd = framespd * 0.50f + 0.50f * (50.0f / (float) fps);
                if (framespd < 0.002f)
                    framespd = 0.002f;
                if (framespd > 20.0f)
                    framespd = 20.0f;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
}



