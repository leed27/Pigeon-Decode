package org.firstinspires.ftc.teamcode.solverslib.opmode.Auto;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Autonomous(name = "CLOSE 15 BALL \uD83D\uDD35", group = "auto")
public class closeAutoBlue extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
    private final Pose startPose = new Pose(33.2, 134.5, Math.toRadians(270));
    private final Pose firstShootPose = new Pose(48, 95.5, Math.toRadians(290));//e
    private final Pose middleBallPose = new Pose(24, 60, Math.toRadians(180));
    private final Pose openGatePose = new Pose(11.25, 59, Math.toRadians(200));
    private final Pose shootGatePose = new Pose(57.5, 84, Math.toRadians(180));
    private final Pose topBallPose = new Pose(24, 84, Math.toRadians(180));

    private final Pose lastBallPose = new Pose(42, 9, Math.toRadians(180));
    /// blue paths
    private PathChain shootPreloads, getMiddle, shootMiddle, openGate, shootGate, getTop, shootTop, getBottom, shootBottom;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);

        shootPreloads = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, firstShootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), firstShootPose.getHeading())
                .build();

        getMiddle = robot.follower.pathBuilder()
                .addPath(new BezierCurve(firstShootPose, new Pose(83.6, 57.2), middleBallPose))
                .setLinearHeadingInterpolation(firstShootPose.getHeading(), middleBallPose.getHeading())
                .build();

        shootMiddle = robot.follower.pathBuilder()
                .addPath(new BezierCurve(middleBallPose, new Pose(83.6, 57.2), firstShootPose))
                .setLinearHeadingInterpolation(middleBallPose.getHeading(), firstShootPose.getHeading())
                .build();

        openGate = robot.follower.pathBuilder()
                .addPath(new BezierCurve(firstShootPose, new Pose(51, 25.5), openGatePose))
                .setLinearHeadingInterpolation(firstShootPose.getHeading(), openGatePose.getHeading())
                .build();

        shootGate = robot.follower.pathBuilder()
                .addPath(new BezierLine(openGatePose, shootGatePose))
                .setLinearHeadingInterpolation(firstShootPose.getHeading(), openGatePose.getHeading())
                .build();

        getTop = robot.follower.pathBuilder()
                .addPath(new BezierLine(shootGatePose, topBallPose))
                .setConstantHeadingInterpolation(topBallPose.getHeading())
                .build();

        shootTop = robot.follower.pathBuilder()
                .addPath(new BezierLine(topBallPose, shootGatePose))
                .setConstantHeadingInterpolation(topBallPose.getHeading())
                .build();

        getBottom = robot.follower.pathBuilder()
                .addPath(new BezierCurve(shootGatePose, new Pose(84, 31), lastBallPose))
                .setTangentHeadingInterpolation()
                .build();

        shootBottom = robot.follower.pathBuilder()
                .addPath(new BezierCurve(shootGatePose, new Pose(84, 31), new Pose(54, 111)))
                .setTangentHeadingInterpolation()
                .build();



    }

    public SequentialCommandGroup scorePreload() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup grabTopBlue() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup scoreTopBlue() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup grabMiddleBlue() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup openGate() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }



    public SequentialCommandGroup scoreGate() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup grabBottomBlue() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

        );
    }

    public SequentialCommandGroup scoreBottomBlue() {
        return new SequentialCommandGroup(
                new WaitCommand(100)

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

                new SequentialCommandGroup(
                        new InstantCommand(),
                        scorePreload(),

                        grabMiddleBlue(),

                        openGate(),

                        scoreGate(),

                        grabTopBlue(),

                        scoreTopBlue(),

                        grabBottomBlue(),

                        scoreBottomBlue()
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

