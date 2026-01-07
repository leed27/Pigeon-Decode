package org.firstinspires.ftc.teamcode.solverslib.opmode.Auto;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

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
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Autonomous(name = "CLOSE 12 BALL \uD83D\uDD34", group = "auto")
public class closeAutoRed extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
    private final Pose startPose = new Pose(24.6, 128.4, Math.toRadians(144)).mirror(); //e
    /// blue paths
    private final Pose blueTopPilePose = new Pose(52,84, Math.toRadians(180)).mirror(); //e

    private final Pose blueTopPileForwardPose = new Pose(17, 84, Math.toRadians(180)).mirror(); //e
    private final Pose blueMiddlePilePose = new Pose(52, 59, Math.toRadians(180)).mirror();
    private final Pose blueMiddlePileForwardPose = new Pose(15, 59, Math.toRadians(180)).mirror();

    private final Pose readyGatePose = new Pose(30, 59, Math.toRadians(180)).mirror();
    private final Pose openGatePose = new Pose(18, 70, Math.toRadians(180)).mirror();
    private final Pose controlPose = new Pose(79, 37);
    private final Pose blueBottomPilePose = new Pose(52, 36, Math.toRadians(180)).mirror();
    private final Pose blueBottomPileForwardPose = new Pose(15, 36, Math.toRadians(180)).mirror();
    private final Pose blueTopShootPose = new Pose(51,96, Math.toRadians(144)).mirror();
    private final Pose blueTopShootPose2 = new Pose(51,96, Math.toRadians(135));

    private final Pose blueBottomShootPose =  new Pose(55, 15, Math.toRadians(120));

    private final Pose parkPose = new Pose(34, 80, Math.toRadians(135)).mirror();
    private Path grabTopBlue;
    private PathChain shootPreloads, collectTopBlue, shootTopBlue, grabMiddleBlue, goBackMiddleBlue, goBackMiddleBlue2, collectMiddleBlue, readyGateBlue, openGateBlue, shootMiddleBlue, grabEndBlue, collectEndBlue, goBackEndBlue, shootEndBlue, park;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);

        shootPreloads = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, blueTopShootPose))
                .setConstantHeadingInterpolation(startPose.getHeading())
                .build();

        grabTopBlue = new Path(new BezierLine(blueTopShootPose, blueTopPilePose));
        grabTopBlue.setLinearHeadingInterpolation(startPose.getHeading(), blueTopPilePose.getHeading());

        collectTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopPilePose, blueTopPileForwardPose))
                .setConstantHeadingInterpolation(blueTopPilePose.getHeading())
                .build();
        shootTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopPileForwardPose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueTopPileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();
        grabMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopShootPose, blueMiddlePilePose))
                .setLinearHeadingInterpolation(blueTopShootPose.getHeading(), blueMiddlePilePose.getHeading())
                .build();
        collectMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueMiddlePilePose, blueMiddlePileForwardPose))
                .setConstantHeadingInterpolation(blueMiddlePilePose.getHeading())
                .build();

        readyGateBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueMiddlePileForwardPose, readyGatePose))
                .setConstantHeadingInterpolation(openGatePose.getHeading())
                .build();

        openGateBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(readyGatePose, openGatePose))
                .setConstantHeadingInterpolation(openGatePose.getHeading())
                .build();

        goBackMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueMiddlePileForwardPose, blueMiddlePilePose))
                .setConstantHeadingInterpolation(blueMiddlePilePose.getHeading())
                .build();

        goBackMiddleBlue2 = robot.follower.pathBuilder()
                .addPath(new BezierLine( openGatePose, blueMiddlePilePose))
                .setConstantHeadingInterpolation(blueMiddlePilePose.getHeading())
                .build();

        shootMiddleBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueMiddlePilePose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueMiddlePileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();

        grabEndBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopShootPose, blueBottomPilePose))
                .setLinearHeadingInterpolation(blueTopShootPose.getHeading(), blueBottomPilePose.getHeading())
                .build();

        goBackEndBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueBottomPileForwardPose, blueBottomPilePose))
                .setConstantHeadingInterpolation(blueBottomPileForwardPose.getHeading())
                .build();

        collectEndBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueBottomPilePose, blueBottomPileForwardPose))
                .setConstantHeadingInterpolation(blueBottomPileForwardPose.getHeading())
                .build();

        shootEndBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueBottomPileForwardPose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueBottomPileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();

        park = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopShootPose, parkPose))
                .setConstantHeadingInterpolation(blueTopShootPose.getHeading())
                .build();

    }


    public SequentialCommandGroup scorePreload() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
