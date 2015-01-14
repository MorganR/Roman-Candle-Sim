import java.util.ArrayList;

/**
 * Emits sparks from the star for a big explosion
 * @author Morgan Roff
 * @version 1.0
 */
public class BigSparkEmitter extends StarSparkEmitter {
	//Lifetime of the sparks (s)
	private final static double LIFETIME = 1.0;
	//Number of sparks
	private final static int NUMBER = 30;
	
	public BigSparkEmitter(double xPos, double yPos, double xVel, double yVel,
			double newExitV, double angle, double variation) throws EmitterException {
		super(xPos, yPos, xVel, yVel, newExitV, angle, variation);
	}
	//Must override StarSparkEmitter so lifetime is 1.0 and number is 30
	@Override
	public ArrayList<Spark> launch(double time) {
		double angle;
		double vX; 
		double vY;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<Spark> spark = new ArrayList<>(NUMBER);
		if (time - getEmitTime() > 0.01) {
			setEmitTime(time);	
			for (int i = 0; i < NUMBER; i++) {
				angle = getRandomLaunchAngle();
				vX = velocity[X] + getExitVelocity()*Math.sin(angle);
				vY = velocity[Y] + getExitVelocity()*Math.cos(angle);
				spark.add(new Spark(time, position[X], position[Y], vX, vY, LIFETIME, super.getColour()));
			}
		}
		return spark;
	}

}
