package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.GoalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.OpModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.autoEndPose;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.goalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
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
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "Pigeon Teleop MANUAL")
public class TeleOpManual extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;

    public GoBildaPrismDriver prism;
    private MecanumDrive drive;
    public static int adjustSpeed = 0;
    public static int adjustAngle = 0;
    public static int speed = 1200;
    int overShoot = 0;
       int motorPos;


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

        register(robot.intake, robot.outtake/*, robot.lights*/);
        /// LIGHTS
        driver = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);

        robot.stopperServo.set(0.15);
        robot.hoodServo.set(0.27);
        robot.intakeServo.set(0.3);

        motorPos = robot.turretMotor.getCurrentPosition();


        robot.follower.setStartingPose(autoEndPose);


        robot.prism.clearAllAnimations();

//       robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.transpo);

//        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading);
        //robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2);
        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid);


        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.start())
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.snake1)),
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.snake2))
                )
        );

        /*driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2)),
                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.fading))
                )
        );*/
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.stop())
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid))
                )
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.reverse())
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.snake1)),
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.snake2))
                )
        );
//        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2)),
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.fading))
//                )
//        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        //new InstantCommand(() -> robot.intake.stop()),
                        //new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid))
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

        /// MANUAL STOPPER SERVO ADJUSTMENT

        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.05))

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.05))

        );


        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new ParallelCommandGroup(
                    new InstantCommand(() -> robot.stopperServo.set(.5)),
                        new AutoShoot())
        );

        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                new InstantCommand(() -> {
                    robot.outtake.stop();
                    robot.stopperServo.set(.15);
                    robot.intake.stop();
                    startIntake = false;
                }
                )
                )

        );




        super.run();
    }

    @Override
    public void run() {
        // Keep all the has movement init for until when TeleOp starts
        // This is like the init but when the program is actually started
//        Pose currentPose = robot.follower.getPose();
//        double howFar = Math.pow(((144-robot.follower.getPose().getY())/(12)), 2) + Math.pow(((-robot.follower.getPose().getX())/(12)),2);




        if (gameTimer == null) {
            robot.initHasMovement();

            gameTimer = new ElapsedTime();
        }

        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();

        robot.singlePIDF.setSetPoint(0);
        motorPos = robot.turretMotor.getCurrentPosition();
        double maxPower = (robot.f * motorPos) + 1;
        double power = Range.clip(robot.singlePIDF.calculate(motorPos, 0), -maxPower, maxPower);

        if(!robot.singlePIDF.atSetPoint() && gamepad2.left_bumper){
            motorPos = robot.turretMotor.getCurrentPosition();



            robot.turretMotor.setPower(power);
        }else{
            robot.turretMotor.setPower(0);
        }

        telemetry.addData("motorPower", power);
        telemetry.addData("max power", maxPower);






        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);




        telemetry.addData("Status", "Running");
        telemetry.addData("loop times", elapsedtime.milliseconds());
        //telemetry.addData("follower busy", robot.follower.isBusy());
        telemetry.addData("x", robot.follower.getPose().getX());
        telemetry.addData("y", robot.follower.getPose().getY());
        telemetry.addData("angle", Math.toDegrees(robot.follower.getPose().getHeading()));
        //telemetry.addData("speed in feet", test);
        //telemetry.addData("target speed", speed);
        telemetry.addData("target speed ORIGINAL", speed);
        telemetry.addData("adjust speed", adjustSpeed);
        telemetry.addData("FINAL SPEED", speed +adjustSpeed);
        telemetry.addData("how Far", Math.sqrt(Math.pow(((144-robot.follower.getPose().getY())/(12)), 2) + Math.pow(((-robot.follower.getPose().getX())/(12)),2)));
        telemetry.addData("team", goalColor);
        telemetry.addData("targetheading", robot.outtake.autoAlign());
        telemetry.addData("adjust angle", adjustAngle);
        telemetry.addData("motor speed", robot.leftShooter.getVelocity());
        telemetry.addData("turret REAL encoder position", robot.turretMotor.getCurrentPosition());
        //telemetry.addData("SERVO POSITIONNNNN", robot.stopperServo.get());
        elapsedtime.reset();

        telemetry.update();
        robot.follower.update();

        autoEndPose = robot.follower.getPose();

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

        //autoEndPose = robot.follower.getPose();
        //robot.prism.clearAllAnimations();
    }
}
