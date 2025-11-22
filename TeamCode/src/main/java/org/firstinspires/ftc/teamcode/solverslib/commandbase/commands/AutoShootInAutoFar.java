package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;


import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.test;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class AutoShootInAutoFar extends ParallelCommandGroup {
    public AutoShootInAutoFar() {
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.outtake.shootAuto()),
                new InstantCommand(() -> robot.hoodServo.set(0.7)),
                new InstantCommand(() -> test++),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.startNoHood()
                        ),
                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
                        () -> (robot.leftShooter.getVelocity() > 1550)
                ).withTimeout(100)

        );
    }
}
