package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.sim.SimStart;

/**
 * Demo for various Gamepad control modes
 *
 * See Gamepad docs at
 * https://javadoc.io/doc/org.firstinspires.ftc/RobotCore/latest/com/qualcomm/robotcore/hardware/Gamepad.html
 */
@TeleOp(name = "Gamepad Demo", group = "BioBuzz")
public class TeleOpGamepadDemo extends LinearOpMode {

    private Gamepad previous1 = new Gamepad();

    private final float fastModeMutiplier = 1.0f;
    private final float slowModeMutiplier = 0.5f;

    public void runOpMode() {

        telemetry.addLine("Press START + A on your gamepad to connect it as gamepad1.");
        telemetry.addData("Ready", "Press START");
        telemetry.update();

        waitForStart();

        float continuousControl = 0.0f;
        boolean onOffControl = false;
        boolean toggleControl = false;

        float modifiedControl = 0.0f;
        boolean modifier = false;

        // Keep going until the driver presses STOP.
        while (opModeIsActive()) {

            // continuous control
            continuousControl = gamepad1.left_stick_y;
            telemetry.addData("Continuous Control", String.format("%.2f", continuousControl));

            // on/off control
            onOffControl = gamepad1.left_bumper;
            telemetry.addData("On/Off Control", onOffControl);

            // toggle control
            if (gamepad1.rightBumperWasPressed()) {
                toggleControl = !toggleControl;
            }
            telemetry.addData("Toggle Control", toggleControl);

            // modifier control
            modifier = gamepad1.a;
            if (modifier) {
                modifiedControl = continuousControl * slowModeMutiplier;
            } else {
                modifiedControl = continuousControl * fastModeMutiplier;
            }
            telemetry.addData("Modifier activated", modifier);
            telemetry.addData("Modified Control", String.format("%.2f", modifiedControl));

            // print to screen
            telemetry.update();
        }
    }
}
