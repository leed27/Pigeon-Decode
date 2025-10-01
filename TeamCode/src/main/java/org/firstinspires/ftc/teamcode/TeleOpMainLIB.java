package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "YES")
public class TeleOpMainLIB extends CommandOpMode {
    public GamepadEx driver;

    public ElapsedTime gameTimer;
    private MecanumDrive drive;

    public ElapsedTime elapsedtime;
    private final Robot robot = Robot.getInstance();


    @Override
    public void initialize(){
        opModeType = OpModeType.TELEOP;


        // DO NOT REMOVE! Resetting FTCLib Command Sechduler
        super.reset();

        robot.init(hardwareMap);
        elapsedtime = new ElapsedTime();
        elapsedtime.reset();

        //register(robot.intake);

        driver = new GamepadEx(gamepad1);
        drive = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);


        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

//        driver.getGamepadButton(GamepadKeys.Button.TRIANGLE).whenPressed(
//                new InstantCommand(() -> robot.intake.moveSpindex(1))
//
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.CIRCLE).whenPressed(
//                new InstantCommand(() -> robot.intake.moveSpindex(2))
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.CROSS).whenPressed(
//                new InstantCommand(() -> robot.intake.moveSpindex(3))
//        );

        /// pedro position lock
//        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                new InstantCommand(() -> robot.follower.holdPoint(robot.follower.getPose()))
//        );

        /// pedro heading lock
//        if(headingLock) {
//            double targetHeading = Math.toRadians(180);
//
//            double headingError = targetHeading - currentHeading;
//            headingError = Math.IEEEremainder(headingError, 2 * Math.PI);
//
//            if (Math.abs(headingError) < Math.toRadians(2)) {
//                headingCorrection = 0;
//            } else {
//                headingCorrection = headingPIDController.calculate(headingError);
//            }
//
//            follower.setTeleOpMovementVectors(-gamepad1.getLeftY(), gamepad1.getLeftX(), headingCorrection);
//
//        } else {
//            follower.setTeleOpMovementVectors(-gamepad1.getLeftY(), gamepad1.getLeftX(), gamepad1.getRightX());
//        }



        super.run();
    }

    @Override
    public void run() {
        // Keep all the has movement init for until when TeleOp starts
        // This is like the init but when the program is actually started
        if (gameTimer == null) {
            robot.initHasMovement();

            gameTimer = new ElapsedTime();
        }

        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();

        drive.driveRobotCentric(
                driver.getLeftX(),
                driver.getLeftY(),
                driver.getRightX()
        );

        telemetry.addData("Status", "Running");
        //telemetry.addData("right_horizontal: ", robot.spindex.getPosition());
        telemetry.addData("loop times", elapsedtime.milliseconds());
        elapsedtime.reset();

        telemetry.update();

        // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual mode
        // Also only clearing the control hub to decrease loop times
        // This means if we start reading both hubs (which we aren't) we need to clear both
        //robot.ControlHub.clearBulkCache();
        for(LynxModule hub : robot.allHubs) {
            hub.clearBulkCache();
        }
    }

//    @Override
//    public void end() {
//        autoEndPose = robot.follower.getPose();
//    }
}
