package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class AutoShoot extends ParallelCommandGroup {
    public AutoShoot() {
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.outtake.shootClose()),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.start()
                        ),
                        new InstantCommand(() -> robot.intake.stop()),
                        () -> (robot.leftShooter.getVelocity() > 1000)
                )

        );
    }
}
