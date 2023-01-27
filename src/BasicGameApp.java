import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class BasicGameApp implements Runnable {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;
    public Image astroPic;
    public Image dogPic;
    public Image background;

    public Image seaweedPic;
    public Image diedPic;
    //Declare the objects used in the program
    //These are things that are made up of more than one variable type
    private Fish astro;
    private Fish jack;
    private Fish dog;
    private Fish seaweed;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game the "new" makes it a constructor
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // Constructor Method
    // This has the same name as the class
    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() {
        //constructor

        setUpGraphics();

        //variable and objects
        //create (construct) the objects needed for the game and load up
        astroPic = Toolkit.getDefaultToolkit().getImage("fish.png"); //load the picture
        dogPic = Toolkit.getDefaultToolkit().getImage("dog.png");
        background = Toolkit.getDefaultToolkit().getImage("underwater.png");
        seaweedPic = Toolkit.getDefaultToolkit().getImage("seaweed.png");
        diedPic = Toolkit.getDefaultToolkit().getImage("died.png");
        astro = new Fish(10, 100);
        jack = new Fish(300, 40);
        dog = new Fish(160, 354);
        seaweed = new Fish (600,500);
        dog.height = 80;
        dog.width = 100;
        jack.dy = -1;
        jack.dx = 3;
        dog.dy = 2;
        astro.dx = 3;


    }// BasicGameApp()


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {

        //for the moment we will loop things forever.
        while (true) {

            moveThings();  //move all the game objects
            render();  // paint the graphics
            pause(8); // sleep for 10 ms
        }
    }

    public void crash() {
        //System.out.println("w: " + dog.rec.width+"h: " +dog.rec.height);


        if (jack.rec.intersects(astro.rec) && jack.isAlive == true && astro.isAlive == true) {
            System.out.println("crash");
            astro.dx = -1 * astro.dx;
            astro.dy = -1 * astro.dy;
            jack.dx = -jack.dx;

        }


        //System.out.println("jack rec x: "+ jack.rec.x +"jack rec y: " +jack.rec.y);
        System.out.println(" jack crash count" + jack.crashCount);
        if (jack.rec.intersects(dog.rec) && astro.isAlive == true && jack.isCrashingDog == false) {

            if (jack.crashCount % 2 == 1) {
                System.out.println("crash");
                jack.isAlive = false;
                // jack.resurect = true;

                // dog.(Color.RED);
                dog.changeColor = true;

            } else {
                System.out.println("resurect jack");
                jack.isAlive = true;
            }
            jack.isCrashingDog = true;

            jack.crashCount++;
        }
        if (astro.rec.intersects(dog.rec) && astro.isCrashingDog == false) {
            // System.out.println("crash");
            System.out.println("crash");
            dog.dx = -1 * dog.dx;
            dog.dy = -1 * dog.dy;
            astro.dy = -astro.dy;
            expand();
            System.out.println("w: " + astro.rec.width + "h: " + astro.rec.height);
            astro.isCrashingDog = true;
        }
        if (astro.rec.intersects(dog.rec) == false) {
            astro.isCrashingDog = false;
        }
        if (jack.rec.intersects(dog.rec) == false) {
            jack.isCrashingDog = false;
        }
    }

    public void expand() {
        if (astro.height < 100) {
            astro.height = (int) (1.5 * astro.height);
            if (astro.width < 100) {
                astro.width = (int) (1.2 * astro.width);
            }
        }
    }

    public void moveThings() {
        //calls the move( ) code in the objects
        //expand();
        crash();
        astro.bounce();
        jack.bounce();
        dog.bounce();


    }


    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");

    }


    //paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        //draw the image of the astronaut
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width, astro.height, null);
        g.drawImage(dogPic, dog.xpos, dog.ypos, dog.width, dog.height, null);
        g.drawImage(seaweedPic, seaweed.xpos, seaweed.ypos, seaweed.width, seaweed.height, null);
        if (jack.isAlive == true ) {
            g.drawImage(astroPic, jack.xpos, jack.ypos, jack.width, jack.height, null);
//            g.draw(new Rectangle(jack.xpos, jack.ypos, jack.width, jack.height));
            g.setColor(Color.CYAN);
            g.draw(new Rectangle(jack.xpos, jack.ypos, jack.width, jack.height));

        }
        if (jack.isAlive == false ) {
            g.drawImage(diedPic,400,300,300,80,null);
        }
        if (jack.crashCount == 5){
            g.drawImage(diedPic,0,0,1000,1000,null);
        }

        g.draw(new Rectangle(astro.xpos, astro.ypos, astro.width, astro.height));
        if (dog.changeColor == true) {
            g.setColor(Color.RED);
        }
        g.draw(new Rectangle(dog.xpos, dog.ypos, dog.width, dog.height));
        g.dispose();

        bufferStrategy.show();
    }}


