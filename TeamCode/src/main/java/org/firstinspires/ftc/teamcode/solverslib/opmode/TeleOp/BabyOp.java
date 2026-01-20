package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Lights.lightsState;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.GoalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.OpModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.autoEndPose;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.goalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.test;

import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.Direction;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShoot;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Lights;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import java.util.List;

@TeleOp(name = "Baby Teleop")
public class BabyOp extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;
    private MecanumDrive drive;
    public static int speed = 1000;
    public static int adjustSpeed = 0;

    GoBildaPrismDriver prism;

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
        prism = hardwareMap.get(GoBildaPrismDriver.class, "prism");


        register(robot.intake, robot.outtake, robot.lights);
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);

        robot.lights.constantColor = robot.lights.ORANGE;
        lightsState = Lights.LightsState.CONSTANT_COLOR;

        robot.stopperServo.set(0.56);

        /// this is what slows it down i think probably
        robot.follower.setMaxPower(0.5);

        PrismAnimations.Solid solid = new PrismAnimations.Solid(new Color(225, 30, 0));
        PrismAnimations.Solid transpo = new PrismAnimations.Solid(Color.TRANSPARENT);
        PrismAnimations.Snakes snake1 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, (float) (Math.PI/120.0F), Direction.Forward, new Color (225, 30, 0));
        PrismAnimations.Snakes snake2 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, (float) (Math.PI/120.0F), Direction.Forward, new Color (225, 30, 0));
        PrismAnimations.SineWave fading = new PrismAnimations.SineWave(new Color (225, 30, 0),  Color.TRANSPARENT, 6, (float) (Math.PI/36.0F), 0.3F, Direction.Forward);
        PrismAnimations.SineWave fading2 = new PrismAnimations.SineWave(new Color (225, 30, 0),  Color.TRANSPARENT, 6, (float) (Math.PI/36.0F), 0.3F, Direction.Forward);

        solid.setBrightness(100);
        solid.setStartIndex(0);
        solid.setStopIndex(12);

        snake1.setBrightness(100);
        snake1.setStartIndex(0);
        snake1.setStopIndex(5);

        snake2.setBrightness(100);
        snake2.setStartIndex(6);
        snake2.setStopIndex(11);

        fading.setBrightness(100);
        fading2.setBrightness(100);

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



        /// REGULAR SHOOTING
//        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileHeld(
//
//                new ParallelCommandGroup(
////                        new InstantCommand(() -> robot.leftShooter.set(1)),
////                        new InstantCommand(() -> robot.rightShooter.set(1)),
//                        new AutoShoot()
//                )
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
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
//                        //new InstantCommand(() -> robot.outtake.reverseShoot())
//                )

//        );

        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(

                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop()),
                        new InstantCommand(() -> robot.intake.stop())
                        //new InstantCommand(() -> robot.outtake.reverseShoot())
                )

        );






        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.fading2);


        super.run();
    }

    @Override
    public void run() {

        //prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.fading2);

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







        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);

        /// joystick override
        if ((gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0)&& robot.follower.isBusy()) {
            //if(robot.follower.isBusy()){
                robot.follower.breakFollowing();
            //}
            robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            robot.follower.startTeleopDrive();
        }







        telemetry.addData("Status", "Running");
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
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.solid);
    }
}
