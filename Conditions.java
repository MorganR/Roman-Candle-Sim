
public class Conditions {
	private double angle;
	private int wind;
	
	public Conditions() {
		setAngle(0);
		setWind(0);
	}
	
	public void setAngle(double num) {
		angle = Math.toRadians(num);
	}
	
	public void setWind(int num) {
		wind = num;
	}
	
	public double getAngle() { return angle; }
	
	public int getWind() { return wind; }
	
}
