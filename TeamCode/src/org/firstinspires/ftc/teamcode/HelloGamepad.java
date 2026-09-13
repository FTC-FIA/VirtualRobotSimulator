package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp()
public class HelloGamepad extends OpMode {

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
