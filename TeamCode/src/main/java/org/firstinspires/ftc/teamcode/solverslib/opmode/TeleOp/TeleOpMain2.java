package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.GoalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.OpModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.autoEndPose;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.goalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;

import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShoot;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "Pigeon Teleop mark 3")
public class TeleOpMain2 extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;

    public GoBildaPrismDriver prism;
    private MecanumDrive drive;
    public static int adjustSpeed = 0;
    public static int speed = 1200;
    int overShoot = 0;

    public static double targetHeading;
    boolean startIntake = false;
    boolean autoShootDisabled = false;
    double howFar = 0;



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

        register(robot.intake/*, robot.outtake, robot.lights*/);
        /// LIGHTS
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);

        //robot.stopperServo.set(1);
        robot.intakeServo.set(0.3);

        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                        new InstantCommand(() -> robot.intake.start())
        );


        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.stop())
)
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                        new InstantCommand(() -> robot.intake.reverse())
        );

        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.stop())
)

        );






        // REGULAR SHOOTING
//        driver2.getGamepadButton(GamepadKeys.Button.CROSS).whileHeld(
//
//                new ParallelCommandGroup(
//                        new AutoShoot()
//                )
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.CROSS).whenReleased(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
//        );
//
//        /// FAR SHOOTING
//        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE).whileHeld(
//
//                new ParallelCommandGroup(
//                        new AutoShootInAutoFAR()
//                )
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE).whenReleased(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
//        );
//
//
//        /// CLOSE SHOOTING
//        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whileHeld(
//
//                new ParallelCommandGroup(
//                        new AutoShootInAuto()
//                )
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
//        );

        /// MANUAL STOPPER SERVO ADJUSTMENT

        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.05))

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.05))

        );

        /// BACKUP ADJUSTMENT SPEED IF LOCALIZATION DRIFTS

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> adjustSpeed -= 10)

        );

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> adjustSpeed += 10)

        );


        drive = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);


        super.run();
    }

    @Override
    public void run() {
        // Keep all the has movement init for until when TeleOp starts
        // This is like the init but when the program is actually started

        drive.driveRobotCentric(
                driver.getLeftX(),
                driver.getLeftY(),
                driver.getRightX()
        );


        if (gameTimer == null) {
            robot.initHasMovement();

            gameTimer = new ElapsedTime();
        }


        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();





        telemetry.addData("Status", "Running");
        telemetry.addData("loop times", elapsedtime.milliseconds());
        //telemetry.addData("follower busy", robot.follower.isBusy());

        //telemetry.addData("target speed", speed);
        telemetry.addData("SERVO POSITIONNNNN", robot.stopperServo.get());
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

    }
}
