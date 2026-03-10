package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.GoalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.OpModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.autoEndPose;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.goalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.turretEncoder;

import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShoot;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootCustom;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import kotlin.time.Instant;

@TeleOp(name = "Pigeon Teleop mark 3")
public class TeleOpMain2 extends CommandOpMode {
    public GamepadEx driver, driver2;

    public ElapsedTime gameTimer;

    public GoBildaPrismDriver prism;
    private MecanumDrive drive;
    public static int adjustSpeed = 0;
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

        //motorPos= robot.turretMotor.getCurrentPosition();

        //robot.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.stopperServo.set(0.15);
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
//        driver.getGamepadButton(GamepadKeys.Button.CROSS).whileHeld(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.stopperServo.set(0.5)),
//                        new AutoShoot()
//                )
//
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.CROSS).whenReleased(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.stopperServo.set(0.1)),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
//        );

        /// FAR SHOOTING
//        driver.getGamepadButton(GamepadKeys.Button.CIRCLE).whileHeld(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.stopperServo.set(0.5)),
//                        new AutoShootInAutoFAR()
//                )
//
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.CIRCLE).whenReleased(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.stopperServo.set(0.1)),
//                        new InstantCommand(() -> robot.intake.stop())
//                )
//
//        );


        /// CLOSE SHOOTING
//        driver.getGamepadButton(GamepadKeys.Button.SQUARE).whileHeld(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.stopperServo.set(0.5)),
//                        new AutoShootInAuto()
//                )
//
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.stopperServo.set(0.1)),
//                        new InstantCommand(() -> robot.intake.stop())
//
//                )
//
//        );

        /// MANUAL STOPPER SERVO ADJUSTMENT

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.05))

        );

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.05))

        );

        /// BACKUP ADJUSTMENT SPEED IF LOCALIZATION DRIFTSx

        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> adjustSpeed -= 10)

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> adjustSpeed += 10)

        );


        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> robot.turretMotor.setTargetPosition(robot.turretMotor.getCurrentPosition() + 5))

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> robot.turretMotor.setTargetPosition(robot.turretMotor.getCurrentPosition() - 5))

        );
        driver.getGamepadButton(GamepadKeys.Button.CROSS).whileHeld(
                new ParallelCommandGroup(
                        //new InstantCommand(() ->  robot.controller.setSetPoint(robot.outtake.autoAlign())),
//                        new InstantCommand(() ->  robot.turretMotor.setTargetPosition()),
                        new InstantCommand(() -> robot.stopperServo.set(0.5))
                )

        );

        driver.getGamepadButton(GamepadKeys.Button.CROSS).whenReleased(
                new ParallelCommandGroup(
                        //new InstantCommand(() -> robot.turretMotor.set(0)),
                        new InstantCommand(() -> robot.intake.stop()),
                        new InstantCommand(() -> robot.stopperServo.set(0.1))
                        //new InstantCommand(() -> robot.turretMotor.stopMotor())


                )

        );

        driver.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                //
                new InstantCommand(() -> {
                    if(goalColor == GoalColor.BLUE_GOAL){
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                        goalColor = GoalColor.RED_GOAL;
                    }else{
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                        goalColor = GoalColor.BLUE_GOAL;
                    }

                }
                )

        );






        /// RELOCALIZATION
//
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                //facing the wall
                new InstantCommand(() -> {
                    if(goalColor == GoalColor.RED_GOAL){
                        robot.follower.setPose(new Pose(
                                11, 9, Math.toRadians(180)
                        ));
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }else{
                        robot.follower.setPose(new Pose(
                                11, 9, Math.toRadians(180)
                        ).mirror());
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }

                }
                )

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                //
                new InstantCommand(() -> {

                    if(goalColor == GoalColor.RED_GOAL){
                        robot.follower.setPose(new Pose(
                                80, 136, Math.toRadians(90)
                        ));
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }else{
                        robot.follower.setPose(new Pose(
                                80, 136, Math.toRadians(90)
                        ).mirror());
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }

                }
                )

        );



        //drive = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);


        super.run();
    }

    @Override
    public void run() {
        // Keep all the has movement init for until when TeleOp starts
        // This is like the init but when the program is actually started

//        drive.driveRobotCentric(
//                driver.getLeftX(),
//                driver.getLeftY(),
//                driver.getRightX()
//        );

       // robot.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        if (gameTimer == null) {
            robot.initHasMovement();

            gameTimer = new ElapsedTime();
        }

        //robot.follower.startTeleopDrive();

        // DO NOT REMOVE! Runs FTCLib Command Scheudler`
        super.run();

