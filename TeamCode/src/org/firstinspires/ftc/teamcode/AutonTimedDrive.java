package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.sim.SimStart;

/**
 * RED Autonomous: pull away from the wall, then drive into the LOADING ZONE.
 *
 * This scores two things in BIOBUZZ:
 *    LEAVE (3 points) -- at the end of AUTO the robot is not touching the perimeter wall
 *    PARK  (5 points) -- at the end of AUTO the robot is at least partly in the LOADING ZONE
 *
 * Both are checked at the END of the 30 seconds, so the robot has to finish off the wall
 * AND in the zone at the same time.
 *
 * The robot starts with its left side against the red wall, facing the loading zone.
 * Then it makes two moves:
 *    1. strafe right about 4 inches, straight out from the wall
 *    2. drive forward about 7 inches, until it is roughly 3 inches into the zone
 *
 * The moves are timed: turn the motors on, wait, turn them off. There are no encoders and
 * no odometry here. That means the distances are only as good as the timing, which is fine
 * for this -- there are several inches of room for error in both directions.
 */
@Autonomous(name = "Auton 1: Timed Drive", group = "BioBuzz")
public class AutonTimedDrive extends LinearOpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public void runOpMode() {

        // Find the four drive motors by the names in the robot configuration.
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");

        // The motors on the left side face the other way, so flip them.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        // SIMULATOR ONLY. Left side against the red wall, facing the loading zone.
        // Delete this line for the real robot.
        SimStart.setPose(-12, -63, 180);

        telemetry.addData("Ready", "Press START");
        telemetry.update();

        waitForStart();

        // Move 1: straight out from the wall, sideways. This is what scores LEAVE.
        strafeRight(0.2);
        sleep(350);
        stopDriving();
        sleep(250);

        // Move 2: forward into the loading zone. This is what scores PARK.
        driveForward(0.2);
        sleep(610);
        stopDriving();

        telemetry.addData("Done", "Off the wall and parked in the loading zone");
        telemetry.update();
        sleep(3000);
    }

    /** All four wheels forward. */
    private void driveForward(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    /** Slide sideways to the right without turning. This is what mecanum wheels are for. */
    private void strafeRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    /** Everybody stop. */
    private void stopDriving() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
