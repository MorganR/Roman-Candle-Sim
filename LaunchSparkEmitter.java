import java.util.ArrayList;


public class LaunchSparkEmitter extends Emitter {

	//Lifetime of the sparks (s)
	private final static double LIFETIME = 0.15;
	//Number of sparks
	private final static int NUMBER = 20;
	
	public LaunchSparkEmitter(double xPos, double yPos, double xVel,
			double yVel, double newExitV, double newAngle, double variation)
			throws EmitterException {
		super(xPos, yPos, xVel, yVel, newExitV, newAngle, variation);
		if (variation < -10 || variation > 10)
			throw new EmitterException ("Launch spark angle has too much variation\nVariation: " + variation);
	}

	public LaunchSparkEmitter(double xPos, double yPos, int xVel, int yVel,
			int newExitV, Conditions current, int variation) {
		super(xPos, yPos, xVel, yVel, newExitV, current, variation);
//		System.out.println("Current angle: " + current.getAngle());
//		System.out.println("Variation: " + Math.toRadians(variation));
	}

	public ArrayList<LaunchSpark> launch(double time) {
		double angle;
		double vX; 
		double vY;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<LaunchSpark> spark = new ArrayList<>(NUMBER);
		double emitTime;
		for (int i = 0; i < NUMBER; i++) {
			angle = getRandomLaunchAngle();
			//All sparks emitted at launch
			emitTime = time;
			//Velocity equals velocity of the emitter + the relative velocity of the spark (exit velocity)
			vX = velocity[X] + getExitVelocity()*Math.sin(angle);
			vY = velocity[Y] + getExitVelocity()*Math.cos(angle);
			spark.add(new LaunchSpark(emitTime, position[X], position[Y], vX, vY, LIFETIME, "orange"));
		}
		return spark;
	}
	
}
