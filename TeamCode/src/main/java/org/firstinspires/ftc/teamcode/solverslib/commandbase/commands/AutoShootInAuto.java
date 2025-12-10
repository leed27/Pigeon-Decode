package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;


import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.test;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class AutoShootInAuto extends ParallelCommandGroup {
    public AutoShootInAuto() {
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.hoodServo.set(0.5)),
                new InstantCommand(() -> robot.stopperServo.set(0.56)),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.startNoHood()
                        ),
                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
                        () -> (robot.leftShooter.getVelocity() > 1050)
                ).withTimeout(100)

        );
    }
}
