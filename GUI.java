import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.imageio.ImageIO;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.BasicStroke;

/**
 * This class instantiates the GUI for the firework simulator.
 * It also initializes the firework simulation upon pressing the start button
 * @author Morgan Roff
 * @version 1.0
 */
public class GUI extends JFrame {
	
	private final int WIDTH = 1300;
	private final int HEIGHT = 768;
	private double timeInterval;
	
	private BorderLayout layout;
	private Box box;
	private JPanel bottomPanel;
	private ParticleManager manager;
	
	private JSlider angSlide;
	private JSlider windSlide;
	private boolean sound;
	private JButton soundButton;
	
	public Conditions current;
	
	private BufferedImage image;
	private BufferedImage night;
	private BufferedImage red;
	private BufferedImage blue;
	private BufferedImage cyan;
	private BufferedImage green;
	private BufferedImage orange;
	private BufferedImage lit;
	private BufferedImage magenta;
	private ImagePanel imgPanel;
	
	private Sound song;
	
	private AffineTransform rotate;
	private Rectangle tubRectangle;
	
	private Timer timer;
	private double time;
	private boolean isStar = false;
	private String s;
	private int y0;
	private int x0;
	//Use an impossible base value to indicate it is not set
	//Base is location of firework launch
	private int xBase = 50;
	private int yBase = 50;
	
	private ArrayList<Particle> guiFireworks = new ArrayList<>();
	