//        robot.singlePIDF.setP(robot.p);
//        robot.singlePIDF.setI(robot.i);
//        robot.singlePIDF.setD(robot.d);
//        robot.singlePIDF.setF(robot.f);

//
        if(!robot.singlePIDF.atSetPoint() && gamepad1.cross){
        //robot.singlePIDF.setSetPoint(robot.outtake.autoAlign());
            motorPos = robot.turretMotor.getCurrentPosition();


            double maxPower = (robot.f * motorPos) + 1;
            double power = Range.clip(robot.singlePIDF.calculate(motorPos, robot.outtake.autoAlign()), -maxPower, maxPower);

            robot.turretMotor.setPower(power);
        }
//        else{
//            robot.turretMotor.setPower(0);
//        }

        speed = robot.outtake.autoShoot2();

        if (speed == -1 || autoShootDisabled) {
        } else {

//            if(goalColor == GoalColor.RED_GOAL){
//                targetHeading = Math.atan2((144-robot.follower.getPose().getY()), (144-robot.follower.getPose().getX()));
//            }else{
//                targetHeading = Math.atan2((144-robot.follower.getPose().getY()), -robot.follower.getPose().getX());
//            }


            /*if(howFar < 5){
                overShoot = 20;
            }else if(howFar < 7){
                overShoot = 30;
            }else if(howFar > 8){
                overShoot = 0;
            }*/


            if (gamepad1.cross) {
                robot.outtake.shootCustom(speed + (adjustSpeed) + overShoot);
                if (robot.leftShooter.getVelocity() > speed + adjustSpeed - 10) {
//            if(howFar > 10){
//                new InstantCommand(() -> new WaitCommand(500));
//            }
                    startIntake = true;
                    //robot.intake.startNoHood();
                }else{
                    startIntake = false;
                }

                if (howFar < 10) {
                    if (startIntake) {
                        robot.intake.startNoHood();
                    } else {
                        robot.intake.stopExceptShooter();
                    }

                } else {

                    if (robot.leftShooter.getVelocity() > speed + adjustSpeed ) {

                        robot.intake.startNoHood();
                    } else {
                        robot.intake.stopExceptShooter();
                    }
                }

            }
//            else {
//                robot.outtake.stop();
//
//            }


//            if (gamepad1.cross) {
//                //robot.outtake.rapidShooting(adjustSpeed);
//
//                //robot.outtake.shootCustom(speed + adjustSpeed + overShoot);
//                //robot.stopperServo.set(.4);
//
//                /// ONLY START THE INTAKE ONCE THE SHOOTER VELOCITY IS MET AND ROBOT IS WITHIN 5 DEGREES OF TARGET ANGLE AND NOT BUSY
//
//            }


            //robot.turretMotor.setTargetPosition(robot.outtake.autoAlign());

            robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);

//        if(robot.outtake.autoAlign() == -200){
//            gamepad1.rumbleBlips(1);
//        }

            telemetry.addData("Status", "Running");
            telemetry.addData("loop times", elapsedtime.milliseconds());
            //telemetry.addData("follower busy", robot.follower.isBusy());

            //telemetry.addData("target speed", speed);
//            telemetry.addData("SERVO POSITIONNNNN", robot.stopperServo.get());
//            telemetry.addData("turret", robot.turretMotor.getCurrentPosition());
//            telemetry.addData("x", robot.follower.getPose().getX());
//            telemetry.addData("y", robot.follower.getPose().getY());
//            telemetry.addData("angle", Math.toDegrees(robot.follower.getPose().getHeading()));
//            telemetry.addData("turret angle", robot.outtake.autoAlign());
//            telemetry.addData("goal color", goalColor);
//            telemetry.addData("speed", speed);
//            telemetry.addData("adjustspeed", adjustSpeed);
//            telemetry.addData("motor speed real", robot.leftShooter.getVelocity());



            //telemetry.addData("turret", robot.turretMotor.getTargetPosition());

            elapsedtime.reset();
            robot.follower.update();

            telemetry.update();

            // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual mode
            // Also only clearing the control hub to decrease loop times
            // This means if we start reading both hubs (which we aren't) we need to clear both
            //robot.ControlHub.clearBulkCache();
            for (LynxModule hub : robot.allHubs) {
                hub.clearBulkCache();
            }
        }
    }


    @Override
    public void end() {
        turretEncoder = robot.turretMotor.getCurrentPosition();
        autoEndPose = robot.follower.getPose();
//        robot.prism.clearAllAnimations();
    }
}
