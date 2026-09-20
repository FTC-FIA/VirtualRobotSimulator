package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.sim.SimStart;

/**
 * RED Autonomous, version 3: the same two moves as AutonPinpoint, but the robot eases off the gas
 * as it gets close instead of running flat out and slamming on the brakes.
 *
 * AutonPinpoint knows exactly where it is and still overshoots, because it drives at one fixed
 * power right up to the instant it decides to stop, and then has to coast.
 *
 * PROPORTIONAL CONTROL -- make the power depend on how far away you are:
 *
 *     power = distance * POSITION_GAIN
 *
 * Far away, push hard; nearly there, barely push. The error shrinks, the power shrinks with it, and
 * the robot settles onto the target. MAX_POWER stops it launching itself from across the field;
 * MIN_POWER stops it stalling an inch short, where friction beats a tiny motor command.
 *
 * WHAT CHANGED FROM AutonPinpoint -- two lines in driveTo():
 *   1. speed is proportional to distance instead of a fixed DRIVE_POWER
 *   2. the robot holds its heading, with turn power proportional to how far it has drifted
 * Everything else is identical. Put the two files side by side and diff them.
 *
 * It should land within TOLERANCE of (-19, -59) instead of past it. POSITION_GAIN is the knob: too
 * small and it is still crawling when the timeout fires, too large and you are overshooting again.
 *
 * FIELD COORDINATES (official FTC, drawn from the red alliance's side of the field)
 *   origin at field center; x and y each run -72 to +72 inches
 *   +x to your right, +y away from the red wall -- so red wall is y = -72, blue wall is y = +72
 *   heading is degrees counterclockwise from +x:  0 right, 90 up, 180 left, -90 down
 *
 * THE PLAN (unchanged)
 *   start at (-12, -63) facing 180, left side against the red wall
 *   move 1: go to (-12, -59)  -- 4 inches off the wall, scores LEAVE
 *   move 2: go to (-19, -59)  -- 7 inches into the loading zone, scores PARK
 */
@Autonomous(name = "Auton 3: Pinpoint + Proportional", group = "BioBuzz")
public class AutonPinpointProportional extends LinearOpMode {

    // Where to go, in field coordinates. The only numbers you change to go somewhere else.
    static final double START_X = -12;          // left side against the red wall
    static final double START_Y = -63;
    static final double START_HEADING = 180;    // facing -x, along the wall toward the zone

    static final double LEAVE_X = -12;          // move 1: off the wall
    static final double LEAVE_Y = -59;

    static final double PARK_X = -19;           // move 2: into the loading zone
    static final double PARK_Y = -59;

    // How hard to push. Turn these down if it overshoots, up if it never arrives.
    static final double POSITION_GAIN = 0.030;  // motor power per inch of error
    static final double HEADING_GAIN = 0.015;   // motor power per degree of heading error
    static final double MAX_POWER = 0.35;       // never push harder than this
    static final double MIN_POWER = 0.10;       // ...or softer, or friction wins and it just sits there
    static final double TOLERANCE = 0.5;        // inches; "close enough, stop"
    static final double TIMEOUT = 5.0;          // seconds; give up rather than hang forever

