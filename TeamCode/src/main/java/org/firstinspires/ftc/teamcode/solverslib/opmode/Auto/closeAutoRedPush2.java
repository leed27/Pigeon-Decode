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

@Autonomous(name = "12 BALL + PUSH \uD83D\uDD34", group = "auto")
public class closeAutoRedPush2 extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
//    private final Pose startPose = new Pose(24.6, 128.4, Math.toRadians(144)); //e
    private final Pose startPose = new Pose(33.2, 134.5, Math.toRadians(90)).mirror(); //e

    private final Pose parkPose2 = new Pose(60, 30, Math.toRadians(0)).mirror();


    /// blue paths
    private final Pose blueTopPilePose = new Pose(51,84, Math.toRadians(180)).mirror(); //e
    private final Pose blueTopPileForwardPose = new Pose(17, 84, Math.toRadians(180)).mirror();
    private final Pose blueMiddlePileForwardPose = new Pose(10, 59, Math.toRadians(180)).mirror();
    private final Pose readyGatePose = new Pose(27, 63, Math.toRadians(180)).mirror(); //old X = 30
    private final Pose openGatePose = new Pose(18, 78, Math.toRadians(180)).mirror();

    private final Pose openGatePose2 = new Pose(13, 71, Math.toRadians(180)).mirror();
    private final Pose blueBottomPileForwardPose = new Pose(10, 36, Math.toRadians(180)).mirror();
    private final Pose blueTopShootPose = new Pose(51,96, Math.toRadians(135)).mirror();
    private final Pose prepPushPose = new Pose(61, 50, Math.toRadians(0)).mirror();

    private final Pose getReadyPushPose = new Pose(61, 9, Math.toRadians(0)).mirror();
    private final Pose pushPose = new Pose(30, 9, Math.toRadians(0)).mirror();

    private final Pose parkPose = new Pose(34, 80, Math.toRadians(135)).mirror();
    private Path grabTopBlue;
    private PathChain shootPreloads, collectTopBlue, shootTopBlue;

    private PathChain prepPushing, getToPushing, pushTime, pushToPark;
    private PathChain readyGateBlue, openGateBlue, openGateBlue2;
    private PathChain grabAndCollectMiddle, grabAndCollectBottom, bezzieBackMiddle, bezzieBackBottom, bezzieMiddleGate, bezzieBackMiddle2;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);\

        shootPreloads = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, blueTopShootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), blueTopShootPose.getHeading())
                //.setConstantHeadingInterpolation(startPose.getHeading())
                .build();

        //BEIZIER TS
        grabAndCollectMiddle = robot.follower.pathBuilder()
                .addPath(new BezierCurve( blueTopShootPose, new Pose(72, 54).mirror(), blueMiddlePileForwardPose))
                .setConstantHeadingInterpolation(blueMiddlePileForwardPose.getHeading())
                .build();

        bezzieMiddleGate = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueMiddlePileForwardPose, openGatePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), blueTopShootPose.getHeading())
                //.setConstantHeadingInterpolation(startPose.getHeading())
                .build();

        bezzieBackMiddle = robot.follower.pathBuilder()
                .addPath(new BezierCurve( blueMiddlePileForwardPose, new Pose(55, 72).mirror(), blueTopShootPose))
                .setLinearHeadingInterpolation(blueMiddlePileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();

        bezzieBackMiddle2 = robot.follower.pathBuilder()
                .addPath(new BezierCurve( openGatePose2, new Pose(55, 66).mirror(), blueTopShootPose))
                .setLinearHeadingInterpolation(openGatePose2.getHeading(), blueTopShootPose.getHeading())
                .build();

        grabAndCollectBottom = robot.follower.pathBuilder()
                .addPath(new BezierCurve( blueTopShootPose, new Pose(69, 24).mirror(), blueBottomPileForwardPose))
                .setConstantHeadingInterpolation(blueBottomPileForwardPose.getHeading())
                .build();

        bezzieBackBottom = robot.follower.pathBuilder()
                .addPath(new BezierCurve( blueBottomPileForwardPose, new Pose(73.5, 58.4).mirror(), blueTopShootPose))
                .setLinearHeadingInterpolation(blueMiddlePileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();

        openGateBlue2 = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueMiddlePileForwardPose, readyGatePose))
                .setConstantHeadingInterpolation(readyGatePose.getHeading())
                .addPath(new BezierLine(readyGatePose, openGatePose2))
                .setConstantHeadingInterpolation(openGatePose2.getHeading())
                .build();

        readyGateBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueMiddlePileForwardPose, readyGatePose))
                .setConstantHeadingInterpolation(openGatePose.getHeading())
                .build();

        openGateBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueMiddlePileForwardPose, openGatePose))
                .setConstantHeadingInterpolation(openGatePose.getHeading())
                .build();

        grabTopBlue = new Path(new BezierLine(blueTopShootPose, blueTopPilePose));
        grabTopBlue.setLinearHeadingInterpolation(blueTopShootPose.getHeading(), blueTopPilePose.getHeading());

        collectTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopPilePose, blueTopPileForwardPose))
                .setConstantHeadingInterpolation(blueTopPilePose.getHeading())
                .build();
        shootTopBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( blueTopPileForwardPose, blueTopShootPose))
                .setLinearHeadingInterpolation(blueTopPileForwardPose.getHeading(), blueTopShootPose.getHeading())
                .build();



        prepPushing = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueTopShootPose, prepPushPose))
                .setConstantHeadingInterpolation(prepPushPose.getHeading())
                .build();


        getToPushing = robot.follower.pathBuilder()
                .addPath(new BezierLine(prepPushPose, getReadyPushPose))
                .setConstantHeadingInterpolation(getReadyPushPose.getHeading())
                .build();

        pushTime = robot.follower.pathBuilder()
                .addPath(new BezierLine( getReadyPushPose, pushPose))
                .setConstantHeadingInterpolation(getReadyPushPose.getHeading())
                .build();

        pushToPark = robot.follower.pathBuilder()
                .addPath(new BezierLine(pushPose, parkPose2))
                .setConstantHeadingInterpolation(parkPose2.getHeading())
                .build();

    }

    public SequentialCommandGroup grabTopBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, grabTopBlue, true),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                //start intake
                new InstantCommand(() -> robot.intake.start()),
                new FollowPathCommand(robot.follower, collectTopBlue, false).withTimeout(3000),
                new WaitCommand(500),
                //stop intake
                //new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(1))
        );
    }

    public SequentialCommandGroup biezerMiddle() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, grabAndCollectMiddle, false).withTimeout(5000),
                new WaitCommand(500),
                //new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(0.7)),
                new FollowPathCommand(robot.follower, openGateBlue2, true).withTimeout(2500),
                //new FollowPathCommand(robot.follower, readyGateBlue, true),
                //new InstantCommand(() -> robot.follower.setMaxPower(0.7)),
                //new FollowPathCommand(robot.follower, openGateBlue, true).withTimeout(500),
                //new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, bezzieBackMiddle2, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1070),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(400),

                //new WaitCommand(3000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup biezerEnd() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, grabAndCollectBottom, false).withTimeout(5500),
                new WaitCommand(500),
                //new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, bezzieBackBottom, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1070),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(400)

                //new WaitCommand(3000),
                //new InstantCommand(() -> robot.intake.stop()),
                //new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }
    public SequentialCommandGroup scoreTopBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootTopBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
                new WaitCommand(500),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1070),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(400),

                //new WaitCommand(3000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }
    public SequentialCommandGroup scorePreload() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new InstantCommand(() -> robot.stopperServo.set(.47)),
//                new InstantCommand(() -> robot.outtake.shootAuto()),
//
                new FollowPathCommand(robot.follower, shootPreloads, true),
                new WaitCommand(200),
                new WaitUntilCommand(() -> robot.leftShooter.getVelocity() > 1070),
                new RepeatCommand(
                        new RapidShoot()
                ).withTimeout(750),

                //new WaitCommand(3000),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup pushAndPark(){
        return new SequentialCommandGroup(
                new InstantCommand(()->robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, prepPushing, false).withTimeout(1500),
                new InstantCommand(()->robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, getToPushing, true).withTimeout(500),
                new FollowPathCommand(robot.follower, pushTime, true).withTimeout(2000),
                new WaitCommand(500),
                new InstantCommand(()->robot.follower.setMaxPower(0.8))
                //new FollowPathCommand(robot.follower, pushToPark, true)
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

                        biezerMiddle(), //it opens gate now :D

                        grabTopBlue(),

                        scoreTopBlue(),

                        biezerEnd(),

                        pushAndPark()
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

