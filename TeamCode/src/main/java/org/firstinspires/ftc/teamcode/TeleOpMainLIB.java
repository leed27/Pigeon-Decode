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
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "Pigeon Teleop")
public class TeleOpMainLIB extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;
    private MecanumDrive drive;
    double speed = 1;

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

        register(robot.intake, robot.outtake);
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        //drive = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);


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



        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                new InstantCommand(() -> robot.hoodServo.set(0.5))
        );


        /// REGULAR SHOOTING
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

        /// FAR SHOOTING
        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE).whileHeld(

                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.leftShooter.set(1)),
//                        new InstantCommand(() -> robot.rightShooter.set(1)),
                        new AutoShootInAutoFAR()
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop()),
                        new InstantCommand(() -> robot.intake.stop())
                )

        );


        /// CLOSE SHOOTING
        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whileHeld(

                new ParallelCommandGroup(
                        new AutoShootInAuto()
                        //new InstantCommand(() -> robot.outtake.reverseShoot())
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(

                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop()),
                        new InstantCommand(() -> robot.intake.stop())
                        //new InstantCommand(() -> robot.outtake.reverseShoot())
                )

        );

        /// EMERGENCY OUTTAKE SHOOTING
        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whileHeld(

                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.leftShooter.set(-0.5)),
                        new InstantCommand(() -> robot.rightShooter.set(-0.5))
                        //new InstantCommand(() -> robot.outtake.reverseShoot())
                )

        );


        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop())
                        //new InstantCommand(() -> robot.intake.stop())
                )

        );


        /// AUTO PEDRO PATHS (not recommended)
//        driver2.getGamepadButton(GamepadKeys.Button.TOUCHPAD).whenPressed(
//                new ConditionalCommand(
//                        new ConditionalCommand(
//                                new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), redParkPose)), true),
//                                new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), blueParkPose)), true),
//                                () -> goalColor == GoalColor.RED_GOAL
//                        )
//                        , new InstantCommand(() -> {
//                            gamepad2.rumble(200);
//                }),
//                        () -> goalColor != null
//                )
//
//
//
//        );
//
//        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5).whenActive(
//                new ConditionalCommand(
//                        new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), blueShootClose.mirror())), true),
//                        new FollowPathCommand(robot.follower, new Path(new BezierLine(robot.follower.getPose(), blueShootClose)), true),
//                        () -> goalColor == GoalColor.RED_GOAL
//                )
//
//        );
//
//        //position lock
//        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5).whenActive(
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

//        drive.driveRobotCentric(
//                driver.getLeftX(),
//                driver.getLeftY(),
//                driver.getRightX()
//        );



        if(shooterReady){

            robot.lightLeft.setPosition(0.5);
            robot.lightRight.setPosition(0.5); //green
        }else{
            robot.lightLeft.setPosition(0.28);
            robot.lightRight.setPosition(0.28); //red
        }

        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);

        //joystick override
        if ((gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0) && robot.follower.isBusy()) {
            //if(robot.follower.isBusy()){
                robot.follower.breakFollowing();
            //}
            robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            robot.follower.startTeleopDrive();
        }

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
        robot.follower.update();

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
