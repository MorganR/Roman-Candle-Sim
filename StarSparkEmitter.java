import java.util.ArrayList;

/**
 * Emits sparks from the star
 * @author Morgan Roff
 * @version 2.0
 */
public class StarSparkEmitter extends Emitter {
	//Lifetime of the sparks (s)
	private final static double LIFETIME = 0.3;
	//Number of sparks
	private final static int NUMBER = 20;
	private String colour;
	private double emitTime;
	
	public StarSparkEmitter(double xPos, double yPos, double xVel, double yVel,
			double newExitV, double angle, double variation)
			throws EmitterException {
		super(xPos, yPos, xVel, yVel, newExitV, angle, variation);
		emitTime = 0;
	}
	
	/**
	 * Set the colour of the emitted sparks
	 * @param newColour
	 */
	public void setColour(String newColour) {
		colour = newColour;
	}
	
	/**
	 * Get the colour of sparks as a string
	 * @return colour The colour as a string
	 */
	public String getColour() {
		return colour;
	}
	
	public double getEmitTime() {
		return emitTime;
	}
	
	public void setEmitTime(double time) {
		emitTime = time;
	}
	
	public ArrayList<Spark> launch(double time) {
		double angle;
		double vX; 
		double vY;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<Spark> spark = new ArrayList<>(NUMBER);
		if (time - emitTime > 0.01) {
			emitTime = time;
			for (int i = 0; i < NUMBER; i++) {
				angle = getRandomLaunchAngle();
				vX = velocity[X] + getExitVelocity()*Math.sin(angle);
				vY = velocity[Y] + getExitVelocity()*Math.cos(angle);
				spark.add(new Spark(time, position[X], position[Y], vX, vY, LIFETIME, colour));
			}
		}
		return spark;
	}
}
