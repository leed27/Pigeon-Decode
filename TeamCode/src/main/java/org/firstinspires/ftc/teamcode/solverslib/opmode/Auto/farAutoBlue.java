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
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAutoFAR;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Autonomous(name = "HP CYCLE \uD83D\uDD35", group = "auto")
public class farAutoBlue extends CommandOpMode{
    private final Robot robot = Robot.getInstance();
    private ElapsedTime timer;

    //private final ArrayList<PathChain> paths = new ArrayList<>();

    // ALL PATHS
    private final Pose startPose = new Pose(9.5, 8, Math.toRadians(180));
    private final Pose firstIntake = new Pose(48, 95.5, Math.toRadians(290));//e
    private final Pose goBack = new Pose(24, 60, Math.toRadians(180));
    private final Pose secondIntake = new Pose(9.5, 14, Math.toRadians(180));
    /// blue paths
    private PathChain goToIntake, backUp, goToIntake2, goToShoot;

    public void generatePath() {
        // PEDRO VISUALIZER: https://visualizer.pedropathing.com

        // Starting Pose (update this as well):
        //robot.follower.setStartingPose(startPose);

        goToIntake = robot.follower.pathBuilder()
                .addPath(new BezierLine(startPose, firstIntake))
                .setConstantHeadingInterpolation(180)
                .build();

        backUp = robot.follower.pathBuilder()
                .addPath(new BezierLine(firstIntake, goBack))
                .setConstantHeadingInterpolation(180)
                .build();

        goToIntake2 = robot.follower.pathBuilder()
                .addPath(new BezierLine(goBack, secondIntake))
                .setConstantHeadingInterpolation(180)
                .build();

        goToShoot = robot.follower.pathBuilder()
                .addPath(new BezierLine(secondIntake, startPose))
                .setConstantHeadingInterpolation(180)
                .build();



    }

    public SequentialCommandGroup shootPreloads() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(() -> robot.turretMotor.setTargetPosition(0)),
                new InstantCommand(() -> robot.outtake.shootAutoFar()),
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.stopperServo.set(.5)),
                new RepeatCommand(
                        new AutoShootInAutoFAR()
                ).withTimeout(2000),
                new InstantCommand(() -> robot.stopperServo.set(.1))

        );
    }
    public SequentialCommandGroup fullCycle() {
        //DYLAN LETS JS DO IT IN ONE GROUP CUZ KINDA REPITIVE
        return new SequentialCommandGroup(
                new FollowPathCommand(robot.follower, goToIntake, false).withTimeout(3000),
                new FollowPathCommand(robot.follower, backUp, false).withTimeout(1000),
                new FollowPathCommand(robot.follower, goToIntake2, false).withTimeout(3000),
                new FollowPathCommand(robot.follower, goToShoot, false).withTimeout(3000),
                new InstantCommand(() -> robot.outtake.shootAutoFar()),
                new InstantCommand(() -> robot.intake.start()),
                new InstantCommand(() -> robot.stopperServo.set(.5)),
                new RepeatCommand(
                        new AutoShootInAutoFAR()
                ).withTimeout(2000),
                new InstantCommand(() -> robot.stopperServo.set(.1))

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

        robot.stopperServo.set(0.15);

        //robot.turretMotor.stopAndResetEncoder();

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
                       shootPreloads(),

                        fullCycle()
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
        turretEncoder = robot.turretMotor.getCurrentPosition();
        autoEndPose = robot.follower.getPose();
    }
}

