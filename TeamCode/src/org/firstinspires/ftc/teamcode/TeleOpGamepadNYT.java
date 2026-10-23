package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.sim.SimStart;

/**
 * Basic TeleOp with FIELD-RELATIVE Mecanum Drive.
 *
 * The left stick says which way to go ON THE FIELD, as seen by a driver standing behind the red
 * wall -- no matter which way the robot is pointed:
 *   stick up    -> away from the driver (+y, toward the blue wall)
 *   stick right -> to the driver's right (+x)
 * The right stick still spins the robot.
 *
 * To do that we need the robot's heading, which comes from the Pinpoint. Each loop we rotate the
 * field direction from the stick into the robot's own forward/right, exactly like
 * driveFieldRelative() in AutonPinpoint.
 */
@TeleOp(name = "TeleOp Gamepad NYT", group = "BioBuzz")
public class TeleOpGamepadNYT extends LinearOpMode {

    // Where the robot starts, in field coordinates. The Pinpoint needs this to know its heading.
    static final double START_X = -12;          // left side against the red wall
    static final double START_Y = -63;
    static final double START_HEADING = 180;    // facing -x, along the wall toward the zone
    boolean toggleControl = false;

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    GoBildaPinpointDriver pinpoint;

    public void runOpMode() {

        // Find the four drive motors by the names in the robot configuration.
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");

        // The motors on the left side face the other way, so flip them.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        // SIMULATOR ONLY, both lines -- delete for the real robot. The sleep gives the physics a
        // moment to actually move the robot before we read any sensors.
        SimStart.setPose(START_X, START_Y, START_HEADING);
        sleep(100);

        // Tell the Pinpoint where (and which way) the robot is starting, so its heading is a
        // field heading.
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, START_X, START_Y,
                AngleUnit.DEGREES, START_HEADING));

        telemetry.addLine("Press START + A on your gamepad to connect it as gamepad1.");
        telemetry.addData("Ready", "Press START");
        telemetry.update();

        waitForStart();

        // Keep going until the driver presses STOP.
        while (opModeIsActive()) {
            pinpoint.update();                      // nothing updates until you call this
            double heading = pinpoint.getPosition().getHeading(AngleUnit.DEGREES);

            // Stick in FIELD terms: up is +y, right is +x.
            double fieldX = gamepad1.left_stick_x;
            double fieldY = gamepad1.left_stick_y;
            double turn = -1.0f * gamepad1.right_stick_x;

            driveFieldRelative(fieldX, fieldY, heading, turn);

            telemetry.addData("Stick", "field x: %.1f  field y: %.1f  turn: %.1f", fieldX, fieldY, turn);
            showPose();
            telemetry.addData("SlowMode", "off");
            telemetry.update();
        }
    }

    /**
     * Drive in a direction measured on the FIELD, whichever way the robot happens to be pointed.
     * Rotating the field vector by the robot's heading turns it into the robot's own forward/right.
     * Unlike AutonPinpoint we do not divide by the length: how far the stick is pushed is the speed.
     *
     * @param fieldX  +1 is toward +x on the field (driver's right)
     * @param fieldY  +1 is toward +y on the field (away from the red wall)
     * @param heading robot's current heading, degrees
     * @param turn    spin power, + is counterclockwise
     */
    private void driveFieldRelative(double fieldX, double fieldY, double heading, double turn) {
        double h = Math.toRadians(heading);

        double forward = fieldX * Math.cos(h) + fieldY * Math.sin(h);
        double right = fieldX * Math.sin(h) - fieldY * Math.cos(h);

        drive(forward, right, turn);
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

    /** Put the robot's current field position on the driver station. */
    private void showPose() {
        pinpoint.update();
        Pose2D pose = pinpoint.getPosition();
        telemetry.addData("Pose", "x: %.1f  y: %.1f  heading: %.1f",
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.DEGREES));

                if (gamepad1.rightBumperWasPressed()) {
                    toggleControl = !toggleControl;
                    telemetry.addData("SlowMode", "on");
                    telemetry.update();

                }
    }
}
