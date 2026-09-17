package org.firstinspires.ftc.teamcode;

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
 * COORDINATES (standard FTC convention)
 *   - origin is the CENTER of the field
 *   - +x is to the right, +y is up, both in INCHES
 *   - each runs from -72 to +72, because the field is 144 inches across
 *   - heading is in DEGREES, counterclockwise, with 0 meaning "facing +y" (toward the
 *     back wall, away from the audience). So 90 faces left (-x), 180 faces the audience
 *     (-y), and 270 faces right (+x), toward the blue wall.
 *
 * Some landmarks on the BIOBUZZ field:
 *   ( 0,   0)  center of the field, inside the hive structure -- do not start here
 *   (-60, -60) red garden corner
 *   ( 60,  60) blue garden corner
 *   (-72,   y) hard against the left wall
 */
public class SimStart {

    /** Utility class; never instantiated. */
    private SimStart() { }

    /**
     * Put the robot at this exact spot before the OpMode runs.
     *
     * @param xInches        inches right of field center, -72 to +72
     * @param yInches        inches above field center, -72 to +72
     * @param headingDegrees degrees counterclockwise, 0 = facing +y
     */
    public static void setPose(double xInches, double yInches, double headingDegrees) {
        VirtualRobotController controller = OpMode.getVirtualRobotController();
        if (controller == null) return;

        VirtualBot bot = controller.getBot();
        if (bot == null) return;

        bot.setPosition(xInches, yInches, headingDegrees);
    }
}
