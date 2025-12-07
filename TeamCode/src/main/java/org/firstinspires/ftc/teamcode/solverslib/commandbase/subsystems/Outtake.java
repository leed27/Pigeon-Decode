package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import static java.lang.Double.NaN;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Outtake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();
    //private final PIDFController flywheelController = new PIDFController(FLYWHEEL_PIDF_COEFFICIENTS);

    private double targetHoodAngle = 90-15;//MIN_HOOD_ANGLE;
    private double targetFlywheelVelocity = 0.0;

    private final InterpLUT launcherVelClose = new InterpLUT();
    private final InterpLUT launcherVelFar = new InterpLUT();//converts from m/s to ticks/s ?

    private final InterpLUT lookUpClose = new InterpLUT();
    private final InterpLUT lookUpFar = new InterpLUT();




    public void init(){
        launcherVelClose.add(0, 0);
        launcherVelClose.add(25.878, 1320);
        launcherVelClose.add(26.13, 1290);
        launcherVelClose.add(27.25, 1220);
        launcherVelClose.add(29, 1200);
        launcherVelClose.add(31.7, 1180);
        launcherVelClose.add(41.2, 1155);
        launcherVelClose.add(53.2, 1140);
        launcherVelClose.add(71.5, 1170);
        launcherVelClose.add(91.5, 1100);
        launcherVelClose.createLUT();

        launcherVelFar.add(0, 0);
        launcherVelFar.add(22.9, 1500);
        launcherVelFar.add(24, 1570);
        launcherVelFar.add(25, 1640);
        launcherVelFar.add(26, 1720);
        launcherVelFar.createLUT();

        lookUpClose.add(0, 0);
        lookUpClose.add(4, 1100);
        lookUpClose.add(4.25, 1140);
        lookUpClose.add(4.5, 1170);
        lookUpClose.add(5, 1200);
        lookUpClose.add(8, 1300);
        lookUpClose.add(10, 1350);
        lookUpClose.add(13, 1400);
    }

    public int autoShoot2(int correctionTicks){
        double x = robot.follower.getPose().getX();
        double y = robot.follower.getPose().getY();


        double howFar = 0;

        if(goalColor == GoalColor.RED_GOAL){
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
        }else{
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((-x)/(12)),2));
        }

        if(howFar <= 4.5){
            return -1;
        }

        if(y >= 63){
            robot.hoodServo.set(.5);
            return (int) (lookUpClose.get(howFar));
        }else{
            robot.hoodServo.set(.7);
            return (int) (lookUpFar.get(howFar));
        }

    }
    public int shootAutoGenerator(){
        double x = robot.follower.getPose().getX();
        double y = robot.follower.getPose().getY();
        final double height = 2.6;
        double howFar = 0;
        if(goalColor == GoalColor.RED_GOAL){
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
        }else{
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((-x)/(12)),2));
        }

        if(howFar <= 3 || howFar >= 13){
            return -1;
        }

        if(howFar > 9.5){
            robot.hoodServo.set(.7);
            double hoodAngle = Math.toRadians(40);
            double newSpeedInFeet = (howFar*Math.sqrt(16.1))/(Math.cos(hoodAngle) * Math.sqrt(howFar*Math.tan(hoodAngle)-height));
            test = newSpeedInFeet;
            if(newSpeedInFeet > 26 || Double.isNaN(newSpeedInFeet)){
                return -1;
            }
            return (int) (launcherVelFar.get(newSpeedInFeet));
        }else{
            robot.hoodServo.set(.5);
            double hoodAngle = Math.toRadians(30);
            double newSpeedInFeet = (howFar*Math.sqrt(16.1))/(Math.cos(hoodAngle) * Math.sqrt(howFar*Math.tan(hoodAngle)-height));
            test = newSpeedInFeet;
            if(newSpeedInFeet > 50 || Double.isNaN(newSpeedInFeet)){
                return -1;
            }
            return (int) (launcherVelClose.get(newSpeedInFeet));
        }

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

    public void shootAuto(){
        if(robot.leftShooter.getVelocity() > 1100 && robot.rightShooter.getVelocity() > 1100){
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

    public void shootAutoFar(){
        if(robot.leftShooter.getVelocity() > 1430 && robot.rightShooter.getVelocity() > 1430){
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

    public void shootCustom(int speed){
        if(robot.leftShooter.getVelocity() > speed && robot.rightShooter.getVelocity() > speed){
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

        robot.stopperServo.set(0.56);

//        if(robot.leftShooter.getVelocity() > speed-60){
//            robot.stopperServo.set(0.56);
//        }else{
//            robot.stopperServo.set(0.7);
//        }
    }

    public void reverse(){
        robot.leftShooter.set(-1);
        robot.rightShooter.set(-1);
    }


//    public void reverseShoot(){
//        if(robot.leftShooter.getVelocity() > -1100 && robot.rightShooter.getVelocity() > -1100){
//            robot.leftShooter.setVelocity(robot.leftShooter.getVelocity());
//            robot.rightShooter.setVelocity(robot.rightShooter.getVelocity());
//            shooterReady = true;
//        }else{
////            robot.leftShooter.setVelocity(1300);
////            robot.rightShooter.setVelocity(1300);
//            robot.leftShooter.set(-1);
//            robot.rightShooter.set(-1);
//            shooterReady = false;
//        }
//    }

    public void stop(){
        robot.leftShooter.setVelocity(0);
        robot.rightShooter.setVelocity(0);
    }






    //periodic runs in a loop
    @Override
    public void periodic(){
        //updateFlywheel();
    }
}