//                new InstantCommand(() -> robot.outtake.shootAuto()),
//
                new FollowPathCommand(robot.follower, shootPreloads, true),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAuto()
                ).withTimeout(1500),

                //new WaitCommand(3000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup grabTopBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, grabTopBlue, true),
                new InstantCommand(() -> robot.follower.setMaxPower(.7)),
                //start intake
                new InstantCommand(() -> robot.intake.start()),
                new FollowPathCommand(robot.follower, collectTopBlue, false).withTimeout(3000),
                new WaitCommand(500),
                //stop intake
                new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(1))
        );
    }

    public SequentialCommandGroup scoreTopBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootTopBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAuto()
                ).withTimeout(1500),
                //new WaitCommand(3000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup grabMiddleBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, grabMiddleBlue, true),
                new InstantCommand(() -> robot.follower.setMaxPower(.7)),
                //start intake
                new InstantCommand(() -> robot.intake.startCustom(1)),
                new FollowPathCommand(robot.follower, collectMiddleBlue, false).withTimeout(3000),
                new WaitCommand(500),
                //stop intake
                new InstantCommand(() -> robot.follower.setMaxPower(1))
        );
    }

    public SequentialCommandGroup openGate() {
        return new SequentialCommandGroup(
                //
                new FollowPathCommand(robot.follower, readyGateBlue, false),
                new InstantCommand(() ->robot.intake.stop()),
                new FollowPathCommand(robot.follower, openGateBlue, false).withTimeout(1000),
                new WaitCommand(250),
                new InstantCommand(() -> robot.follower.setMaxPower(1))
        );
    }



    public SequentialCommandGroup scoreMiddleBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, goBackMiddleBlue, true),
                new FollowPathCommand(robot.follower, shootMiddleBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAuto()
                ).withTimeout(1500),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup scoreMiddleBlue2() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, goBackMiddleBlue2, true),
                new FollowPathCommand(robot.follower, shootMiddleBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAuto()
                ).withTimeout(1500),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup grabBottomBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, grabEndBlue, true),
                new InstantCommand(() -> robot.follower.setMaxPower(.6)),
                //start intake
                new InstantCommand(() -> robot.intake.startCustom(1)),
                new FollowPathCommand(robot.follower, collectEndBlue, false).withTimeout(3000),
                new WaitCommand(500),
                //new FollowPathCommand(robot.follower, goBackEndBlue, false),
                new InstantCommand(() -> robot.follower.setMaxPower(.8)),
                //stop intake

                new InstantCommand(() ->robot.intake.stop())
        );
    }

    public SequentialCommandGroup scoreBottomBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootEndBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAuto()
                ).withTimeout(1500),
                new FollowPathCommand(robot.follower, park, true),
                new InstantCommand(() -> robot.outtake.stop()),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup parkAndStuff() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, park, false)
        );
    }






    @Override
    public void initialize() {
        opModeType = OpModeType.AUTO;
        goalColor = GoalColor.RED_GOAL;
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
                        new InstantCommand(),
                        scorePreload(),

                        grabMiddleBlue(),

                        openGate(),

                        scoreMiddleBlue2(),

                        grabTopBlue(),

                        scoreTopBlue(),

                        grabBottomBlue(),

                        scoreBottomBlue(),

                        parkAndStuff()
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

