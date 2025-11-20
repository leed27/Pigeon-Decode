package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Intake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();

    public void init(){
    }

    public void start(){
        robot.leftIntake.set(1);
        robot.rightIntake.set(1);
    }

    public void stop(){
        robot.leftIntake.set(0);
        robot.rightIntake.set(0);
    }

    public void reverse(){
        robot.leftIntake.set(-1);
        robot.rightIntake.set(-1);
    }






    //periodic runs in a loop
    @Override
    public void periodic(){

    }
}
