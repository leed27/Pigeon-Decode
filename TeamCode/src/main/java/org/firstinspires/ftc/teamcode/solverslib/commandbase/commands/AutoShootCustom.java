package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;



import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class AutoShootCustom extends ParallelCommandGroup {
    public AutoShootCustom(int speed) {
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.outtake.shootCustom(speed)),
                new InstantCommand(() -> robot.hoodServo.set(0.5)),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.startNoHood()
                        ),
                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
                        () -> (robot.leftShooter.getVelocity() > speed-50)
                ).withTimeout(100)

        );
    }
}
