package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

import java.util.Arrays;
import java.util.List;

public class Outtake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();
    //private final PIDFController flywheelController = new PIDFController(FLYWHEEL_PIDF_COEFFICIENTS);

    private double targetHoodAngle = 90-15;//MIN_HOOD_ANGLE;
    private double targetFlywheelVelocity = 0.0;

    private final InterpLUT launcherVel = new InterpLUT(); //converts from m/s to ticks/s ?

    public void init(){
        launcherVel.add(-0.01, 0.0);
        launcherVel.add(0.0, 0.0);
        launcherVel.add(4.29, 1267.0);
        launcherVel.add(4.76, 1367.0);
        launcherVel.add(5.22, 1500.0);
        launcherVel.add(5.65, 1667.0);
        launcherVel.add(6.06, 1810.0);
        launcherVel.add(6.48, 2000.0);
        launcherVel.add(10.0, 2100.0);
        launcherVel.createLUT();
        //flywheelController.setTolerance(41);
    }

    public void shootClose(){
        if(robot.leftShooter.getVelocity() > 1250 && robot.rightShooter.getVelocity() > 1250){
            robot.leftShooter.setVelocity(robot.leftShooter.getVelocity());
            robot.rightShooter.setVelocity(robot.rightShooter.getVelocity());
            shooterReady = true;
        }else{
//            robot.leftShooter.setVelocity(1300);
//            robot.rightShooter.setVelocity(1300);
            robot.leftShooter.set(1);
            robot.rightShooter.set(1);
            shooterReady = false;
        }
    }

    public void stop(){
        robot.leftShooter.setVelocity(0);
        robot.rightShooter.setVelocity(0);
    }



    //pidf stuff
//    public double getFlywheelTarget() {
//        return flywheelController.getSetPoint();
//    }
//
//    public void setFlywheel(double vel, boolean setActiveControl) {
//        flywheelController.setSetPoint(Math.min(launcherVel.get(vel), LAUNCHER_MAX_VELOCITY));
//        targetFlywheelVelocity = vel;
//        //activeControl = setActiveControl;
//    }
//    private void updateFlywheel() {
//        //if (activeControl) {
//        if (getFlywheelTarget() == 0) {
//            robot.launchMotors.set(0);
//        }else{
//            flywheelController.setF(FLYWHEEL_PIDF_COEFFICIENTS.f / (12 / 12));
//            robot.launchMotors.set(
//                    flywheelController.calculate(robot.launchEncoder.getCorrectedVelocity())
//            );
//        }
//
//        //} else {
//
////            else {
////                robot.launchMotors.set(LAUNCHER_DEFAULT_ON_SPEED);
////            }
//        //}
//        //robot.profiler.end("Launcher Update");
//    }
//
//    public boolean shooterReady(){
//        return flywheelController.atSetPoint();
//    }




    //periodic runs in a loop
    @Override
    public void periodic(){
        //updateFlywheel();
    }
}
