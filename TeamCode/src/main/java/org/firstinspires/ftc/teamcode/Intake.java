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
        robot.hoodServo.set(0.7);
        robot.leftShooter.set(-0.5);
        robot.rightShooter.set(-0.5);
    }

    public void startNoHood(){
        robot.leftIntake.set(1);
        robot.rightIntake.set(1);
    }


    public void stop(){
        robot.leftIntake.set(0);
        robot.rightIntake.set(0);
        robot.leftShooter.set(0);
        robot.rightShooter.set(0);
    }

    public void stopExceptShooter(){
        robot.leftIntake.set(0);
        robot.rightIntake.set(0);
    }

    public void reverse(){
        robot.leftIntake.set(-1);
        robot.rightIntake.set(-1);
        robot.leftShooter.set(-0.5);
        robot.rightShooter.set(-0.5);
    }






    //periodic runs in a loop
    @Override
    public void periodic(){

    }
}
