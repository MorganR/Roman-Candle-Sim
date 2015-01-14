import java.util.ArrayList;

public class LaunchTube extends Emitter {

	private int numStars = 0;
	private String[] colour = {"blue", "green", "red", "yellow", "orange", "pink", "cyan", "purple"};
	
	public LaunchTube(double xPos, double yPos, double xVel, double yVel,
			double newExitV, double newAngle, double variation)
			throws EmitterException {
		super(xPos, yPos, xVel, yVel, newExitV, newAngle, variation);
		if (newAngle < -15.0 || newAngle > 15.0) 
			throw new EmitterException("Star launch angle must be between -15 and 15 degrees\nAngle: " + newAngle);
		if (variation < 0 || variation > 10)
			throw new EmitterException("Star launch angle variation must be between 0 and 10 degrees.\nVariation: " + variation);
	}
			
	
	public LaunchTube(double xPos, double yPos, double xVel, double yVel,
			double maxExitVelocity, Conditions current, double variation) {
		super(xPos, yPos, xVel, yVel, maxExitVelocity, current, variation);
	}


	public ArrayList<Star> launch(double time) {
		double angle = getRandomLaunchAngle();
		double[] position = getPosition();
		double[] velocity = getVelocity();
		//Velocity equals velocity of the emitter + the relative velocity of the spark (exit velocity)
		double vXInitial = velocity[X] + getExitVelocity()*Math.sin(angle);
		double vYInitial = velocity[Y] + getExitVelocity()*Math.cos(angle);
		ArrayList<Star> star = new ArrayList<>();
		//Check you still have stars to launch
		if (numStars != 8)
			star.add(new Star(time, position[0], position[1], vXInitial, vYInitial, colour[numStars]));
		//Add star
		numStars++;
		return star;
	}
}
