package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import static java.lang.Double.NaN;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Outtake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();
    //private final PIDFController flywheelController = new PIDFController(FLYWHEEL_PIDF_COEFFICIENTS);

    private double targetFlywheelVelocity = 0.0;

    private final InterpLUT launcherVelClose = new InterpLUT();
    private final InterpLUT launcherVelFar = new InterpLUT();//converts from m/s to ticks/s ?

    private final InterpLUT lookUpClose = new InterpLUT();
    private final InterpLUT lookUpFar = new InterpLUT();




    public void init(){

        //DEFYING PHYSICS TABLES :D
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

        //NORMAL TABLES THAT DIDDY APPROVES
        lookUpClose.add(0, 0);
        lookUpClose.add(2.138, 1020);
        lookUpClose.add(2.953, 1120);
        lookUpClose.add(3.301, 1135);
        lookUpClose.add(3.900, 1160);
        lookUpClose.add(4.670, 1210);
        lookUpClose.add(5.15, 1250);
        lookUpClose.add(5.85, 1270);
        lookUpClose.add(6.38, 1280);
        lookUpClose.add(6.7, 1300);
        lookUpClose.add(7.3, 1350);
        lookUpClose.add(8, 1370);
        lookUpClose.add(8.7, 1390);
        lookUpClose.add(9.15, 1420);
        lookUpClose.createLUT();

        lookUpFar.add(0, 0);
        lookUpFar.add(10.5, 1470);
        lookUpFar.add(11.436, 1490);
        lookUpFar.add(12, 1510);
        lookUpFar.add(13, 1540);
        lookUpFar.add(14.06, 1570);
        lookUpFar.add(30, 1800); //when our odom wheels are rly off

        lookUpFar.createLUT();
    }

    public int autoShoot2(){
        double x = robot.follower.getPose().getX();
        double y = robot.follower.getPose().getY();

        double howFar = 0;

        if(goalColor == GoalColor.RED_GOAL){
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
        }else{
            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((-x)/(12)),2));
        }

        if(howFar <= 2 || howFar >= 18){
            return -1;
        }

        if(y < 55){
            robot.hoodServo.set(.7);
            return (int) (lookUpFar.get(howFar));
        }else{
            robot.hoodServo.set(.5);
            if(howFar >= 9.14){
                return -1;
            }
            return (int) (lookUpClose.get(howFar));
        }

    }

//    public double autoAlign(){
//        double x = robot.follower.getPose().getX();
//        double y = robot.follower.getPose().getY();
//        double aimPosX = 0;
//        double aimPosY = 0;
//
//        double angleNeeded = 0;
//
//        if(goalColor == GoalColor.RED_GOAL){
//            if(x>)
//            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
//        }else{
//            howFar = Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((-x)/(12)),2));
//        }
//    }
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


        if(y < 55){
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

        //robot.stopperServo.set(0.56);
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

        //robot.stopperServo.set(0.56);
    }

    public void shootAutoFar(){
        if(robot.leftShooter.getVelocity() > 1530 && robot.rightShooter.getVelocity() > 1530){
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

        //robot.stopperServo.set(0.56);
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
