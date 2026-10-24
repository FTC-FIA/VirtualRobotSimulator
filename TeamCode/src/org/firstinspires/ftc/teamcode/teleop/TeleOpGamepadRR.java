package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.sim.SimStart;

/**
 * Basic TeleOp with Robot Relative Mecanum Drive
 */
@TeleOp(name = "TeleOp Gamepad RR", group = "BioBuzz")
public class TeleOpGamepadRR extends LinearOpMode {

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

        telemetry.addLine("Press START + A on your gamepad to connect it as gamepad1.");
        telemetry.addData("Ready", "Press START");
        telemetry.update();

        waitForStart();

        // Keep going until the driver presses STOP.
        while (opModeIsActive()) {
            double forward = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = -1.0f * gamepad1.right_stick_x;

            drive(forward, strafe, turn);
            telemetry.addData(
                    "Drive",
                    String.format(
                            "fwd: %.1f  strafe: %.1f  turn: %.1f",
                            forward,
                            strafe,
                            turn
                    )
            );
            telemetry.update();
        }
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
}
