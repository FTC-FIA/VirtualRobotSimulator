package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.base.SimBotBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.Drive;
import org.firstinspires.ftc.teamcode.sim.SimStart;

@Autonomous(name = "Auton 3 w/Base", group = "BioBuzz")
public class AutonPinpointOnBase extends SimBotBaseOpMode {

    Drive drive;

    // Where to go, in field coordinates. The only numbers you change to go somewhere else.
    static final double START_X = -12;          // left side against the red wall
    static final double START_Y = -63;
    static final double START_HEADING = 180;    // facing -x, along the wall toward the zone

    static final double LEAVE_X = -12;          // move 1: straight out from the wall, no turning
    static final double LEAVE_Y = -48;
    static final double LEAVE_HEADING = 180;

    static final double TURN_X = -30;           // move 2: drive and turn at the same time
    static final double TURN_Y = -48;
    static final double TURN_HEADING = 0;      // end up facing +y, out into the field

    static final double PARK_X = -30;           // move 3: down into the loading zone
    static final double PARK_Y = -59;
    static final double PARK_HEADING = 90;

    // How we drive. One speed the whole way, same as the timed version.
    static final double DRIVE_POWER = 0.2;      // how hard to push, 0 to 1
    static final double TURN_POWER = 0.2;       // how hard to spin, 0 to 1
    static final double TOLERANCE = 0.5;        // inches; "close enough, stop"
    static final double HEADING_TOLERANCE = 3;  // degrees; "pointed close enough, stop turning"
    static final double TIMEOUT = 5.0;          // seconds; give up rather than hang forever

    @Override
    public void runOpMode() {

        config();

        drive = new Drive(
                frontLeft,
                frontRight,
                backLeft,
                backRight,
                pinpoint,
                telemetry
        );


        // SIMULATOR ONLY, both lines -- delete for the real robot. The sleep gives the physics a
        // moment to actually move the robot before we read any sensors.
        SimStart.setPose(START_X, START_Y, START_HEADING);
        sleep(100);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, START_X, START_Y,
                AngleUnit.DEGREES, START_HEADING));

        telemetry.addData("Ready", "Press START");
        drive.showPose();

        waitForStart();

        drive.driveTo(LEAVE_X, LEAVE_Y, LEAVE_HEADING);   // out from the wall -- scores LEAVE
        drive.driveTo(TURN_X, TURN_Y, TURN_HEADING);      // drive and turn together -- the thing to watch
        drive.driveTo(PARK_X, PARK_Y, PARK_HEADING);      // into the loading zone -- scores PARK

        drive.showPose();
        sleep(3000);
    }

}
