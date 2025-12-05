package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

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
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShoot;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import java.util.List;

@TeleOp(name = "Pigeon Teleop SOLO")
public class TeleOpMainSolo extends CommandOpMode {
    public GamepadEx driver;

    public ElapsedTime gameTimer;
    private MecanumDrive drive;
    public static int speed = 1000;
    public static int adjustSpeed = 0;
    Limelight3A limelight;


    public ElapsedTime elapsedtime;
    private final Robot robot = Robot.getInstance();


    @Override
    public void initialize(){
        opModeType = OpModeType.TELEOP;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        //limelight.start(); // This tells Limelight to start looking!

        limelight.pipelineSwitch(0);


        // DO NOT REMOVE! Resetting FTCLib Command Sechduler
        super.reset();

        robot.init(hardwareMap);
        elapsedtime = new ElapsedTime();
        elapsedtime.reset();

        register(robot.intake, robot.outtake);
        driver = new GamepadEx(gamepad1);

        robot.stopperServo.set(0.65);
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





        /// MANUAL STOPPER SERVO ADJUSTMENT

        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() - 0.05))

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(

                new InstantCommand(() -> robot.stopperServo.set(robot.stopperServo.get() + 0.05))

        );

        /// RELOCALIZATION

        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(

                new InstantCommand(() -> {
                    if(goalColor == GoalColor.RED_GOAL){
                        robot.follower.setPose(new Pose(
                                56, 8, Math.toRadians(90)
                        ));
                        gamepad1.rumbleBlips(3);
                    }else{
                        robot.follower.setPose(new Pose(
                                56, 8, Math.toRadians(90)
                        ).mirror());
                        gamepad1.rumbleBlips(3);
                    }

                }
                )

        );

        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(

                new InstantCommand(() -> {
                    if(goalColor == GoalColor.RED_GOAL){
                        robot.follower.setPose(new Pose(
                                80, 136, Math.toRadians(90)
                        ));
                        gamepad1.rumbleBlips(3);
                    }else{
                        robot.follower.setPose(new Pose(
                                80, 136, Math.toRadians(90)
                        ).mirror());
                        gamepad1.rumbleBlips(3);
                    }

                }
                )

        );


        /// AUTO SHOOTING


        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5).whenActive(
                new InstantCommand(() -> {
                    if(goalColor == GoalColor.RED_GOAL){
                        double newHeading = Math.atan2((144-robot.follower.getPose().getY()), (144-robot.follower.getPose().getX()));
                        robot.follower.turnToDegrees(Math.toDegrees(newHeading));
                        robot.follower.setConstraints(new PathConstraints(
                                0.995,
                                200,
                                1.5,
                                1
                        ));
                    }else{
                        double newHeading = Math.atan2((144-robot.follower.getPose().getY()), -robot.follower.getPose().getX());
                        robot.follower.turnToDegrees(Math.toDegrees(newHeading));
                    }}
                )


        );

        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5).whenInactive(
                new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.stop()),
                        new InstantCommand(() -> robot.intake.stop()),
                        new InstantCommand(() -> robot.stopperServo.set(0.7)),
                        new SequentialCommandGroup(
                                new InstantCommand(() -> robot.follower.breakFollowing()),
                                new InstantCommand(() -> robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true)),
                                new InstantCommand(() -> robot.follower.startTeleopDrive())
                        )
                )


        );




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
        limelight.start();




        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();

