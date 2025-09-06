package solverslib.opmode.Auto;

import static solverslib.hardware.Globals.*;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
//import pedroPathing.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import java.util.List;

import solverslib.commandbase.commands.GrabSpecimen;
import solverslib.commandbase.commands.ScoreSpecimen;
import solverslib.hardware.Robot;

@Autonomous(name = "closeAuto", group = "auto")
public class closeAuto extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    //private DashboardPoseTracker dashboardPoseTracker;

    // ALL PATHS
    private final Pose startPose = new Pose(9, 72, Math.toRadians(180));
    private final Pose scorePrePose = new Pose(40,72, Math.toRadians(180));

    private final Pose pushSplineControl1 = new Pose(20, 30);
    private final Pose pushSplineEnd = new Pose(30, 30, Math.toRadians(180));
    private final Pose returnFirst = new Pose(68,43);
    private final Pose strafeFirst = new Pose(56, 25);
    private final Pose pushFirst = new Pose(26, 26);
    private final Pose returnSecond = new Pose(58, 27);
    //    private final Pose returnSecond = new Pose(56, 25);
    private final Pose strafeSecond = new Pose(56, 15);
    private final Pose pushSecond =  new Pose(24, 15);
    private final Pose returnThird = new Pose(80, 15); //suggested old 75, 17
    private final Pose strafeThird = new Pose(70, 4); //old y = 5
    private final Pose pushThird =  new Pose(20, 8.5); // x = 17
    private final Pose grabSplineControl = new Pose(28, 32);

    //old x = 9
    private final Pose grabForwardPose = new Pose(9, 32, Math.toRadians(180));
    private final Pose grabForwardPose2 = new Pose(8.7, 32, Math.toRadians(180));
    private final Pose grabForwardPose3 = new Pose(8.3, 32, Math.toRadians(180));
    private final Pose grabForwardPose4 = new Pose(8.3, 32, Math.toRadians(180));
    private final Pose grabPose = new Pose(25, 32); //x = 30

    private final Pose scoreFirstPose = new Pose(40, 70, Math.toRadians(180));
    private final Pose scoreSecondPose = new Pose(40, 69, Math.toRadians(180));
    private final Pose safetyScore = new Pose(40, 69, Math.toRadians(180));
    private final Pose scoreThirdPose = new Pose(40, 68, Math.toRadians(180));
    private final Pose scoreFourthPose = new Pose(40, 67, Math.toRadians(180));
    private final Pose parkPose = new Pose(12, 10);
    private Path scorePreload;
    private PathChain pushSpline, pushBlocks, grabSpline, scoreFirst, grabSecond, scoreSecond, grabThird, scoreThird, grabFourth, scoreFourth, parkGood;

    public void generatePath() {
        // If you want to edit the pathing copy and update the json code/.pp file found in the Recipes package into https://pedro-path-generator.vercel.app/
        // Then paste the following code https://pedro-path-generator.vercel.app/ spits out at you (excluding the top part with the class and constructor headers)
        // Make sure to update the Recipes package so others can update the pathing as well
        // NOTE: .setTangentialHeadingInterpolation() doesn't exist its .setTangentHeadingInterpolation() so just fix that whenever you paste

        // Starting Pose (update this as well):
        robot.follower.setStartingPose(startPose);

        scorePreload = new Path(new BezierLine(startPose, scorePrePose));
        scorePreload.setConstantHeadingInterpolation(Math.toRadians(180));

        pushSpline = robot.follower.pathBuilder()
                .addPath(new BezierCurve( scorePrePose,  pushSplineControl1,  pushSplineEnd,  returnFirst,  strafeFirst))
                .setConstantHeadingInterpolation(scorePrePose.getHeading())
                .build();
        pushBlocks  = robot.follower.pathBuilder()
                .addPath(new BezierCurve( strafeFirst,  pushFirst))
                .setTimeoutConstraint(100)
                .setTValueConstraint(0.95)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(new BezierCurve( pushFirst,  returnSecond,  strafeSecond))
                .setTimeoutConstraint(100)
                .setTValueConstraint(0.95)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(new BezierLine( strafeSecond,  pushSecond))
                .setTimeoutConstraint(100)
                .setTValueConstraint(0.95)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .addPath(new BezierCurve( pushSecond,  returnThird,  strafeThird,  pushThird))
                .setTimeoutConstraint(100)
                .setTValueConstraint(0.95)
                .setConstantHeadingInterpolation(Math.toRadians(180))
//                .addPath(new BezierLine( strafeThird),  pushThird)))
//                .setPathEndTimeoutConstraint(100)
//                .setPathEndTValueConstraint(0.95)
//                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        grabSpline = robot.follower.pathBuilder()
                //.addPath(new BezierCurve( pushThird),  grabSplineControl),  grabForwardPose)))
                .addPath(new BezierLine( pushThird,  grabForwardPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreFirst = robot.follower.pathBuilder()
                .addPath(new BezierLine( grabForwardPose,  scoreFirstPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreSecond = robot.follower.pathBuilder()
                .addPath(new BezierLine( grabForwardPose2,  scoreSecondPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreThird = robot.follower.pathBuilder()
                .addPath(new BezierLine( grabForwardPose3,  scoreThirdPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreFourth = robot.follower.pathBuilder()
                .addPath(new BezierLine( grabForwardPose4,  scoreFourthPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        grabSecond = robot.follower.pathBuilder()
                .addPath(new BezierCurve( scoreFirstPose,  grabPose,  grabForwardPose2))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setBrakingStrength(3.2)
                .build();
        grabThird = robot.follower.pathBuilder()
                .addPath(new BezierCurve( scoreSecondPose,  grabPose,  grabForwardPose3))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setBrakingStrength(2)
                .build();
        grabFourth = robot.follower.pathBuilder()
                .addPath(new BezierCurve( scoreThirdPose,  grabPose,  grabForwardPose4))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setBrakingStrength(2)
                .build();
        parkGood = robot.follower.pathBuilder()
                .addPath(new BezierLine( grabForwardPose,  parkPose))
                .build();

    }

    public SequentialCommandGroup scorePreload() {
        return new SequentialCommandGroup(
                //goes to chamber
                new SequentialCommandGroup(
                    new InstantCommand(() -> robot.outtake.armUp()),
                    new WaitCommand(250),
                    new InstantCommand(() -> robot.outtake.rotateClaw(OUTTAKE_ROTATED)),
                    new WaitCommand(150),
                    new FollowPathCommand(robot.follower, scorePreload, true)
                ),
                //scores on chamber
                new SequentialCommandGroup(
                    new InstantCommand(() -> robot.outtake.armScore()),
                    new WaitCommand(1600),
                    new ParallelCommandGroup(
                        new InstantCommand(() -> robot.outtake.openClaw()),
                        new InstantCommand(() -> robot.outtake.armGrab())
                    ),
                    new WaitCommand(200),
                    new InstantCommand(() -> robot.outtake.rotateClaw(OUTTAKE_NOTROTATED))

                )
        );
    }

    public SequentialCommandGroup pushSamples() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, pushSpline, false),
                new FollowPathCommand(robot.follower, pushBlocks, false),
                new FollowPathCommand(robot.follower, grabSpline, false)
        );
    }

    public SequentialCommandGroup scoreSpecimens(int cycle){
        PathChain speciScorePath;
        PathChain speciGrabPath;
        if(cycle == 1){
            speciScorePath = scoreFirst;
            speciGrabPath = grabSecond;
        }else if(cycle == 2){
            speciScorePath = scoreSecond;
            speciGrabPath = grabThird;
        }else if(cycle == 3){
            speciScorePath = scoreThird;
            speciGrabPath = grabFourth;
        //last cycle
        }else{
            speciScorePath = scoreFourth;
            speciGrabPath = parkGood;
        }
        return new SequentialCommandGroup(
                //will only run subsequent actions when waituntil condition is met
                new WaitUntilCommand(() -> {
                    return robot.follower.getPose().getX() < 11;
                }),
                new GrabSpecimen(robot),
                new FollowPathCommand(robot.follower, speciScorePath, true),
                new ScoreSpecimen(robot),
                //new WaitCommand(500),
                new FollowPathCommand(robot.follower, speciGrabPath, true)

        );

    }

    @Override
    public void initialize() {
        opModeType = OpModeType.AUTO;
        timer = new ElapsedTime();
        timer.reset();

        // DO NOT REMOVE! Resetting FTCLib Command Scheduler
        super.reset();

        robot.init(hardwareMap);


        Limelight3A limelight;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!

        limelight.pipelineSwitch(1); // pipleline 1 is our AprilTags pipeline

        LLResult result = limelight.getLatestResult();

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults(); // fiducials are special markers (like AprilTags)
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId(); // The ID number of the fiducial
            if(id == 21){
                randomizationMotif = RandomizationMotif.GREEN_LEFT;
            }else if(id == 22){
                randomizationMotif = RandomizationMotif.GREEN_MIDDLE;
            }else if(id == 23){
                randomizationMotif = RandomizationMotif.GREEN_RIGHT;
            }else{
                //failsafe
                randomizationMotif = RandomizationMotif.GREEN_LEFT;
            }
        }



        // Initialize subsystems
        register(robot.outtake, robot.intake);

        robot.initHasMovement();

        robot.follower.setMaxPower(1);

        generatePath();

        //different paths for each auto
        if(randomizationMotif == RandomizationMotif.GREEN_LEFT){
            schedule(
                    // DO NOT REMOVE: updates follower to follow path
                    new RunCommand(() -> robot.follower.update()),

                    new SequentialCommandGroup(
                            // Specimen 1
                            scorePreload(),

                            pushSamples(),

                            scoreSpecimens(1),
                            scoreSpecimens(2),
                            scoreSpecimens(3),
                            //score last then park
                            scoreSpecimens(4)
                    )
            );
        }else if(randomizationMotif == RandomizationMotif.GREEN_MIDDLE){
            schedule(
                    // DO NOT REMOVE: updates follower to follow path
                    new RunCommand(() -> robot.follower.update()),

                    new SequentialCommandGroup(
                            // Specimen 1
                            scorePreload(),

                            pushSamples(),

                            scoreSpecimens(1),
                            scoreSpecimens(2),
                            scoreSpecimens(3),
                            //score last then park
                            scoreSpecimens(4)
                    )
            );
        }else{
            schedule(
                    // DO NOT REMOVE: updates follower to follow path
                    new RunCommand(() -> robot.follower.update()),

                    new SequentialCommandGroup(
                            // Specimen 1
                            scorePreload(),

                            pushSamples(),

                            scoreSpecimens(1),
                            scoreSpecimens(2),
                            scoreSpecimens(3),
                            //score last then park
                            scoreSpecimens(4)
                    )
            );
        }



//        dashboardPoseTracker = new DashboardPoseTracker(robot.poseUpdater);
//        Drawing.drawRobot(robot.poseUpdater.getPose(), "#4CAF50");
//        Drawing.sendPacket();

    }

    @Override
    public void initialize_loop(){
        if(gamepad2.circle){
            new InstantCommand(() -> robot.outtake.closeClaw());
        }
    }

    @Override
    public void run() {
        super.run();

        telemetry.addData("timer", timer.milliseconds());

        telemetry.addData("followerIsBusy", robot.follower.isBusy());
        telemetry.addData("target pose", robot.follower.getClosestPose());
        telemetry.addData("Robot Pose", robot.follower.getPose());
        telemetry.addData("Heading", Math.toDegrees(robot.follower.getPose().getHeading()));
        telemetry.addData("Heading Error", Math.toDegrees(robot.follower.getHeadingError()));


        telemetry.update(); // DO NOT REMOVE! Needed for telemetry

        // Pathing telemetry
        //dashboardPoseTracker.update();
//        Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
//        Drawing.drawRobot(robot.poseUpdater.getPose(), "#4CAF50");
//        Drawing.sendPacket();

        // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual mode
        // Also only clearing the control hub to decrease loop times
        // This means if we start reading both hubs (which we aren't) we need to clear both
        robot.ControlHub.clearBulkCache();
    }

    @Override
    public void end() {
        autoEndPose = robot.follower.getPose();
    }
}

