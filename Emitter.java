import java.util.ArrayList;

public abstract class Emitter extends Firework {
	//Exit velocity of the spark relative to the emitter
	private double exitV;
	//Angle (radians)
	private Conditions current;
	//Variation in angle (radians)
	private double var;
	
	public Emitter(double xPos, double yPos, double xVel, double yVel, double newExitV, double newAngle, double variation) throws EmitterException {
		super(xPos, yPos, xVel, yVel);
		current = new Conditions();
		exitV = newExitV;
		if (newAngle < -180 || newAngle > 180)
			throw new EmitterException("The emitter angle is not legal!\nAngle: " + newAngle);
		else
			current.setAngle(newAngle);
		var = Math.toRadians(variation);
	}
	
	public Emitter(double xPos, double yPos, double xVel, double yVel, double newExitV, Conditions current, double variation) {
		super(xPos, yPos, xVel, yVel);
		this.current = current;
		var = Math.toRadians(variation);
		exitV = newExitV;
	}
	
	public double getRandomLaunchAngle() {
		double launchAngle = getRand(current.getAngle() - var, current.getAngle() + var);
		return launchAngle;
	}
	
	public double getExitVelocity() {
		return exitV;
	}
	
	public void setExitVelocity(double newVel) {
		exitV = newVel;
	}
	
	public double getLaunchAngle() {
		return current.getAngle();
	}
	
	public abstract ArrayList<? extends Particle> launch(double time);
	
}
