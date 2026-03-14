package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

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
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@TeleOp(name = "Pigeon Teleop")
public class TeleOpMain extends CommandOpMode {
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
        robot.hoodServo.set(0);
        robot.intakeServo.set(0.3);

        motorPos = robot.turretMotor.getCurrentPosition();


      //  robot.follower.setStartingPose(autoEndPose);


        //robot.prism.clearAllAnimations();

//        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.transpo);

//        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading);
        //robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2);
        //robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid);


        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(
                        new InstantCommand(() -> robot.intake.start())
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
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.solid)),
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid))
                )
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(
                        new InstantCommand(() -> robot.intake.reverse())
        );
//        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2)),
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.fading))
//                )
//        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.intake.stop())
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.solid)),
//                        new InstantCommand(() -> robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, robot.solid))
                )

        );


        /// OVERRIDE AUTO SHOOT
        driver2.getGamepadButton(GamepadKeys.Button.TOUCHPAD).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.follower.breakFollowing()),
                        new InstantCommand(() -> robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true)),
                        new InstantCommand(() -> robot.follower.startTeleopDrive()),
                        new InstantCommand(() -> autoShootDisabled = !autoShootDisabled)
                )
        );





        /// CLOSE SHOOTING
//        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whileHeld(
//
//                new ParallelCommandGroup(
//                        new AutoShootInAuto()
//                        //new InstantCommand(() -> robot.outtake.reverseShoot())
//                )
//
//        );
//
//        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenReleased(
//
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> robot.outtake.stop()),
//                        new InstantCommand(() -> robot.intake.stop())
//                        //new InstantCommand(() -> robot.outtake.reverseShoot())
//                )
//
//        );

        /// MANUAL STOPPER SERVO ADJUSTMENT

//        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
//
//                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.05))
//
//        );
//
//        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
//
//                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.05))
//
//        );

        /// BACKUP ADJUSTMENT SPEED IF LOCALIZATION DRIFTS

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> adjustSpeed -= 10)

        );

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> adjustSpeed += 10)

        );

        /// BACKUP ADJUSTMENT ANGLE IF TURRET DRIFTS

        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenPressed(

                new InstantCommand(() -> adjustAngle += 2)

        );

        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE).whenPressed(

                new InstantCommand(() -> adjustAngle -= 2)

        );

        /// RELOCALIZATION

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
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

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                //
                new InstantCommand(() -> {

                    if(goalColor == GoalColor.RED_GOAL){
                        robot.follower.setPose(new Pose(
                                79, 134, Math.toRadians(90)
                        ));
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }else{
                        robot.follower.setPose(new Pose(
                                79, 134, Math.toRadians(90)
                        ).mirror());
                        gamepad1.rumbleBlips(3);
                        gamepad2.rumbleBlips(3);
                    }

                }
                )

        );





        driver2.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                //
                new InstantCommand(() -> {
                    if(goalColor == GoalColor.BLUE_GOAL){
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                        goalColor = GoalColor.RED_GOAL;
                        //lightsState = Lights.LightsState.SWITCH_SIDE;
                    }else{
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                        goalColor = GoalColor.BLUE_GOAL;
                        //lightsState = Lights.LightsState.SWITCH_SIDE;
                    }

                }
                )

        );

        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new InstantCommand(() -> robot.stopperServo.set(.5))
        );


                driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(
                //
                new InstantCommand(() -> {
                    robot.outtake.stop();
                    robot.stopperServo.set(.15);
                    robot.intake.stop();
                    startIntake = false;
                }
                )

        );




//        driver2.getGamepadButton(GamepadKeys.Button.SQUARE).whenPressed(
//                //
//                new InstantCommand(() -> {
//                    if(robot.kickerServo.get() == 0){
//                        robot.kickerServo.set(0.58);
//                        autoShootDisabled = true;
//
//                    }else{
//                        robot.kickerServo.set(0);
//                        autoShootDisabled = false;
//                    }
//                    robot.outtake.stop();
//
//
//                }
//                )
//
//        );

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

        //robot.prism.clearAllAnimations();

        //robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.solid);

