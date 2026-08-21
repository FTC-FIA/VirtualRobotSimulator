package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

/**
 * Starter Auton using Mecanum and Pinpoint
 */
@Autonomous(name = "Auton Playground", group = "Auton Demo")
public class AutonPlayground extends LinearOpMode {

    DcMotor backLeft = null;
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backRight = null;

    public void runOpMode(){
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,
                "pinpoint");
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.RADIANS, 0));

        telemetry.addData("Press Start When Ready","");
        telemetry.update();

        waitForStart();

        double fwdPower = 0.5;
        double strafePower = 0.5;
        double rotPower = 0.5;
        Pose2D pose = null;

        pinpoint.update();
        pose = pinpoint.getPosition();
        telemetry.addData(
                "Pinpoint Pos", "x: %.1f  y: %.1f  h: %.1f",
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.DEGREES)
        );
        telemetry.update();

        drive(fwdPower, strafePower, rotPower);
        sleep(1000);
        drive(0, 0, 0);

        pinpoint.update();
        pose = pinpoint.getPosition();
        telemetry.addData(
                "Pinpoint Pos", "x: %.1f  y: %.1f  h: %.1f",
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.DEGREES)
        );
        telemetry.update();
        sleep(1000);

    }

    private void drive(double fwd, double strafe, double rot) {
        if (Math.abs(rot) < 0.05) rot = 0;
        double blPower = -fwd + strafe - rot;
        double flPower = fwd + strafe + -rot;
        double frPower = -fwd + strafe + rot;
        double brPower = fwd + strafe + rot;
        double max = Math.max(1.0, Math.abs(blPower));
        max = Math.max(max, Math.abs(flPower));
        max = Math.max(max, Math.abs(frPower));
        max = Math.max(max, Math.abs(brPower));
        blPower /= max;
        flPower /= max;
        frPower /= max;
        brPower /= max;
        backLeft.setPower(blPower);
        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backRight.setPower(brPower);
    }
}