	/**
	 * The constructor for the GUI builds the window and loads necessary external files
	 */
	public GUI() {
		
		super();
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Roman Candle Simulator - ASSN4_11mr60");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width-WIDTH)/2, (dim.height - HEIGHT)/2);
		getContentPane().setBackground(Color.BLACK);
		
		Font fText = new Font("Arial", Font.PLAIN, 20);
		Font fButton = new Font("Arial", Font.BOLD, 18);
		//Establish computation interval
		timeInterval = 0.005;
		//Establish object for storing wind and angle
		current = new Conditions();
		//Initialize particle manager;
		manager = null;
		song = new Sound("Firework.wav");
		try {
			manager = new ParticleManager(current);
		} catch (EnvironmentException except) {
			System.out.println(except.getMessage());
			return;
		} catch (EmitterException except) {
			System.out.println(except.getMessage());			
			return;
		}
		//Initialize timer
		timer = new Timer((int)(timeInterval*1000), new ClockListener());
		//Initialize array of guiFireworks
		guiFireworks = null;
		
		//Create outer border layout
		layout = new BorderLayout(0,20);
        setLayout(layout);
        //Create box layout for animation
        box = Box.createVerticalBox();
        box.add(Box.createRigidArea(new Dimension(50, 10)));
		
        //Load background images (put them in your src folder!)
		image = null;
		night = null;
		orange = null;
		green = null;
		blue = null;
		magenta = null;
		red = null;
		lit = null;
		ClassLoader loader = getClass().getClassLoader();
		
		try {
			night = ImageIO.read(ClassLoader.getSystemResource("Night.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			green = ImageIO.read(loader.getSystemResource("Green.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			blue = ImageIO.read(loader.getSystemResource("Blue.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			cyan = ImageIO.read(loader.getSystemResource("Cyan.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			magenta = ImageIO.read(loader.getSystemResource("Magenta.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			red = ImageIO.read(loader.getSystemResource("Red.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			orange = ImageIO.read(loader.getSystemResource("Orange.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		try {
			lit = ImageIO.read(loader.getSystemResource("Lit.jpg"));
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		image = night;
		
		imgPanel = new ImagePanel();

		//Create flow layout for bottom panel
		bottomPanel = new JPanel(new FlowLayout());
		
		JLabel angLab = new JLabel("Firing Angle (Degrees):");
		angLab.setFont(fText);
		JLabel windLab = new JLabel("Wind Speed (km/h):");
		windLab.setFont(fText);
		angSlide = mySlide(15);
		angSlide.addChangeListener(new AngleListener());
		windSlide = mySlide(20);
		windSlide.addChangeListener(new WindListener());
		JButton startButton = new JButton("Start");
		startButton.setFont(fButton);
		startButton.addActionListener(new StartListener());
		sound = true;
		soundButton = new JButton("Mute");
		soundButton.setFont(fButton);
		soundButton.addActionListener(new SoundListener());
		JButton quitButton = new JButton("Quit");
		quitButton.setFont(fButton);
		quitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				song.closeFile();
				System.exit(0);
			}
		});
		
		bottomPanel.add(angLab);
		bottomPanel.add(angSlide);
		bottomPanel.add(windLab);
		bottomPanel.add(windSlide);
		bottomPanel.add(startButton);
		bottomPanel.add(soundButton);
		bottomPanel.add(quitButton);
		
		add(imgPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private JSlider mySlide(int limit) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL,-limit,limit,0);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setToolTipText("0");
		return slider;
	}
	
	private class AngleListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			current.setAngle(angSlide.getValue());
			angSlide.setToolTipText(Integer.toString(angSlide.getValue()));
			imgPanel.repaint();
		}
	}
	
	private class WindListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			current.setWind(windSlide.getValue());
			windSlide.setToolTipText(Integer.toString(current.getWind()));
		}
	}
	
	private class ImagePanel extends JPanel {

    	public ImagePanel() {
    		super();
    	}
    	
    	public void paint(Graphics g) {
			isStar = false;
			Graphics2D g2D = (Graphics2D)g;
			y0 = 0;
			x0 = 0;
    		if (image != null) {
        		g.setColor(Color.BLACK);
    			g.fillRect(0, 0, getWidth(), getHeight());
    			g.drawImage(image, (getWidth()-image.getWidth())/2, (getHeight()-image.getHeight())/2, image.getWidth(), image.getHeight(), null);
    		}
			if (image.getHeight()>imgPanel.getHeight())
				y0 = imgPanel.getHeight()-30;
			else
				y0 = image.getHeight()+(imgPanel.getHeight()-image.getHeight())/2 - 30;
			x0 = imgPanel.getWidth()/2;
    		if (guiFireworks != null) {
    			for(Particle firework : guiFireworks) {
    				paintParticle(g2D, firework);
    			}
    			if (!isStar) {
    					image = night;
    					xBase = 50;
    					yBase = 50;
    			}
    		}
			g2D.setColor(Color.GRAY);
			tubRectangle = new Rectangle(x0-5, y0, 10, 30);
			rotate = g2D.getTransform();
			rotate.setToRotation(current.getAngle(), x0, y0+30);
			g2D.setTransform(rotate);
			g2D.fill(tubRectangle);
    		rotate.setToRotation(0, x0, y0+30);
    		g2D.setTransform(rotate);
    	}
    	
    }
	
	private void paintParticle(Graphics2D g, Particle firework) {
		switch (firework.getColour()) {
		case "blue": 
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = blue;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.BLUE);
		break;
		case "green":
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = green;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.GREEN);
		break;
		case "red":
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = red;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.RED);
			break;
		case "purple":
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = magenta;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.MAGENTA);
			break;
		case "pink":
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = red;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.PINK);
			break;
		case "orange": 
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = orange;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.ORANGE);
			break;
		case "yellow": 
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = lit;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.YELLOW);
			break;
		case "cyan": 
			if (firework instanceof Star || (firework instanceof Spark && firework.getLifetime() != 0.6)) {
				image = cyan;
				isStar = true;
				if (xBase == 50) {
					xBase = (int)Math.round((30.0*Math.sin(current.getAngle())));
					yBase = (int)(30.0*Math.cos(current.getAngle()));
				}
			}
			g.setColor(Color.CYAN);
			break;
		}
		if (firework instanceof LaunchSpark) {
			g.setStroke(new BasicStroke(firework.getRenderSize()));
            g.draw(new Line2D.Float(x0 + (int)Math.round((30.0*Math.sin(current.getAngle()))), y0+30-(int)(30.0*Math.cos(current.getAngle())), getPixel(firework)[0], getPixel(firework)[1]));
		}
		else {
			int [] pixel = getPixel(firework);
			g.fillOval(pixel[0], pixel[1], firework.getRenderSize(), firework.getRenderSize());
		}
	}
	
	private class StartListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (manager.getLaunchFlag()) {
				guiFireworks = null;
				timer.stop();
				imgPanel.repaint();
				song.endSong();
			}
			song.playSong();
			manager = null;
			try {
				manager = new ParticleManager(current);
			} catch (EnvironmentException except) {
				System.out.println(except.getMessage());
				return;
			} catch (EmitterException except) {
				System.out.println(except.getMessage());			
				return;
			}
			timer.start();
			time = System.currentTimeMillis()/1000.0;
			manager.start(time, current);
			guiFireworks = manager.getFireworks(time, current);
			imgPanel.repaint();
		}
	}
	
	private class SoundListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (sound) {
				song.quiet();
				soundButton.setText("Unmute");
				sound = false;
			} else {
				song.loud();
				soundButton.setText("Mute");
				sound = true;

			}
		}
	}
	
	private class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			time = (System.currentTimeMillis())/1000.0;
			if(guiFireworks.isEmpty()) {
				timer.stop();
				image = night;
			}
			else
				guiFireworks = manager.getFireworks(time, current);
			imgPanel.repaint();	
		}
	}
	
	private int[] getPixel(Particle spark) {
		int[] position = new int[2];
		if (spark instanceof Star || (spark instanceof Spark && spark.getLifetime() != 0.6)){
			position[0] = (int)Math.round(spark.getPosition()[0]*(image.getWidth()/35.0)) + x0 + xBase - spark.getRenderSize()/2;
			position[1] = -(int)Math.round(spark.getPosition()[1]*(image.getHeight()/35.0)) + y0 + 30 - yBase - spark.getRenderSize();
		}
		else {
			position[0] = (int)Math.round(spark.getPosition()[0]*(image.getWidth()/35.0)) + x0 + (int)Math.round((30.0*Math.sin(current.getAngle()))) - spark.getRenderSize()/2;
			position[1] = -(int)Math.round(spark.getPosition()[1]*(image.getHeight()/35.0)) + y0 + 30 - (int)(30.0*Math.cos(current.getAngle())) - spark.getRenderSize();
		}
			return position.clone();
	}

	
	
}
