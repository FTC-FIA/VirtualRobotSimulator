package virtual_robot.games;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import virtual_robot.controller.Filters;
import virtual_robot.controller.Game;

/**
 * BIOBUZZ -- the 2026-2027 FIRST Tech Challenge game.
 *
 * Like Decode, this Game adds no movable game elements (no Nectar or Pollen yet). It only adds
 * stationary, infinite-mass bodies so the robot cannot drive through the permanent field
 * structures: the FRAME / HIVE assembly at the center of the field, and the four FLOWER
 * assemblies against the middle of each wall.
 *
 * COORDINATES
 * -----------
 * The dyn4j world used by the simulator has its origin at the CENTER of the field, with +x to
 * the right and +y UP, measured in METERS. Everything below is written in INCHES (which is how
 * the FTC field is specified) and converted with the IN constant, because inches are much easier
 * to reason about when you are comparing against the field drawings.
 *
 * The field is 144 x 144 inches, so every coordinate here is between -72 and +72.
 *
 * ACCURACY
 * --------
 * These rectangles were measured off the biobuzz648.bmp field image, not off the official FIRST
 * field drawings, so treat them as close-but-not-exact. If a shape feels wrong when you drive
 * into it, the numbers below are the only thing you need to change.
 */
public final class BioBuzz extends Game {

    /** Meters per inch, so the numbers below can be written in inches. */
    private static final double IN = 0.0254;

    @Override
    public final void initialize() {
        super.initialize();

        //          center x, center y,  width, height     (all inches)
        addObstacle(  -13.95,      0.0,   23.9,   51.0);   // RED HIVE  (left half of center frame)
        addObstacle(   13.80,      0.0,   22.8,   51.0);   // BLUE HIVE (right half of center frame)
        addObstacle(    0.20,      0.0,    8.0,   24.0);   // ramps bridging the two hives

        addObstacle(  -24.00,     68.7,    6.5,    6.5);   // FLOWER, top wall
        addObstacle(   68.50,     24.0,    6.5,    6.5);   // FLOWER, right wall
        addObstacle(   23.90,    -68.7,    6.5,    6.5);   // FLOWER, bottom wall
        addObstacle(  -68.80,    -23.9,    6.5,    6.5);   // FLOWER, left wall
    }

    /**
     * Add one immovable rectangular obstacle to the physics world.
     *
     * @param xInches      center of the rectangle, inches right of field center
     * @param yInches      center of the rectangle, inches above field center
     * @param widthInches  size along x
     * @param heightInches size along y
     */
    private void addObstacle(double xInches, double yInches, double widthInches, double heightInches) {
        Rectangle rect = new Rectangle(widthInches * IN, heightInches * IN);
        rect.translate(xInches * IN, yInches * IN);

        Body body = new Body();
        BodyFixture fixture = body.addFixture(rect);
        fixture.setFilter(Filters.WALL_FILTER);   // collides with everything, like the field walls
        body.setMass(MassType.INFINITE);          // never moves, no matter how hard it is hit
        world.addBody(body);
    }

    @Override
    public final void resetGameElements() { }

    @Override
    public boolean hasHumanPlayer() { return false; }

    @Override
    public final boolean isHumanPlayerAuto() { return false; }

    @Override
    public final void setHumanPlayerAuto(boolean selected) { }

    @Override
    public final void updateHumanPlayerState(double millis) { }

    @Override
    public final void requestHumanPlayerAction() { }

    @Override
    public final boolean isHumanPlayerActionRequested() { return false; }

    @Override
    public final void stopGameElements() { }
}
