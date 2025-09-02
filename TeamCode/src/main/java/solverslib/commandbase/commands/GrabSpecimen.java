package solverslib.commandbase.commands;

import static solverslib.hardware.Globals.*;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import solverslib.hardware.Robot;

public class GrabSpecimen extends SequentialCommandGroup {
    public GrabSpecimen(Robot robot){
        addCommands(
                new InstantCommand(() -> robot.outtake.closeClaw()),
                new WaitCommand(150),
                new InstantCommand(() -> robot.outtake.armUp()),
                new WaitCommand(200),
                new InstantCommand(() -> robot.outtake.rotateClaw(OUTTAKE_ROTATED))
        );
    }
}
