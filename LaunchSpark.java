
public class LaunchSpark extends Spark {

	public LaunchSpark(double creationTimeIn, double xPosIn, double yPosIn,
			double xVelIn, double yVelIn, double lifetime, String colourIn) {
		super(creationTimeIn, xPosIn, yPosIn, xVelIn, yVelIn, lifetime, colourIn);
		//Alter radius for launch sparks
		setStartingRadius(0.0005);
	}
	
	@Override
	public String toString() {
		String s = super.toString("Launch");
		return s;
	}
}
