package org.firstinspires.ftc.teamcode.sim;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import virtual_robot.controller.VirtualBot;
import virtual_robot.controller.VirtualRobotController;

/**
 * SIMULATOR ONLY -- places the robot at an exact starting pose.
 *
 * Normally you set the robot's starting position by left-clicking the field (to move it) and
 * right-clicking (to aim it) before pressing INIT. That is fine for driving practice, but it is
 * useless for testing an Autonomous, because you can never click the same spot twice. If the
 * robot ends up somewhere different every run, you cannot tell whether your code got better or
 * whether you just clicked two inches to the left.
 *
 * Call SimStart.setPose(...) as the FIRST line of runOpMode() and every run starts identically.
 *
 * >>> THIS CLASS DOES NOT EXIST ON A REAL ROBOT. <<<
 * When you copy an OpMode over to the real Control Hub, delete the SimStart line. On a real
 * field you set the starting position by physically placing the robot on the tiles.
 *
 * COORDINATES (official FTC field coordinate system)
 *   The official system is drawn from the RED alliance's point of view: stand outside the
 *   field, at the center of the RED WALL, and look toward the middle of the field.
 *   - origin is the CENTER of the field
 *   - +x runs to your right, +y runs away from you toward the BLUE WALL, both in INCHES
 *   - each runs from -72 to +72, because the field is 144 inches across
 *   - so the RED WALL is the bottom edge (y = -72) and the BLUE WALL is the top edge (y = +72)
 *   - heading is in DEGREES counterclockwise, measured from the +x axis:
 *          0 faces +x (right)
 *         90 faces +y (toward the blue wall)
 *        180 faces -x (left)
 *        -90 faces -y (toward the red wall)
 *
 *   FIRST's definition pins down the axes but never says what heading ZERO is. The 0 = +x
 *   convention above is what the FTC path libraries use (Pedro Pathing and Road Runner both
 *   put heading 0 along +x) and what the Pinpoint reports, so it is what we use everywhere.
 *
 * This is the same convention the Pinpoint reports, so a pose you hand to setPose() and a pose
 * you read back from pinpoint.getPosition() mean the same thing. The simulator's own insides
 * measure heading from +y instead, but that is its business -- setPose() converts for you, and
 * nothing above this line needs to know.
 *
 * Some landmarks on the BIOBUZZ field:
 *   (  0,   0) center of the field, inside the hive structure -- do not start here
 *   (  x, -72) hard against the red wall (the bottom edge)
 *   (  x,  72) hard against the blue wall (the top edge)
 *   ( 60, -60) red garden corner
 *   (-60,  60) blue garden corner
 */
public class SimStart {

    /** Utility class; never instantiated. */
    private SimStart() { }

    /**
     * Put the robot at this exact spot before the OpMode runs.
     *
     * @param xInches        inches right of field center, -72 to +72
     * @param yInches        inches above field center, -72 to +72
     * @param headingDegrees degrees counterclockwise from the +x axis, 0 = facing +x
     */
    public static void setPose(double xInches, double yInches, double headingDegrees) {
        VirtualRobotController controller = OpMode.getVirtualRobotController();
        if (controller == null) return;

        VirtualBot bot = controller.getBot();
        if (bot == null) return;

        // The simulator measures heading from +y; FTC measures it from +x. Ninety degrees apart.
        bot.setPosition(xInches, yInches, headingDegrees - 90);
    }
}
