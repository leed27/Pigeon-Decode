package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.draw;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawOnlyCurrent;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import java.util.List;

@Autonomous(name = "closeAuto", group = "auto")
public class closeAuto extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
    private final Pose startPose = new Pose(24, 128, Math.toRadians(143));
    /// blue paths
    private final Pose blueTopPilePose = new Pose(48,84, Math.toRadians(180));

    private final Pose blueTopPilePoseControl = new Pose(49, 134);
    private final Pose blueTopPileForwardPose = new Pose(17, 84, Math.toRadians(180));
    private final Pose blueMiddlePilePose = new Pose(48, 60, Math.toRadians(180));
    private final Pose blueMiddlePileForwardPose = new Pose(17, 60, Math.toRadians(180));
    private final Pose blueBottomPilePose = new Pose(48, 36);
    private final Pose blueBottomPileForwardPose = new Pose(17, 36, Math.toRadians(180));
    private final Pose blueTopShootPose = new Pose(51,96, Math.toRadians(135));

    private final Pose blueBottomShootPose =  new Pose(55, 15, Math.toRadians(120));
    private Path grabTopBlue;
    private PathChain collectTopBlue, shootTopBlue, grabMiddleBlue, collectMiddleBlue, shootMiddleBlue;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        robot.follower.setStartingPose(startPose);

        grabTopBlue = new Path(new BezierCurve(startPose, blueTopPilePoseControl, blueTopPilePose));
        grabTopBlue.setLinearHeadingInterpolation(startPose.getHeading(), blueTopPilePose.getHeading());

        collectTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopPilePose, blueTopPileForwardPose))
                .setConstantHeadingInterpolation(blueTopPilePose.getHeading())
                .build();
        shootTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierCurve( blueTopPileForwardPose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueTopPileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .setTimeoutConstraint(100)
                .setTValueConstraint(0.95)
                .build();
        grabMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopShootPose, blueMiddlePilePose))
                .setLinearHeadingInterpolation(blueTopShootPose.getHeading(), blueMiddlePilePose.getHeading())
                .setBrakingStrength(3.2)
                .build();
        collectMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueMiddlePilePose, blueMiddlePileForwardPose))
                .setConstantHeadingInterpolation(blueMiddlePilePose.getHeading())
                .build();

        shootMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueMiddlePileForwardPose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueMiddlePileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();


    }

    public SequentialCommandGroup scorePreload() {
        return new SequentialCommandGroup(
                //shoots thingy
                new SequentialCommandGroup(
//                    new InstantCommand(() -> robot.outtake.armUp()),
//                    new WaitCommand(250),
//                    new InstantCommand(() -> robot.outtake.rotateClaw(OUTTAKE_ROTATED)),
                    new WaitCommand(150)
                )
        );
    }

    public SequentialCommandGroup grabTopBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, grabTopBlue, false),
                //start intake
//                new InstantCommand(() -> robot.outtake.armUp()),
                new FollowPathCommand(robot.follower, collectTopBlue, false),
                new WaitCommand(250)
                //stop intake
//                new InstantCommand(() -> robot.outtake.armUp())
        );
    }

    public SequentialCommandGroup scoreTopBlue(){
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootTopBlue, false)
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
        //register(robot.intake);

        robot.initHasMovement();


        robot.follower.setMaxPower(1);

        generatePath();

//        //different paths for each auto
//        if(randomizationMotif == RandomizationMotif.GREEN_LEFT){
//            schedule(
//                    // DO NOT REMOVE: updates follower to follow path
//                    new RunCommand(() -> robot.follower.update()),
//
//                    new SequentialCommandGroup(
//                            // Specimen 1
//                            scorePreload(),
//
//                            grabTopBlue(),
//
//                            scoreTopBlue()
//                    )
//            );
//        }else if(randomizationMotif == RandomizationMotif.GREEN_MIDDLE){
//            schedule(
//                    // DO NOT REMOVE: updates follower to follow path
//                    new RunCommand(() -> robot.follower.update()),
//
//                    new SequentialCommandGroup(
//                            // Specimen 1
//                            scorePreload()//,
//
//                            //grabMiddleBlue(),
//                            //scoreMiddleBlue()
//                    )
//            );
//        }else{
//            schedule(
//                    // DO NOT REMOVE: updates follower to follow path
//                    new RunCommand(() -> robot.follower.update()),
//
//                    new SequentialCommandGroup(
//                            // Specimen 1
//                            scorePreload()//,
//
//                            //grabBottomBlue(),
//                            //scoreBottomBlue()
//                    )
//            );
//        }




    }

    @Override
    public void initialize_loop(){
//        if(gamepad2.circle){
//            new InstantCommand(() -> robot.outtake.closeClaw());
//        }

        //telemetry.addData("randomization:", randomizationMotif.toString());
        telemetry.update();
        //drawOnlyCurrent();
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

//        // Drawing path on panels?
        draw();


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

