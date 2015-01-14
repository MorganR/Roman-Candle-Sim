/**
 * The Earth class establishes initial conditions for the launch environment,
 * as well as holds the value of the constants air density, gravity, and delta t
 * for the coupled projectile (in this case, a Star). This avoids them being 
 * re-initialized with every Star object created. 
 * 
 * @author Morgan Roff, ID: 10044329, NetID: 11mr60
 * @version 1.0
 */
public class Environment {
	
	//Air density in kg/m^3
	public final static double AIR_DENSITY = 1.2;
	//Acceleration due to gravity in m/s^2
	public final static double G = 9.807;
	
	private Conditions current;
	
	/**
	 * Earth is the constructor for the Earth class. This requires two user inputs, 
	 * and establishes all other initial condition variables on its own. 
	 * @param windIn The user-input for wind velocity (km/h)
	 * @param angleIn The user-input for launch angle (degrees)
	 * @throws InitialConditionException An exception limiting the allowable values of windIn and angleIn
	 */
	public Environment(Conditions current) throws EnvironmentException {
		if (current.getWind() > 20 || current.getWind() < -20)
			throw new EnvironmentException("Initial wind velocity of " + current.getWind() + "km/h is outside the permissable range.");
		//Have to convert km/h into m/s, so divide by 3.6 after receiving input
		this.current = current;
		
	}
	
	/**
	 * Return a double of the wind velocity
	 * @return wind The wind velocity (m/s)
	 */
	public double getWind() { return current.getWind()/3.6; }
}