//        drive.driveRobotCentric(
//                driver.getLeftX(),
//                driver.getLeftY(),
//                driver.getRightX()
//        );


        speed = robot.outtake.shootAutoGenerator();
        if(speed == -1){
            robot.lightLeft.setPosition(0.28);
            robot.lightRight.setPosition(0.28); //red
        }else{
            robot.lightLeft.setPosition(0.5);
            robot.lightRight.setPosition(0.5); //green

            /// MANUALLY ADJUSTING SIDES
            if(gamepad1.share){
                if(goalColor == GoalColor.BLUE_GOAL){
                    robot.lightLeft.setPosition(0.28);
                    robot.lightRight.setPosition(0.28); //red
                    gamepad1.rumble(100);
                    goalColor = GoalColor.RED_GOAL;
                }else{
                    robot.lightLeft.setPosition(0.6);
                    robot.lightRight.setPosition(0.6); //blue
                    gamepad1.rumble(100);
                    goalColor = GoalColor.BLUE_GOAL;
                }


            }



            if(gamepad1.touchpad){
                robot.follower.breakFollowing();
                //}
                robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
                robot.follower.startTeleopDrive();
            }
            if(gamepad1.left_trigger > 0.5){


                robot.outtake.shootCustom(speed+(adjustSpeed));
                //robot.hoodServo.set(0.5);
                if(robot.leftShooter.getVelocity() > speed-50){
                    robot.intake.startNoHood();
                }else{
                    robot.intake.stopExceptShooter();
                }
            }


        }

        if(gamepad1.right_trigger > 0.5){
            speed = robot.outtake.autoShoot2(adjustSpeed);

            robot.outtake.shootCustom(speed);
            //robot.hoodServo.set(0.5);
            if(robot.leftShooter.getVelocity() > speed-50){
                robot.intake.startNoHood();
            }else{
                robot.intake.stopExceptShooter();
            }
        }




        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);

        /// joystick override
        if ((gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0)&& robot.follower.isBusy()) {
            //if(robot.follower.isBusy()){
                robot.follower.breakFollowing();
            //}
            robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            robot.follower.startTeleopDrive();
        }



        LLResult result = limelight.getLatestResult();
                if(result != null){
                    if(result.isValid()){
                        Pose3D botpose = result.getBotpose();
                        double llX = botpose.getPosition().x;
                        double llY = botpose.getPosition().y;
                        double llHeading = Math.toRadians(botpose.getOrientation().getYaw());

                        Pose2D llPose = new Pose2D(DistanceUnit.INCH,llX,llY, AngleUnit.RADIANS,llHeading);
                        Pose pedroPose = PoseConverter.pose2DToPose(llPose, InvertedFTCCoordinates.INSTANCE).getAsCoordinateSystem(PedroCoordinates.INSTANCE);

                        //robot.follower.setPose(pedroPose);
                        telemetry.addData("horray", pedroPose);


                    }


                        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults(); // fiducials are special markers (like AprilTags)
                        for (LLResultTypes.FiducialResult fiducial : fiducials) {

                            //int id = fiducial.getFiducialId();



//                            if(lastGoodPose == null){
//                                lastGoodPose = pedroPose;
//                            }

                            // The ID number of the fiducial
//                                if(id==20) {
//                                    goals = Globals.GoalColor.BLUE_GOAL;
//                                    telemetry.addData("goal color", "blue!");
//                                } else if (id == 21) {
//                                    randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
//                                    telemetry.addData("randomization:", randomizationMotif.toString());
//                                    telemetry.update();
//                                } else if (id == 22) {
//                                    randomizationMotif = Globals.RandomizationMotif.GREEN_MIDDLE;
//                                    telemetry.addData("randomization:", randomizationMotif.toString());
//                                    telemetry.update();
//                                } else if (id == 23) {
//                                    randomizationMotif = Globals.RandomizationMotif.GREEN_RIGHT;
//                                    telemetry.addData("randomization :", randomizationMotif.toString());
//                                    telemetry.update();
//                                } else if (id == 24){
//                                    goals = Globals.GoalColor.RED_GOAL;
//                                    telemetry.addData("goal color", "red!");
//                                }else {
//                                    //failsafe
//                                    randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
//                                    telemetry.addData("FAILSAFE! :", randomizationMotif.toString());
//                                    telemetry.update();
//                                }
                            }

                    }else{
                    //telemetry.addData("none! :", randomizationMotif.toString());
                }







        telemetry.addData("Status", "Running");
        //telemetry.addData("loop times", elapsedtime.milliseconds());
        telemetry.addData("follower busy", robot.follower.isBusy());
        telemetry.addData("x", robot.follower.getPose().getX());
        telemetry.addData("y", robot.follower.getPose().getY());
        telemetry.addData("angle", Math.toDegrees(robot.follower.getPose().getHeading()));
        telemetry.addData("speed in feet", test);
        telemetry.addData("target speed", speed);
        telemetry.addData("adjust speed", adjustSpeed);
        telemetry.addData("motor speed", robot.leftShooter.getVelocity());
        telemetry.addData("degrees TARGET", Math.toDegrees(Math.atan2((144-robot.follower.getPose().getY()), -robot.follower.getPose().getX())));
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
