import java.util.ArrayList;

/**
 * This class manages the simulation.  It draws the Roman candle and launches 8 stars of various colours.
 * The class also manages all the other particle effects: the sparks emitted by the star, the 
 * launch sparks and the delay charge sparks.
 * @author Alan Mcleod (modified by Morgan Roff)
 * @version 1.2
 */
public class ParticleManager {

	private double deltaTime;		// seconds
	// Stores the time of the last star launch
	private double lastTime;		// seconds
	private Environment env;
	private LaunchTube tube;
	private StarSparkEmitter starSparkEmit;
	private LaunchSparkEmitter launchSparkEmit;
	private DelaySparkEmitter delaySparkEmit;
	private BigSparkEmitter bigSparkEmit;
	// This ArrayList will hold all the generated particles.
	private ArrayList<Particle> fireworks = new ArrayList<>();
	private int numStars = 8;
	private int countStars = 0;
	private double starLaunchTime;
	private double maxExitVelocity;
	private boolean launchFlag = false;		// Optional
	private boolean bigBang = false;

	/**
	 * The ParticleManager constructor
	 * @param current The current tube angle and wind velocity
	 * @throws EnvironmentException If the wind velocity is not between -20 and 20 m/sec.
	 * @throws EmitterException If the launch angle is not between -15 and 15 degrees.
	 */
	public ParticleManager(Conditions current) throws EnvironmentException, EmitterException {
		env = new Environment(current);
		// Position the star emitter at the end of the roman candle.  Use a launch velocity of 22 m/sec
		// and add a 2 degree random variation to the launch angle.
		maxExitVelocity = 22;
		tube = new LaunchTube(0.0, 0.0 , 0.0, 0.0, maxExitVelocity, current, 2.0);
		lastTime = System.currentTimeMillis()/1000.0;
	}

	/**
	 * Launches a single star at the supplied absolute time and adds one set of launch sparks.
	 * @param time The system time in seconds.  The first star will be launched at time=0.
	 * @param current The current tube angle and wind velocity.
	 */
	public void start(double time, Conditions current) {
		// Add some variation to the star's exit velocity
		tube.setExitVelocity(maxExitVelocity - 2 * Math.random());
		// Launch the star
		ArrayList<Star> starSet = tube.launch(time);
		if (starSet.size() == 0)
			return;
		Star singleStar = starSet.get(0);
		launchFlag = true;
		starLaunchTime = time;
		// Add the star to the particles collection.
		fireworks.add(singleStar);
		// Create the spark emitters using the initial position and velocity of the star.
		double[] position = singleStar.getPosition();
		double[] velocity = singleStar.getVelocity();
		try {
			// Star sparks of the same colour as the star will be launched at 3 m/sec in all directions.
			starSparkEmit = new StarSparkEmitter(position[0], position[1], velocity[0], velocity[1], 3, 0, 180);
			starSparkEmit.setColour(singleStar.getColour());
			if(countStars == 7) {
				bigSparkEmit = new BigSparkEmitter(position[0], position[1], velocity[0], velocity[1], 15, 0, 180);
				bigSparkEmit.setColour(singleStar.getColour());
			}
			// Launch sparks will be launched at 20 m/sec within 3 degrees of the star's launch angle.
			launchSparkEmit = new LaunchSparkEmitter(position[0], position[1], 0, 0, 20, current, 3);
			// Delay charge sparks will be sprayed out at 2.2 m/sec.
			delaySparkEmit = new DelaySparkEmitter(position[0], position[1], 0, 0, 2.2, current, 45);
		} catch (EmitterException e) {
			// Not likely to get here unless the angles are not legal.
			System.out.println(e.getMessage());
			return;
		}
		// Add launch sparks to "push" the star out.
		fireworks.addAll(launchSparkEmit.launch(time));		
	} // end start method