    DcMotor frontLeft, frontRight, backLeft, backRight;
    GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);   // left motors face the other way
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        // SIMULATOR ONLY, both lines -- delete for the real robot. The sleep gives the physics a
        // moment to actually move the robot before we read any sensors.
        SimStart.setPose(START_X, START_Y, START_HEADING);
        sleep(100);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // On a real robot, also call setOffsets(), setEncoderResolution() and setEncoderDirections()
        // here to describe how the odometry pods are mounted. The simulator does not need them.

        // The important line. The Pinpoint only knows how far it has rolled since you last told it
        // something, so tell it where it is starting. Now every pose it reports is a field coordinate.
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, START_X, START_Y,
                AngleUnit.DEGREES, START_HEADING));

        telemetry.addData("Ready", "Press START");
        showPose();

        waitForStart();

        driveTo(LEAVE_X, LEAVE_Y);   // scores LEAVE
        driveTo(PARK_X, PARK_Y);     // scores PARK

        telemetry.addData("Done", "Off the wall and parked in the loading zone");
        showPose();
        sleep(3000);
    }

    /**
     * Drive to a spot on the field and stop there, holding START_HEADING the whole way.
     * The further away, the harder it pushes; as the error shrinks, so does the power.
     */
    private void driveTo(double targetX, double targetY) {

        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive() && timer.seconds() < TIMEOUT) {

            pinpoint.update();                      // nothing updates until you call this
            Pose2D pose = pinpoint.getPosition();
            double x = pose.getX(DistanceUnit.INCH);
            double y = pose.getY(DistanceUnit.INCH);
            double heading = pose.getHeading(AngleUnit.DEGREES);

            double errorX = targetX - x;            // how far off we are, in FIELD coordinates
            double errorY = targetY - y;
            double distance = Math.hypot(errorX, errorY);

            showPose();
            telemetry.addData("Target", "x: %.1f  y: %.1f   (%.1f in away)", targetX, targetY, distance);
            telemetry.update();

            if (distance < TOLERANCE) break;

            // CHANGE 1: speed proportional to distance, clamped at both ends.
            double power = Math.min(MAX_POWER, Math.max(MIN_POWER, distance * POSITION_GAIN));

            // CHANGE 2: hold our heading, with turn power proportional to the drift in degrees.
            double headingError = AngleUnit.normalizeDegrees(START_HEADING - heading);
            double turn = headingError * HEADING_GAIN;

            driveFieldRelative(errorX, errorY, heading, power, turn);
        }

        stopDriving();
        sleep(250);   // let the robot settle before the next move reads the odometry
    }

    /**
     * Drive toward a direction measured on the FIELD, whichever way the robot happens to be pointed.
     *
     * The motors only understand "forward" and "right," and those swing around as the robot turns.
     * Rotating the field vector by the robot's heading converts one into the other -- it is a plain
     * 2-D rotation. Dividing by the length throws away the distance and keeps only the direction;
     * power decides the speed.
     *
     * @param fieldX  x part of the direction to travel, in field coordinates
     * @param fieldY  y part of the direction to travel, in field coordinates
     * @param heading robot's current heading, degrees
     * @param power   how hard to push, 0 to 1
     * @param turn    spin power, + is counterclockwise
     */
    private void driveFieldRelative(double fieldX, double fieldY, double heading, double power, double turn) {
        double h = Math.toRadians(heading);
        double length = Math.hypot(fieldX, fieldY);

        double forward = (fieldX * Math.cos(h) + fieldY * Math.sin(h)) / length;
        double right = (fieldX * Math.sin(h) - fieldY * Math.cos(h)) / length;

        drive(forward * power, right * power, turn);
    }

    /**
     * Mecanum mixing: turn "go this way and spin this much" into four motor powers.
     *
     * @param forward +1 is straight out the front of the robot
     * @param right   +1 is sideways to the robot's right -- what mecanum wheels are for
     * @param turn    +1 is counterclockwise, the direction heading increases
     */
    private void drive(double forward, double right, double turn) {
        double fl = forward + right - turn;
        double bl = forward - right - turn;
        double fr = forward - right + turn;
        double br = forward + right + turn;

        // Scale all four down together if any exceeds 1, so the direction is kept, just slower.
        double max = Math.max(1.0, Math.max(Math.abs(fl), Math.max(Math.abs(bl),
                Math.max(Math.abs(fr), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        backLeft.setPower(bl / max);
        frontRight.setPower(fr / max);
        backRight.setPower(br / max);
    }

    /** Everybody stop. */
    private void stopDriving() {
        drive(0, 0, 0);
    }

    /** Put the robot's current field position on the driver station. */
    private void showPose() {
        pinpoint.update();
        Pose2D pose = pinpoint.getPosition();
        telemetry.addData("Pose", "x: %.1f  y: %.1f  heading: %.1f",
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.DEGREES));
    }
}
