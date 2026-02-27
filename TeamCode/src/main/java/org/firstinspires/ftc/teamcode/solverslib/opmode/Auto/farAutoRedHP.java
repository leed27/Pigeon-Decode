package org.firstinspires.ftc.teamcode.solverslib.opmode.Auto;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.GoalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.OpModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.autoEndPose;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.goalColor;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.shooterReady;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.test;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Autonomous(name = "FAR HP \uD83D\uDD34", group = "auto")
public class farAutoRedHP extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;
    private boolean delayEnabled;


    //private final ArrayList<PathChain> paths = new ArrayList<>();

    private final Pose startPose = new Pose(88, 9, Math.toRadians(90));
    private final Pose shootPose = new Pose(88, 15, Math.toRadians(69));
    /// blue paths6
    private final Pose parkPose = new Pose(108,14, Math.toRadians(70));

    private final Pose blueDepotPilePose = new Pose(10, 10, Math.toRadians(180)).mirror();
    private final Pose blueDepotPileAnglePose = new Pose(10.5, 10, Math.toRadians(200)).mirror();
    private final Pose blueDepotPileAnglePose2 = new Pose(12.5, 10, Math.toRadians(200)).mirror();
    private final Pose blueDepotPileBackPose = new Pose(20, 9, Math.toRadians(180)).mirror();
    private final Pose blueDepotPileForwardPose = new Pose(10, 9, Math.toRadians(180)).mirror();
    private final Pose blueDepotControlPose = new Pose(37,10).mirror();
    private final Pose humanPlayerForwardPose = new Pose(12.5, 13.664, Math.toRadians(180)).mirror();
    private final Pose humanPlayerBackPose = new Pose(24, 13.664, Math.toRadians(180)).mirror();
    private PathChain startToShoot, grabEndBlue, collectEndBlue, uncollectDepotBlue, shootEndBlue, grabDepotBlue, collectDepotBlue, shootDepotBlue, grabHumanPlayer, shootHumanPlayer, shootToPark;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);

        //open .47
        //close .56


        startToShoot = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();


        grabDepotBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine( shootPose, blueDepotPilePose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), blueDepotPilePose.getHeading(), .6)
                .build();


        collectDepotBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueDepotPilePose, blueDepotPileBackPose))
                .setLinearHeadingInterpolation(blueDepotPilePose.getHeading(), blueDepotPileBackPose.getHeading())
                //.addPath(new BezierLine(blueDepotPileBackPose, blueDepotPilePose))
                //.setLinearHeadingInterpolation(blueDepotPileBackPose.getHeading(), blueDepotPilePose.getHeading())
                //.addPath(new BezierLine(blueDepotPilePose, blueDepotPileBackPose))
                //.setLinearHeadingInterpolation(blueDepotPilePose.getHeading(), blueDepotPileBackPose.getHeading())
                .addPath(new BezierLine(blueDepotPileBackPose, blueDepotPileAnglePose))
                .setLinearHeadingInterpolation(blueDepotPileBackPose.getHeading(), blueDepotPileAnglePose.getHeading())
                .addPath(new BezierLine(blueDepotPileAnglePose, blueDepotPileAnglePose2))
                .setLinearHeadingInterpolation(blueDepotPileBackPose.getHeading(), blueDepotPileAnglePose.getHeading())
                .addPath(new BezierLine(blueDepotPileAnglePose2, blueDepotPileAnglePose))
                .setLinearHeadingInterpolation(blueDepotPileAnglePose2.getHeading(), blueDepotPileAnglePose.getHeading())
                .build();

        uncollectDepotBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueDepotPileAnglePose, blueDepotPileBackPose))
                .setLinearHeadingInterpolation(blueDepotPileAnglePose.getHeading(), blueDepotPileBackPose.getHeading())
                .build();

        shootDepotBlue = robot.follower.pathBuilder()
                .addPath(new BezierLine(blueDepotPilePose, shootPose))
                .setLinearHeadingInterpolation(blueDepotPilePose.getHeading(), shootPose.getHeading(), .8)
                .build();

        grabHumanPlayer = robot.follower.pathBuilder()
                .addPath(new BezierLine(shootPose, blueDepotPileForwardPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), humanPlayerForwardPose.getHeading(), .1)
                .addPath(new BezierLine(blueDepotPileForwardPose, blueDepotPileBackPose))
                .setConstantHeadingInterpolation(humanPlayerForwardPose.getHeading())
                .addPath(new BezierLine(blueDepotPileBackPose, humanPlayerForwardPose))
                .setConstantHeadingInterpolation(humanPlayerForwardPose.getHeading())
                .build();

        shootHumanPlayer = robot.follower.pathBuilder()
                .addPath(new BezierLine(humanPlayerForwardPose, shootPose))
                .setLinearHeadingInterpolation(humanPlayerForwardPose.getHeading(), shootPose.getHeading(), .8)
                .build();


        shootToPark = robot.follower.pathBuilder()
                .addPath(new BezierLine(shootPose, parkPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), parkPose.getHeading())
                .build();


    }

    public SequentialCommandGroup startToShoot() {
        return new SequentialCommandGroup(
                //new WaitCommand(6000),
                new InstantCommand(() -> robot.outtake.shootAutoFar()),
                new FollowPathCommand(robot.follower, startToShoot, true),
                new InstantCommand(() -> robot.stopperServo.set(0.47)),
                new RepeatCommand(
                        new AutoShootInAutoFAR()
                ).withTimeout(3000),

                //new WaitCommand(3000),
                //new InstantCommand(() -> robot.outtake.stop()),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(.56))

        );
    }

    public SequentialCommandGroup getDepotBlue() {
        return new SequentialCommandGroup(
                //new WaitCommand(6000),
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.follower.setMaxPower(1)),
                new FollowPathCommand(robot.follower, grabDepotBlue, false).withTimeout(2500),
                //start intake
                new FollowPathCommand(robot.follower, collectDepotBlue, false).withTimeout(1500),
                new WaitCommand(500),
                //stop intake
                new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(1))

        );
    }

    public SequentialCommandGroup shootDepotBlue() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootDepotBlue, true),
                new InstantCommand(() -> robot.stopperServo.set(0.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAutoFAR()
                ).withTimeout(2500),

                //new WaitCommand(3000),
                //new InstantCommand(() -> robot.outtake.stop()),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(0.56))

        );
    }

    public SequentialCommandGroup grabHumanPlayer() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.follower.setMaxPower(.8)),
                new FollowPathCommand(robot.follower, grabHumanPlayer, false).withTimeout(4000),
                //stop intake
                new InstantCommand(() ->robot.intake.stop()),
                new InstantCommand(() -> robot.follower.setMaxPower(1))
        );
    }

    public SequentialCommandGroup shootHumanPlayer() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootHumanPlayer, true),
                new InstantCommand(() -> robot.stopperServo.set(0.47)),
                new WaitCommand(500),
                new RepeatCommand(
                        new AutoShootInAutoFAR()
                ).withTimeout(2500),

                //new WaitCommand(3000),
                //new InstantCommand(() -> robot.outtake.stop()),
                new InstantCommand(() -> robot.intake.stop()),
                new InstantCommand(() -> robot.stopperServo.set(0.56))

        );
    }

    public SequentialCommandGroup shootToPark() {
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, shootToPark, true).withTimeout(3000)
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

        waitForStart();

        if(delayEnabled){
            schedule(
//                    // DO NOT REMOVE: updates follower to follow path
                    new RunCommand(() -> robot.follower.update()),
                    new InstantCommand(() -> timer.reset()),

                    new SequentialCommandGroup(
                            new InstantCommand(),
                            new WaitCommand(15000),
                            startToShoot(),
                            getDepotBlue(),
                            shootDepotBlue(),
                            grabHumanPlayer(),
                            shootHumanPlayer(),
                            grabHumanPlayer(),
                            shootHumanPlayer(),
                            shootToPark()
                    )
            );
        }else{
            schedule(
//                    // DO NOT REMOVE: updates follower to follow path
                    new RunCommand(() -> robot.follower.update()),
                    new InstantCommand(() -> timer.reset()),

                    new SequentialCommandGroup(
                            new InstantCommand(),
                            startToShoot(),
                            getDepotBlue(),
                            shootDepotBlue(),
                            grabHumanPlayer(),
                            shootHumanPlayer(),
                            grabHumanPlayer(),
                            shootHumanPlayer(),
                            grabHumanPlayer(),
                            shootHumanPlayer(),
                            shootToPark()
                    )
            );
        }




    }

    @Override
    public void initialize_loop(){
//
//        if(gamepad1.square){
//            delayEnabled = true;
//        }
//        if(gamepad1.circle){
//            delayEnabled = false;
//        }

        //telemetry.addData("randomization:", randomizationMotif.toString());
        telemetry.addData("Square to ADD delay", "Circle to REMOVE delay");
        telemetry.addData("add delay?", delayEnabled);
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();

        robot.outtake.shootAutoFar();

        telemetry.addData("timer", timer.milliseconds());

        telemetry.addData("followerIsBusy", robot.follower.isBusy());
        telemetry.addData("repeat", test);
        telemetry.addData("shooterReady", shooterReady);
        telemetry.addData("servo pos", robot.stopperServo.get());
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

