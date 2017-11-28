package com.dimitri.nffnn.example;

        import com.dimitri.nffnn.gfx.TimeGraph;
        import com.dimitri.nffnn.neural.Net;

        import java.awt.Canvas;
        import java.awt.Color;
        import java.awt.Dimension;
        import java.awt.Frame;
        import java.awt.Graphics;
        import java.awt.GraphicsConfiguration;
        import java.awt.Toolkit;
        import java.awt.event.KeyEvent;
        import java.awt.event.WindowAdapter;
        import java.awt.event.WindowEvent;
        import java.awt.image.VolatileImage;
        import java.io.FileNotFoundException;
        import java.util.Random;

public class Unit
{
    private Frame frame;
    private Canvas canvas;

    private int canvasWidth = 0;
    private int canvasHeight = 0;

    public final int GAME_WIDTH = 1080;
    public final int GAME_HEIGHT = (GAME_WIDTH*9)/16;

    public Dimension screenSize;

    private int scale = 0;

    private GraphicsConfiguration gc;
    private VolatileImage vImage;

    protected int FPS = 0;
    protected int UPS = 0;
    protected int CPS = 0;

    private TimeGraph graph;

    Net net;
    Random random = new Random();
    double[] input = new double[4];
    double[] target = new double[4];

    double lastinput = 0;

    private int amount = 0;
    private int maxAmount = 10;

    public Unit(){
        getBestSize();

        net = new Net(new int[]{4,5,4}, true);
        graph = new TimeGraph(net,0,GAME_HEIGHT-200,GAME_WIDTH, 200, "errorRate");

        frame = new Frame();
        canvas = new Canvas();

        canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        canvas.addKeyListener(new InputHandler());
        canvas.addMouseListener(new InputHandler());
        canvas.addMouseMotionListener(new InputHandler());

        frame.add(canvas);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                Test.quit(net);
            }
        });

        frame.setVisible(true);
        gc = canvas.getGraphicsConfiguration();
        vImage = gc.createCompatibleVolatileImage(GAME_WIDTH, GAME_HEIGHT);

    }

    private void getBestSize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        screenSize = toolkit.getScreenSize();
        canvasWidth = GAME_WIDTH*1;
        scale = canvasWidth/GAME_WIDTH;
        canvasHeight = scale*GAME_HEIGHT;
    }

    public void update(){

        net.feedForward(inputFetcher.next());

        net.backPropogation(target);
        double[] output = net.getOutput();
        System.out.println("Input: " + input[0] + ", " + input[1]);
        System.out.println("Target: " + target[0] + ", " + target[1]);
        System.out.println("Output: " + output[0] + ", " + output[1]);
        System.out.println(net.getRecentAverageError());

        graph.addValue(net.getRecentAverageError());
        graph.update();

    }

    public void render(){
        if(vImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE){
            vImage = gc.createCompatibleVolatileImage(GAME_WIDTH, GAME_HEIGHT);
        }

        Graphics g = vImage.getGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.setColor(Color.WHITE);
        /**Draw stuff here*/

        graph.render(g);

//        g.setColor(Color.GREEN);
//        g.drawString("FPS: " + String.valueOf(FPS) , 0, 10);
//        g.drawString("UPS: " + String.valueOf(UPS) , 60, 10);

        g = canvas.getGraphics();
        g.drawImage(vImage, 0, 0, canvasWidth, canvasHeight, null);

        g.dispose();
    }



}
