import java.util.ArrayList;

public class DelaySparkEmitter extends Emitter {
	
	//Number of sparks
	private final static int NUMBER = 5;
	//Lifetime of the sparks (s)
	private final static double LIFETIME = 0.6;
	private double emitTime;
	
	public DelaySparkEmitter(double xPos, double yPos, double xVel,
			double yVel, double newExitV, double newAngle, double variation)
			throws EmitterException {
		super(xPos, yPos, xVel, yVel, newExitV, newAngle, variation);
		if (variation < -90 || variation > 90)
			throw new EmitterException ("Delay spark angle has too much variation\nVariation: " + variation);
		emitTime = 0;
	}

	public DelaySparkEmitter(double xPos, double yPos, int xVel, int yVel,
			double newExitV, Conditions current, int variation) {
		super(xPos, yPos, xVel, yVel, newExitV, current, variation);
	}

	public ArrayList<Spark> launch(double time) {
		double angle;
		double vX; 
		double vY;
		double[] position = getPosition();
		double[] velocity = getVelocity();
		ArrayList<Spark> spark = new ArrayList<>(NUMBER);
		if(time - emitTime > 0.03) {
			emitTime = time;
			for (int i = 0; i < NUMBER; i++) {
				angle = getRandomLaunchAngle();
				// Sparks are emitted on even intervals between "time" and "time + 0.05"
				//emitTime = time + 0.01*(double)i;
				//Velocity equals velocity of the emitter + the relative velocity of the spark (exit velocity)
				vX = velocity[X] + getExitVelocity()*Math.sin(angle);
				vY = velocity[Y] + getExitVelocity()*Math.cos(angle);
				spark.add(new Spark(time, position[X], position[Y], vX, vY, LIFETIME, "orange"));
			}
		}
		return spark;
	}
	
}

