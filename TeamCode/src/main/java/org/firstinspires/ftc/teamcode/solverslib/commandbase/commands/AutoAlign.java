package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;
public class AutoAlign extends ParallelCommandGroup{

    public AutoAlign(boolean isBlue) {
        //needs to be here for some reason
        Robot robot = Robot.getInstance();

        //get the pose of the robot
        Pose pose = follower.getPose();

        //set the x and y values
        double x = pose.getX();
        double y = pose.getY();
        double newAngle;

        if(!isBlue){
            //make the angle if it is RED
            newAngle = Math.toDegrees(Math.atan2((144-y), (144-x)));
        }else{
            //make the angle if it is BLUE
            newAngle = Math.toDegrees(Math.atan2((144-y), -x) + 90);
        }

        addCommands(
                new InstantCommand(() -> robot.follower.setPose(new Pose(x, y, Math.toRadians(newAngle))))
                );
    }

}
