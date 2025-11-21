package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.bylazar.gamepad.*;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShoot;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "Pigeon Teleop")
public class TeleOpMainLIB extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;
    private MecanumDrive drive;
    double speed = 1;

    public ElapsedTime elapsedtime;
    public double targetSpeed = 1200;
    private final Robot robot = Robot.getInstance();


    @Override
    public void initialize(){
        opModeType = OpModeType.TELEOP;


        // DO NOT REMOVE! Resetting FTCLib Command Sechduler
        super.reset();

        robot.init(hardwareMap);
        elapsedtime = new ElapsedTime();
        elapsedtime.reset();

        register(robot.intake, robot.outtake);
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        //robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        drive = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);


        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(
                new InstantCommand(() -> robot.intake.start())

        );
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new InstantCommand(() -> robot.intake.stop())

                );

        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                new InstantCommand(() -> robot.intake.reverse())

        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new InstantCommand(() -> robot.intake.stop())

        );



//        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
//                new InstantCommand(() -> robot.hoodServo.set(robot.hoodServo.get() - 0.1) )
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
//                new InstantCommand(() -> robot.hoodServo.set(robot.hoodServo.get() + 0.1))
//        );

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                new InstantCommand(() -> robot.hoodServo.set(0.5))
        );

//        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
//                new InstantCommand(() -> robot.stopperServo.set(0.5))
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
//                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.1))
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
//                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.1))
//        );


        //auto shooting
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileHeld(

                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.leftShooter.set(1)),
//                        new InstantCommand(() -> robot.rightShooter.set(1)),
                        new AutoShoot()
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop()),
                        new InstantCommand(() -> robot.intake.stop())
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whileHeld(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.leftShooter.set(1)),
                        new InstantCommand(() -> robot.rightShooter.set(1)))


                        );

        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop())
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.TOUCHPAD).whenPressed(
                new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), shootClosePose)), true)


        );

        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5).whenActive(
                new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), shootClosePose)), true)
        );

        //position lock
        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5).whenActive(
                new InstantCommand(() -> robot.follower.holdPoint(robot.follower.getPose()))
        );

        /// EXPERIMENTAL PIDF SHOOTER
//        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
//                        new InstantCommand(() -> robot.outtake.setFlywheel(2, false)) // provide velocity in m/s?
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
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



        if(robot.leftShooter.getVelocity() > targetSpeed){

            robot.lightLeft.setPosition(0.5);
            robot.lightRight.setPosition(0.5); //green
        }else{
            robot.lightLeft.setPosition(0.28);
            robot.lightRight.setPosition(0.28); //red
        }


        //joystick override
//        if ((gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0) && robot.follower.isBusy()) {
//            robot.follower.breakFollowing();
//            robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
//            robot.follower.startTeleopDrive();
//        }

        telemetry.addData("Status", "Running");
        //telemetry.addData("loop times", elapsedtime.milliseconds());
        telemetry.addData("servo", robot.hoodServo.get());
        telemetry.addData("follower busy", robot.follower.isBusy());
        telemetry.addData("servo23", robot.lightLeft.getPosition());
        telemetry.addData("motor speed", robot.leftShooter.getVelocity());
        telemetry.addData("motor speed right", robot.rightShooter.getVelocity());
        telemetry.addData("digaierg right", shooterReady);
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

    @Override
    public void end() {
        autoEndPose = robot.follower.getPose();
    }
}
