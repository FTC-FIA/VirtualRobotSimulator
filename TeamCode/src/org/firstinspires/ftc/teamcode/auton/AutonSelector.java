package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.base.SimBotBaseOpMode;
import org.firstinspires.ftc.teamcode.robot.Drive;
import org.firstinspires.ftc.teamcode.sim.SimStart;

@Disabled
@Autonomous(name = "Auton 3 w/Base", group = "BioBuzz")
public class AutonSelector extends SimBotBaseOpMode {

    // lay out all options here
    private final int RED = 0;
    private final int BLUE = 1;
    private int alliance = RED;
    private boolean allianceSelected = false;

    private final int SHOOT_FIRST = 0;
    private final int SHOOT_SECOND = 1;
    private int shootOrder = SHOOT_FIRST;
    private boolean shootOrderSelected = false;

    private final int PARK_LEFT = 0;
    private final int PARK_RIGHT = 1;
    private int park = PARK_LEFT;
    private boolean parkSelected = false;

    // coordinates of interest
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

        // init loop
        while (!isStarted()) {
            selectMenu();
        }

        waitForStart();



        drive.driveTo(LEAVE_X, LEAVE_Y, LEAVE_HEADING);   // out from the wall -- scores LEAVE
        drive.driveTo(TURN_X, TURN_Y, TURN_HEADING);      // drive and turn together -- the thing to watch
        drive.driveTo(PARK_X, PARK_Y, PARK_HEADING);      // into the loading zone -- scores PARK

        drive.showPose();
        sleep(3000);
    }

    private void selectMenu() {
        if (!allianceSelected) {
            telemetry.addData("Select Alliance", "Red = B | Blue = X");
            if (gamepad1.bWasPressed()) {
                alliance = RED;
                allianceSelected = true;
            } else if (gamepad1.xWasPressed()) {
                alliance = BLUE;
                allianceSelected = true;
            }
        } else if (!shootOrderSelected) {
            telemetry.addData("Select Shoot Order", "First = B | Second = X");
            if (gamepad1.bWasPressed()) {
                shootOrder = SHOOT_FIRST;
                shootOrderSelected = true;
            } else if (gamepad1.xWasPressed()) {
                shootOrder = SHOOT_SECOND;
                shootOrderSelected = true;
            }
        } else if (!parkSelected) {
            telemetry.addData("Select Park", "Left = B | Right = X");
            if (gamepad1.bWasPressed()) {
                park = PARK_LEFT;
                parkSelected = true;
            } else if (gamepad1.xWasPressed()) {
                park = PARK_RIGHT;
                parkSelected = true;
            }
        } else { // all selections done!
            telemetry.addData("All done!", "Press START when ready");
        }
    }
}
