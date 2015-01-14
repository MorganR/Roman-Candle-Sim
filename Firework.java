
public class Firework {
	//Position and velocity array indices
	//Public so all classes can use them for access
	public final static int X = 0;
	public final static int Y = 1;
	
	private double[] position = new double[2];
	private double[] velocity = new double[2];
	
	//Set the position and velocity values
	public Firework(double xPos, double yPos, double xVel, double yVel) {
		position[X] = xPos;
		position[Y] = yPos;
		velocity[X] = xVel;
		velocity[Y] = yVel;
	}
	
	public double[] getPosition() {
		return position.clone();
	}
	
	public double[] getVelocity() {
		return velocity.clone();
	}
	
	public void setPosition(double[] newPosition) {
		position = newPosition.clone();
	}
	
	public void setVelocity(double[] newVelocity) {
		velocity = newVelocity.clone();
	}
	
	public final double getRand(double min, double max) {
		double v = min + (double)(Math.random() * (max-min));
		return v;
	}
	
	//Return the position values of the firework object, rounded to 4 decimals
	@Override
	public String toString() {
		String s = "" + (double)Math.round(10000*position[X])/10000 + "\t" + (double)Math.round(10000*position[Y])/10000.0;
		return s;
	}
}
