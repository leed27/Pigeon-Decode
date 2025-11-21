package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;



import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

public class AutoShoot extends ParallelCommandGroup {
    public AutoShoot() {
        Robot robot = Robot.getInstance();
        addCommands(
                new RunCommand(() -> robot.outtake.shootClose()),
                new InstantCommand(() -> robot.hoodServo.set(0.5)),
                new InstantCommand(() -> test++),
                new ConditionalCommand(
                        new InstantCommand(() -> robot.intake.startNoHood()
                        ),
                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
                        () -> (robot.leftShooter.getVelocity() > 1200)
                )

        );
    }
}
