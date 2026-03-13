package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Intake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();

    public void init(){
    }

    public void start(){
        robot.intakeServo.set(0.38);
        robot.intakeMotor.set(1);
        //robot.transferServo.set(-1);
    }

    public void startCustom(double speed){
        robot.intakeMotor.set(speed);
    }

    public void startLower(){
        robot.intakeServo.set(0.45);
        robot.intakeMotor.set(1);
    }

    public void stop(){
        robot.intakeServo.set(0.45);
        robot.intakeMotor.set(0);
    }

    public void stopUp(){
        robot.intakeServo.set(0.75);
        robot.intakeMotor.set(0);
    }

    public void stopExceptShooter(){
        robot.intakeMotor.set(0);
    }

    public void reverse(){
        robot.intakeServo.set(0.15);
        robot.intakeMotor.set(-1);
    }






    //periodic runs in a loop
    @Override
    public void periodic(){

    }
}
