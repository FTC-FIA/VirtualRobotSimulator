package org.firstinspires.ftc.teamcode.base;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public abstract class SimBotBaseOpMode extends LinearOpMode {

    protected DcMotor frontLeft, frontRight, backLeft, backRight, intake;
    protected GoBildaPinpointDriver pinpoint;

    /**
     * Configures the hardware that all OpModes using this robot share.
     * Call this first thing on startup.
     */
    public void config() {
        // Instantiate hardware
        frontLeft = hardwareMap.dcMotor.get("front_left_motor");
        frontRight = hardwareMap.dcMotor.get("front_right_motor");
        backLeft = hardwareMap.dcMotor.get("back_left_motor");
        backRight = hardwareMap.dcMotor.get("back_right_motor");
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // Configure hardware
        frontLeft.setDirection(DcMotor.Direction.REVERSE);   // left motors face the other way
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontRight.setZeroPowerBehavior(BRAKE);
        frontLeft.setZeroPowerBehavior(BRAKE);
        backRight.setZeroPowerBehavior(BRAKE);
        backLeft.setZeroPowerBehavior(BRAKE);

        pinpoint.setOffsets(-7.375, -5.875, DistanceUnit.INCH);
    }

}
