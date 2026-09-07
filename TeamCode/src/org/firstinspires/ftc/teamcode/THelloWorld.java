package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp()
public class THelloWorld extends OpMode {

    @Override
    public void init() {
        int teamNumber = 26859;
        telemetry.addData("Message", "Hello World");
    }

    @Override
    public void loop() {
        telemetry.addData("Message", "Hello World");
        telemetry.addData("Left Stick X", gamepad1.left_stick_x);
    }
}
