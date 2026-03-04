package org.firstinspires.ftc.teamcode.solverslib.opmode.Auto;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.follower.Follower;
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
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.AutoShootInAuto;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.commands.RapidShoot;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class closeBlue15 {
        public PathChain startToShoot;
        public PathChain grabMiddle;
        public PathChain shootMiddle;
        public PathChain openGate;
        public PathChain shootGate;
        public PathChain getTop;
        public PathChain shootTop;
        public PathChain getBottom;
        public PathChain shootBottom;

        public void generatePaths(Follower follower) {
            startToShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(31.539, 134.558),

                                    new Pose(48.000, 95.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(282))

                    .build();

            grabMiddle = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(48.000, 95.000),
                                    new Pose(58.564, 59.436),
                                    new Pose(24.000, 60.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            shootMiddle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(24.000, 60.000),

                                    new Pose(48.000, 95.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            openGate = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(48.000, 95.000),
                                    new Pose(32.579, 42.470),
                                    new Pose(9.873, 59.851)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            shootGate = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(9.873, 59.851),

                                    new Pose(57.000, 84.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            getTop = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.000, 84.000),

                                    new Pose(23.753, 83.787)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            shootTop = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(23.753, 83.787),

                                    new Pose(57.006, 84.022)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            getBottom = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(57.006, 84.022),
                                    new Pose(68.826, 35.326),
                                    new Pose(24.493, 35.210)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            shootBottom = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(24.493, 35.210),

                                    new Pose(59.000, 102.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();
        }
    }

