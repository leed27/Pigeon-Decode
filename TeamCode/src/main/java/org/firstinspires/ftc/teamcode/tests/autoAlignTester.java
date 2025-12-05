package org.firstinspires.ftc.teamcode.tests;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Disabled
@Autonomous(name = "TESTTTTTT", group = "auto")
public class autoAlignTester extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
    private final Pose startPose = new Pose(24.6, 128.4, Math.toRadians(144));

    private final Pose pose1 = new Pose(95, 128, Math.toRadians(0));

    private final Pose pose2 = new Pose(82, 100, Math.toRadians(0));

    private final Pose pose3 = new Pose(65, 80, Math.toRadians(0));

    private final Pose pose4 = new Pose(48, 109, Math.toRadians(0));

    private final Pose pose5 = new Pose(83, 20, Math.toRadians(0));

    private final Pose pose6 = new Pose(60, 20, Math.toRadians(0));
    /// blue paths
    private PathChain topose1, topose2, topose3, topose4, topose5, topose6;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);

        //MADE BY ADHITHYA YUVARAJ :D

        topose1 = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, pose1))
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.atan2((144- pose1.getY()), -pose1.getX()))
                .build();

        topose2 = robot.follower.pathBuilder()
                .addPath(new BezierLine(pose1, pose2))
                .setLinearHeadingInterpolation(pose1.getHeading(), Math.atan2((144- pose2.getY()), 144-pose2.getX()))
                .build();

        topose3 = robot.follower.pathBuilder()
                .addPath(new BezierLine(pose2, pose3))
                .setLinearHeadingInterpolation(pose2.getHeading(), Math.atan2((144- pose3.getY()), -pose3.getX()))
                .build();

        topose4 = robot.follower.pathBuilder()
                .addPath(new BezierLine(pose3, pose4))
                .setLinearHeadingInterpolation(pose3.getHeading(), Math.atan2((144- pose4.getY()), 144-pose4.getX()))
                .build();

        topose5 = robot.follower.pathBuilder()
                .addPath(new BezierLine(pose4, pose5))
                .setLinearHeadingInterpolation(pose4.getHeading(), Math.atan2((144- pose5.getY()), -pose5.getX()))
                .build();

        topose6 = robot.follower.pathBuilder()
                .addPath(new BezierLine(pose5, pose6))
                .setLinearHeadingInterpolation(pose5.getHeading(), Math.atan2((144- pose6.getY()), 144-pose6.getX()))
                .build();

    }

    public SequentialCommandGroup f1() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose1, true),
                new WaitCommand(3000)

        );
    }

    public SequentialCommandGroup f2() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose2, true),
                new WaitCommand(3000)

        );
    }

    public SequentialCommandGroup f3() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose3, true),
                new WaitCommand(3000)

        );
    }

    public SequentialCommandGroup f4() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose4, true),
                new WaitCommand(3000)

        );
    }

    public SequentialCommandGroup f5() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose5, true),
                new WaitCommand(3000)

        );
    }

    public SequentialCommandGroup f6() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, topose6, true),
                new WaitCommand(3000)

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
                        f1(),

                        f2(),

                        f3(),

                        f4(),

                        f5(),

                        f6()
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

        telemetry.addData("timer", timer.milliseconds());

        telemetry.addData("followerIsBusy", robot.follower.isBusy());
        telemetry.addData("repeat", test);
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