//        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading);
//        robot.prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, robot.fading2);

//        lightsState = Lights.LightsState.SHOOTER_VALID;
//
//        robot.prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);

        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();

        robot.singlePIDF.setSetPoint(robot.outtake.autoAlign() + adjustAngle);
        motorPos = robot.turretMotor.getCurrentPosition();
        double maxPower = (robot.f * motorPos) + 1;
        double power = Range.clip(robot.singlePIDF.calculate(motorPos, robot.outtake.autoAlign() + adjustAngle), -maxPower, maxPower);

        if(!robot.singlePIDF.atSetPoint() && gamepad2.left_bumper){
            motorPos = robot.turretMotor.getCurrentPosition();



            robot.turretMotor.setPower(power);
        }else{
            robot.turretMotor.setPower(0);
        }

        telemetry.addData("motorPower", power);
        telemetry.addData("max power", maxPower);

        if(goalColor == GoalColor.RED_GOAL){
            howFar = Math.sqrt(Math.pow(((144-robot.follower.getPose().getY())/(12)), 2) + Math.pow(((144-robot.follower.getPose().getX())/(12)),2));
        }else{
            howFar = Math.sqrt(Math.pow(((144-robot.follower.getPose().getY())/(12)), 2) + Math.pow(((-robot.follower.getPose().getX())/(12)),2));
        }


        //speed = robot.outtake.shootAutoGenerator();
        targetHeading = robot.outtake.autoAlign() + adjustAngle;
        speed = robot.outtake.autoShoot2();


        if(speed == -1 || autoShootDisabled){
        }else {

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

//
//            if (gamepad2.triangle) {
//                robot.outtake.stop();
//            } else {
//                robot.outtake.shootCustom(speed + (adjustSpeed) + overShoot);
//            }

            if (howFar > 10) {
                robot.hoodServo.set(0.27);
            } else {
                robot.hoodServo.set(0.1);
            }

            if (gamepad2.right_bumper) {

                robot.outtake.shootCustom(speed + adjustSpeed + overShoot);

                //robot.outtake.rapidShooting(adjustSpeed);
                //   robot.outtake.shootCustom(speed + (adjustSpeed) + overShoot);




                /// ONLY START THE INTAKE ONCE THE SHOOTER VELOCITY IS MET AND ROBOT IS WITHIN 5 DEGREES OF TARGET ANGLE AND NOT BUSY
                if (robot.leftShooter.getVelocity() > speed + adjustSpeed - 20
                ) {
//            if(howFar > 10){
//                new InstantCommand(() -> new WaitCommand(500));
//            }
                    startIntake = true;
                    //robot.intake.startNoHood();
                }


                if (startIntake) {
                    robot.intake.startLower();
                } else {
                    robot.intake.stopExceptShooter();
                }
//                if (howFar < 10) {
//                    if(startIntake){
//                        robot.intake.startLower();
//                    }else{
//                        robot.intake.stopExceptShooter();
//                    }
//
//                }
//                else{
//
//                    if (robot.leftShooter.getVelocity() > speed + adjustSpeed) {
//
//                        robot.intake.startNoHood();
//                    } else {
//                        robot.intake.stopExceptShooter();
//                    }
//            }
            }
//            else{
//                robot.outtake.stop();
//                robot.stopperServo.set(.15);
//            }
            //}



//            if(gamepad2.right_bumper){
//                robot.outtake.shootCustom(speed +(adjustSpeed));
//                robot.stopperServo.set(.47);
//                /// ONLY START THE INTAKE ONCE THE SHOOTER VELOCITY IS MET AND ROBOT IS WITHIN 5 DEGREES OF TARGET ANGLE AND NOT BUSY
//                if(robot.leftShooter.getVelocity() > speed +adjustSpeed && Math.abs(robot.follower.getPose().getHeading() - targetHeading) < Math.toRadians(5) && !robot.follower.isBusy()){
//
//                    //robot.intake.startNoHood();
//                }else{
//                    robot.intake.stopExceptShooter();
//                }
//            }

        }







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
        //robot.prism.clearAllAnimations();
    }
}
