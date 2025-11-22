package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;
public class AutoSpeedDecider extends ParallelCommandGroup{
 //DONT USE DONT USE DONT USE DONT USE
    public AutoSpeedDecider(boolean isBlue, Servo servo){
        //needs to be here for some reason
        Robot robot = Robot.getInstance();

        //get the pose of the robot
        Pose pose = follower.getPose();

        //set the x and y values
        double x = pose.getX();
        double y = pose.getY();
        double heading = pose.getHeading();
        servo.setPosition(0.7);
        double currentAngle = 75;
        double newAngle;
        double howFar;
        double velocityNeeded;
        final double gravityInFeet = 32.174;
        double heightNeeded = 3.75;

        if(!isBlue){
            //make the angle if it is RED
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
            velocityNeeded = Math.sqrt((Math.pow(howFar, 2) * gravityInFeet)/(2*Math.pow(Math.cos(heading),2)*(x*Math.tan(heading)-heightNeeded)));
        }else{
            //make the angle if it is BLUE
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((x)/(12)),2));
            velocityNeeded = Math.sqrt((Math.pow(howFar, 2) * gravityInFeet)/(2*Math.pow(Math.cos(heading),2)*(x*Math.tan(heading)-heightNeeded)));
        }

        addCommands(
                new InstantCommand(() -> robot.follower.setPose(new Pose(x, y, Math.toRadians(2))))
        );
    }




}
