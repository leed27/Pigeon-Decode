package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;


import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class AutoShootInAutoFAR extends ParallelCommandGroup{
    public AutoShootInAutoFAR() {
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.outtake.shootAutoFar()),
                new InstantCommand(() -> robot.hoodServo.set(0.7)),
                //new InstantCommand(() -> robot.stopperServo.set(0.47)),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.startCustom(0.8)
                        ),
                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
                        () -> (robot.leftShooter.getVelocity() > 1510)
                ).withTimeout(100)

        );
    }

}
