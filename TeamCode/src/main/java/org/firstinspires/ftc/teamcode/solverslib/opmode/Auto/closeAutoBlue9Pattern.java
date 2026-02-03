package org.firstinspires.ftc.teamcode.solverslib.opmode.Auto;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.RapidShoot;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;



@Autonomous(name = "yv \uD83D\uDD35", group = "auto")
public class closeAutoBlue9Pattern extends CommandOpMode{

    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;
    public PathChain GetFirstPile;
    public PathChain GoToShoot;
    public PathChain SetUpForBack;
    public PathChain GetReadyForBack;
    public PathChain CollectBackAndShoot;
    public PathChain StealMiddleOne;
    public PathChain FlipMiddle;
    public PathChain GetMiddleAndShoot;

    private final Pose startPose = new Pose(27.61595273264401, 127.87296898079765, Math.toRadians(0));

    private final Pose blueTopShootPose = new Pose(51,96, Math.toRadians(144));

    private final Pose blueTopShootPoseFinal = new Pose(54,110, Math.toRadians(150));

    public void generatePath() {
        GetFirstPile = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                startPose,
                                new Pose(118.297, 80.319),
                                new Pose(10.482, 84.496)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180))

                .build();

        GoToShoot = robot.follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(10.482, 84.496),

                                blueTopShootPose
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(144))

                .build();

        SetUpForBack = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                blueTopShootPose,
                                new Pose(98.52067946824222, 38.6056129985229),
                                new Pose(8.765, 47.832)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(270))

                .build();

        GetReadyForBack = robot.follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(8.765, 47.832),

                                new Pose(8.767, 35.756)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(0))

                .build();

        CollectBackAndShoot = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(8.767, 35.756),
                                new Pose(99.798, 31.569),
                                blueTopShootPose
                        )
                ).setTangentHeadingInterpolation()

                .build();

        StealMiddleOne = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                blueTopShootPose,
                                new Pose(65.191, 54.739),
                                new Pose(37.434, 59.080)
                        )
                ).setTangentHeadingInterpolation()

                .build();

        FlipMiddle = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(37.434, 59.080),
                                new Pose(21.840, 18.060),
                                new Pose(5.183, 59.651)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))

                .build();

        GetMiddleAndShoot = robot.follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(5.183, 59.651),
                                new Pose(80.545, 53.028),
                                blueTopShootPoseFinal
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), blueTopShootPoseFinal.getHeading())

                .build();
    }

    public SequentialCommandGroup firstPileAndShoot() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new InstantCommand(() -> robot.intake.start()),
                new FollowPathCommand(robot.follower, GetFirstPile, false),
                new WaitCommand(500),
                new InstantCommand(() -> robot.intake.stop()),
                new FollowPathCommand(robot.follower, GoToShoot, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1090),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(1000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup thirdPileAndShoot() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, SetUpForBack, false),
                new WaitCommand(500),
                new InstantCommand(() -> robot.follower.setMaxPower(0.5)),
                new FollowPathCommand(robot.follower, GetReadyForBack, true),
                new InstantCommand(() -> robot.intake.start()),
                new FollowPathCommand(robot.follower, CollectBackAndShoot, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1090),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(1000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup secondPileAndShoot() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(() -> robot.follower.setMaxPower(0.5)),
                new InstantCommand(() -> robot.intake.start()),
                new FollowPathCommand(robot.follower, StealMiddleOne, true),
                new WaitCommand(500),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, FlipMiddle, false),
                new FollowPathCommand(robot.follower, GetMiddleAndShoot, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1090),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(1000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }





    @Override
    public void initialize() {
        opModeType = OpModeType.AUTO;
        goalColor = GoalColor.BLUE_GOAL;
        timer = new ElapsedTime();
        timer.reset();

        // DO NOT REMOVE! Resetting FTCLib Command Scheduler
        super.reset();

        robot.init(hardwareMap);

        robot.stopperServo.set(0.56);


//        Limelight3A limelight;
//
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
//        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
//        limelight.start(); // This tells Limelight to start looking!
//
//        limelight.pipelineSwitch(1); // pipleline 1 is our AprilTags pipeline
//
//        LLResult result = limelight.getLatestResult();
//
//        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults(); // fiducials are special markers (like AprilTags)
//        for (LLResultTypes.FiducialResult fiducial : fiducials) {
//            int id = fiducial.getFiducialId(); // The ID number of the fiducial
//            if(id == 21){
//                randomizationMotif = RandomizationMotif.GREEN_LEFT;
//            }else if(id == 22){
//                randomizationMotif = RandomizationMotif.GREEN_MIDDLE;
//            }else if(id == 23){
//                randomizationMotif = RandomizationMotif.GREEN_RIGHT;
//            }else{
//                //failsafe
//                randomizationMotif = RandomizationMotif.GREEN_LEFT;
//            }
//        }
//
//

        // Initialize subsystems
        register(robot.intake, robot.outtake);

        //robot.initHasMovement();
        generatePath();
        robot.follower.setStartingPose(startPose);
        robot.follower.setMaxPower(1);

        schedule(
//                    // DO NOT REMOVE: updates follower to follow path
                new RunCommand(() -> robot.follower.update()),

                /*new SequentialCommandGroup(
                        scorePreload(),

                        grabTopBlue(),

                        scoreTopBlue(),

                        grabMiddleBlue(),

                        scoreMiddleBlue(),

                        grabBottomBlue(),

                        scoreBottomBlue(),

                        parkAndStuff()
                )*/

                new SequentialCommandGroup(
                        firstPileAndShoot(),
                        thirdPileAndShoot(),
                        secondPileAndShoot()
                )
        );



    }

    @Override
    public void initialize_loop(){

        //telemetry.addData("randomization:", randomizationMotif.toString());
        telemetry.update();
        //drawOnlyCurrent();
    }

    @Override
    public void run() {
        super.run();

        robot.outtake.shootAuto();

        telemetry.addData("timer", timer.milliseconds());

        telemetry.addData("followerIsBusy", robot.follower.isBusy());
        telemetry.addData("repeat", test);
        telemetry.addData("servo pos", robot.stopperServo.get());
        telemetry.addData("shooterReady", shooterReady);
        telemetry.addData("velocity motor", robot.leftShooter.getVelocity());
        telemetry.addData("Heading", Math.toDegrees(robot.follower.getPose().getHeading()));
        telemetry.addData("Heading Error", Math.toDegrees(robot.follower.getHeadingError()));


        telemetry.update(); // DO NOT REMOVE! Needed for telemetry

//        // Drawing path on panels?
        //draw();


        // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual mode
        // Also only clearing the control hub to decrease loop times
        // This means if we start reading both hubs (which we aren't) we need to clear both
        for(LynxModule hub : robot.allHubs) {
            hub.clearBulkCache();
        }
    }

    @Override
    public void end() {
        autoEndPose = robot.follower.getPose();
    }
}
