import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class twirl extends Applet implements Runnable {
    Image offScreenImage;

    int deg = 255;

    int width = 512;
    int height = 512;

    int winwidth = 512;
    int winheight = 512;

    Thread thread1;
    MediaTracker M;
    Image teximg;

    int index, index2;
    int[] screen = new int[winwidth * winheight];

    ColorModel TargetCM;
    Image TargetImage;
    MemoryImageSource TargetMIS;
    Graphics DoubleBufferGraphics;

    //
    //neodyme code
    //

    //le buffer de rendu du twirl
    int xBufferSize = width;
    int yBufferSize = height;            //donc 200 valeur pour faire 360 degree
    int[] data = new int[xBufferSize * yBufferSize]; //contien la couleur de roation pour chaque ligne
    int[] dataTxt = new int[xBufferSize * yBufferSize]; //contien la texture pour chaque ligne
    double[] dataZ = new double[xBufferSize]; //contien la couleur de roation pour chaque ligne

    //les 4 point avants rotation
    double[] pIn;
    //les 4 points apres rotation
    double[] pOut;
    //les 4 coordonnes x de texture
    double[] pTxtX;


    double alpha = 2.0;
    double tetha = 0.015;

    int[] tex1;//=new int [256*256];


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


        for (int x = 0; x < winheight * winwidth; x++) {
            screen[x] = 0xFF000000;
        }

        tex1 = new int[256 * 256];


        PixelGrabber imagegrabber;
        imagegrabber = new PixelGrabber(teximg, 0, 0, 256, 256, tex1, 0, 256);
        try {
            imagegrabber.grabPixels();
        } catch (InterruptedException e) {
        }


        //alloc 5 points(x,z)
        pIn = new double[5 * 2];
        pOut = new double[5 * 2];
        pTxtX = new double[5 * 2];

        //init p0
        pIn[0] = -(xBufferSize / 8);
        pIn[1] = -(xBufferSize / 4);
        pTxtX[0] = 0;

        //init p1
        pIn[2] = -(xBufferSize / 8);
        pIn[3] = +(xBufferSize / 4);
        pTxtX[1] = 0.25;

        //init p2
        pIn[4] = +(xBufferSize / 8);
        pIn[5] = +(xBufferSize / 4);
        pTxtX[2] = 0.5;

        //init p3
        pIn[6] = +(xBufferSize / 8);
        pIn[7] = -(xBufferSize / 4);
        pTxtX[3] = 0.75;

        //init p4
        pIn[8] = pIn[0];
        pIn[9] = pIn[1];
        pTxtX[4] = 1.0f;

        float phi = 0.0f;
        int indexDest = 0;
        phi = 0.0f;
        for (int y = 0; y < yBufferSize; y++) {
            for (int p = 0; p < 5; p++) {
                //x'=          x*cos(phi)             +    z*sin(phi)
                pOut[(p * 2) + 0] = +pIn[p * 2] * Math.cos(phi) + pIn[(p * 2) + 1] * Math.sin(phi);
                //z'=          -x*sin(phi)            +    z*cos(phi)
                pOut[(p * 2) + 1] = -pIn[p * 2] * Math.sin(phi) + pIn[(p * 2) + 1] * Math.cos(phi);
            }

            //clear the zbuffer line with far data (+oo)
            for (int x = 0; x < xBufferSize; x++)
                dataZ[x] = 99999999.;

            //clear the line
            for (int x = 0; x < xBufferSize; x++)
                data[x + indexDest] = -1;


            //interpolation
            for (int p = 0; p < 4; p++) {
                int a = p;
                int b = (p + 1);

                int startx = (int) (pOut[a * 2] + 0.5);
                int endx = (int) (pOut[b * 2] + 0.5);

                double dd = (pOut[b * 2] - pOut[a * 2]);

                //
                //calcul color
                //
                double xMiddle = pOut[(a * 2) + 0] + (pOut[(b * 2) + 0] - pOut[(a * 2) + 0]) / 2.0;
                double zMiddle = pOut[(a * 2) + 1] + (pOut[(b * 2) + 1] - pOut[(a * 2) + 1]) / 2.0;
                //make a dot (x*x'+z*z') avec eye(0,-1)
                int color = 00 + (int) ((zMiddle / Math.sqrt(xMiddle * xMiddle + zMiddle * zMiddle)) * 255.0);

                double xIncr = (pOut[(b * 2) + 0] - pOut[(a * 2) + 0]) / dd;
                double zIncr = (pOut[(b * 2) + 1] - pOut[(a * 2) + 1]) / dd;

                double xTxtIncr = 0.0;
                if (dd != 0.0)
                    xTxtIncr = (pTxtX[b] - pTxtX[a]) / dd;

                double zStart = pOut[(a * 2) + 1];
                double xStart = pOut[(a * 2) + 0];

                double xTxtStart = pTxtX[a];


                for (int x = startx; x <= endx; x++) {
                    if (zStart < dataZ[x + (xBufferSize / 2)]) {
                        data[x + indexDest + (xBufferSize / 2)] = color | (color << 8) | (color << 16);
                        dataTxt[x + indexDest + (xBufferSize / 2)] = (int) (xTxtStart * 255.0);
                    }

                    xStart += xIncr;
                    zStart += zIncr;

                    xTxtStart += xTxtIncr;

                }

            }
            indexDest += xBufferSize;
            phi += 3.1416f / 180.0f;
        }

    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        moveall();

        TargetMIS.newPixels();
        g.drawImage(TargetImage, 0, 0, this);
    }

    public void moveall() {
        double aa = alpha;
        int index = 0;
        tetha = alpha;
        for (int y = 0; y < height; y++) {
            int yy = (int) ((float) deg * (Math.abs(Math.sin(aa + Math.sin(tetha)))));
            for (int x = 0; x < width; x++) {

                if (data[x + (yy * width)] == -1)
                    screen[x + (y * width)] = 0;
                else {
                    int pixelTexture = tex1[(dataTxt[x + (width * yy)] + ((y << 8)) & 0xffff)];
                    int lightMap = data[x + (width * yy)];

                    pixelTexture = (pixelTexture & 0x00fefefe) >> 1;
                    lightMap = (lightMap & 0x00fefefe) >> 1;
                    screen[x + (y * width)] = pixelTexture + lightMap;

                }
            }
            index2 += width;

            tetha += 0.008;
        }

        alpha += 0.01;
    }

    public void start() {
        thread1 = new Thread(this);
        thread1.start();
    }

    public void run() {

        repaint();

        while (Thread.currentThread() == thread1) {
            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
}