	/**
	 * This method updates the simulation.
	 * @param time The absolute time in seconds. The simulation was started at time = 0;
	 * @param current The current tube angle and wind velocity.
	 */
	private void update(double time, Conditions current) {
		deltaTime = time - lastTime;
		lastTime = time;
		int index = 0;
		Particle firework;
		// Clean out dead fireworks
		do {
			firework = fireworks.get(index);
			if (time - firework.getCreationTime() >= firework.getLifetime() || firework == null) {
				// Get rid of the star spark emitter if the star is gone.
				if (firework instanceof Star) {
					starSparkEmit = null;
					bigSparkEmit = null;
					bigBang = false;
				}
				fireworks.remove(index);
			} else
				index++;
		} while (fireworks.size() > 0 && index < fireworks.size());
		// Update positions
		for (Particle fire : fireworks) {
			fire.updatePosition(time, deltaTime, env);
			// Move the star spark emitter along with the star.
			if (fire instanceof Star) {
				starSparkEmit.setPosition(fire.getPosition());
				starSparkEmit.setVelocity(fire.getVelocity());
				if (countStars == 7 && fire.getLifetime() - time + fire.getCreationTime() < 0.1) {
					bigSparkEmit.setPosition(fire.getPosition());
					bigSparkEmit.setVelocity(fire.getVelocity());
					bigBang = true;
				}
			}
		}
		// Keep adding delay charge sparks until 3.5 seconds are up.
		if (time - starLaunchTime < 3.5) {
			fireworks.addAll(delaySparkEmit.launch(time));
		}
		// Add star sparks as long as the starSpar emitter exists
		if (starSparkEmit != null)
			fireworks.addAll(starSparkEmit.launch(time));
		if (bigBang)
			fireworks.addAll(bigSparkEmit.launch(time));
		// If all the particles associated with the previous star are all gone, then prevent the particle 
		// collection from becoming empty by adding delay charge sparks, and then start the launch
		// of another star.
		if (fireworks.size() == 0) {
			if (countStars < numStars - 1) {
				fireworks.addAll(delaySparkEmit.launch(time));
				countStars++;
				// Launch another star
				start(time, current);
			} else {
				// Stop the simulation after 8 stars have been launched.  The collection will be empty.
				return;
			}
		}		
	} // end update

	/**
	 * An accessor for the collection of particles.
	 * For this to work properly, each particle type should have have its own clone method.  (But they
	 * do not, which is OK...)
	 * @param time The absolute time in seconds. The simulation started at time = 0.
	 * @return The collection of particles in an ArrayList.
	 */
	public ArrayList<Particle> getFireworks(double time, Conditions current) {
		update(time, current);
		ArrayList<Particle> copy = new ArrayList<>(fireworks.size());
		for (Particle firework : fireworks)
			copy.add(firework);
		return copy;
	}

	/**
	 * Returns a flag used to indicate when a Star has been launched. Intended for use
	 * with resetLaunchFlag() which resets the flag back to false.
	 * @return A flag that is true if a Star has just been launched.
	 */
	public boolean getLaunchFlag() { return launchFlag; }

	/**
	 * Resets a flag that is used to track the launch of a Star once the launch has
	 * been detected.
	 */
	public void resetLaunchFlag() { launchFlag = false; }

	/**
	 * Displays a count of all the particle types in the supplied collection at the
	 * given time.
	 * @param fireworks An ArrayList<Particle> collection.
	 * @param time The time in seconds.
	 */
	public static void showTypesCount(ArrayList<Particle> fireworks, double time) {
		int starCount = 0;
		int sparkCount = 0;
		int launchSparkCount = 0;
		for (Particle firework : fireworks) {
			if (firework instanceof Star)
				starCount++;
			else if (firework instanceof LaunchSpark)
				launchSparkCount++;
			else
				sparkCount++;
		}
		System.out.printf("%5.2f\t", time);
		System.out.println(starCount + "\t" + sparkCount + "\t" + launchSparkCount);
	} // end showTypesCount

	/**
	 * Prints out each firework in the supplied collection and its position at the given time.
	 * @param fireworks A snapshot of the fireworks collection.
	 * @param time The time the snapshot was taken in seconds.
	 */
	public static void showFireworks(ArrayList<Particle> fireworks, double time) {
		if (fireworks == null)
			return;
		System.out.printf("\nAt time%5.2f seconds:\n", time);
		System.out.println("Type\t\tPosition (metres)");
		for (Particle firework : fireworks)
			System.out.println(firework);
	}

} // end ParticleManager class
