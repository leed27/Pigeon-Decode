package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

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
    private final InterpLUT lookup = new InterpLUT();


    private final InterpLUT turretAngle = new InterpLUT();




    public void init(){
        //INPUT: angle, OUTPUT: motor position
//        turretAngle.add(-130, -129);
//        turretAngle.add(-90, -92);
//        turretAngle.add(-45, -50);
//        turretAngle.add(0, 0);
//        turretAngle.add(45, 50);
//        turretAngle.add(90, 92);
//        turretAngle.add(130, 129);
//        turretAngle.createLUT();

        lookup.add(0,0);
        lookup.add(4.804, 1000);
        lookup.add(5.138, 1020);
        lookup.add(5.953, 1070);
        lookup.add(6.301, 1085);
        lookup.add(6.900, 1110);
        lookup.createLUT();


        //DEFYING PHYSICS TABLES :D
//        launcherVelClose.add(0, 0);
//        launcherVelClose.add(25.878, 1320);
//        launcherVelClose.add(26.13, 1290);
//        launcherVelClose.add(27.25, 1220);
//        launcherVelClose.add(29, 1200);
//        launcherVelClose.add(31.7, 1180);
//        launcherVelClose.add(41.2, 1155);
//        launcherVelClose.add(53.2, 1140);
//        launcherVelClose.add(71.5, 1170);
//        launcherVelClose.add(91.5, 1100);
//        launcherVelClose.createLUT();
//
//        launcherVelFar.add(0, 0);
//        launcherVelFar.add(22.9, 1500);
//        launcherVelFar.add(24, 1570);
//        launcherVelFar.add(25, 1640);
//        launcherVelFar.add(26, 1720);
//        launcherVelFar.createLUT();

        //NORMAL TABLES THAT DIDDY APPROVES
        lookUpClose.add(0, 0);
        lookUpClose.add(4.804, 1110);
        lookUpClose.add(5.138, 1130);
        lookUpClose.add(5.953, 1180);
        lookUpClose.add(6.301, 1195);
        lookUpClose.add(6.900, 1210);
        lookUpClose.add(7.670, 1270);
        lookUpClose.add(8.15, 1310);
        lookUpClose.add(8.85, 1330);
        lookUpClose.add(9.38, 1340);
        lookUpClose.add(9.7, 1360);
        lookUpClose.add(10.3, 1480);
        lookUpClose.add(11, 1700);
        lookUpClose.add(11.7, 1800);
        lookUpClose.add(12.15, 1900);
        lookUpClose.add(13, 2000);
        lookUpClose.createLUT();

//        lookUpFar.add(0, 0);
//        lookUpFar.add(10.5, 1300);
//        lookUpFar.add(11.436, 1340);
//        lookUpFar.add(12, 1360);
//        lookUpFar.add(13, 1390);
//        lookUpFar.add(14.06, 1420);
//        lookUpFar.add(30, 1800); //when our odom wheels are rly off

        //lookUpFar.createLUT();
    }


    public void rapidShooting(int adjustSpeed){
        double howFar = distance();
        int speed = autoShoot2();
        boolean startIntake = false;
        double targetHeading = autoAlign();
        int overShoot;

        if(howFar < 5){
            overShoot = 20;
        }else if(howFar < 7){
            overShoot = 30;
        }else if(howFar < 10){
            overShoot = 50;
        }else if(howFar < 13){
            overShoot = 0;
        }else{
            overShoot = 0;
        }


        shootCustom(speed + adjustSpeed + overShoot);
        robot.stopperServo.set(.47);

        /// ONLY START THE INTAKE ONCE THE SHOOTER VELOCITY IS MET AND ROBOT IS WITHIN 5 DEGREES OF TARGET ANGLE AND NOT BUSY
        if(robot.leftShooter.getVelocity() > speed + adjustSpeed + 10
                && Math.abs(robot.follower.getPose().getHeading() - targetHeading) < Math.toRadians(5)
                ){
//            if(howFar > 10){
//                new InstantCommand(() -> new WaitCommand(500));
//            }
            startIntake = true;
            //robot.intake.startNoHood();
        }

        if(startIntake){
            robot.intake.startLower();
        }else{
            robot.intake.stopExceptShooter();
        }

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

        if(howFar <= 2 || howFar >= 13){
            return -1;
        }

        if(y < 55){
            //robot.hoodServo.set(.7);
            return (int) (lookUpClose.get(howFar));
        }else{
            //robot.hoodServo.set(.5);
            if(howFar > 10){
                return -1;
            }
            return (int) (lookUpClose.get(howFar));
        }

    }

    public double distance(){
        double x = robot.follower.getPose().getX();
        double y = robot.follower.getPose().getY();
        if(goalColor == GoalColor.RED_GOAL){
            return Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((144-x)/(12)),2));
        }else{
            return Math.sqrt(Math.pow(((144-y)/(12)), 2) + Math.pow(((-x)/(12)),2));
        }
    }
//




    public int autoAlign(){

            double x = robot.follower.getPose().getX();
            double y = robot.follower.getPose().getY();
            double botAngle = Math.toDegrees(robot.follower.getHeading());

            double aimPosX;
            double aimPosY;

            if(goalColor == GoalColor.BLUE_GOAL){
                aimPosX = 0;
                aimPosY = 144;
            }else{
                aimPosX = 144;
                aimPosY = 144;
            }

            double aimAngle = Math.toDegrees(Math.atan2(aimPosY - y, aimPosX - x));

            if(aimAngle < 0) aimAngle += 360;   // ensure 0–360

            double diff = aimAngle - botAngle;

            if(diff > 180) diff -= 360;
            if(diff < -180) diff += 360;

            return (int) diff;





        /*
        if(goalColor == GoalColor.BLUE_GOAL){
            if((x+y)>= 115 && (x+y) <= 170 && y>=120){
                aimPosX = 0;
                aimPosY = 144;
            }else if((x+y) > 170){
                aimPosX = 0;
                aimPosY = 140;
            }else{
                aimPosX = 1;
                aimPosY = 144;
            }
        }else{
            if((x-y)>= -40 && (x-y) <= 29){
                aimPosX = 142;
                aimPosY = 144;
            }else if((x-y) > 29){
                aimPosX = 135;
                aimPosY = 144;
            }else{
                aimPosX = 144;
                aimPosY = 137;
            }
        }*/
        /*double angle;
        if(goalColor == GoalColor.BLUE_GOAL) {
            angle = Math.toDegrees(Math.atan2((144 - robot.follower.getPose().getY()), -robot.follower.getPose().getX())) + 180;
        } else {
            angle = Math.toDegrees(Math.atan2((144 - robot.follower.getPose().getY()), 144 - robot.follower.getPose().getX())) + 180;

        }
        //double botAngle = Math.toDegrees(robot.follower.getHeading()) + 180;

        double finalAngle;
        if(botAngle >= 180){
            finalAngle = botAngle - angle;
        }else {
            finalAngle = angle - botAngle;
        }
        if(finalAngle > 180){
            finalAngle -= 360;
        }*/

        //double angle = Math.atan2((aimPosY-robot.follower.getPose().getY()), aimPosX-robot.follower.getPose().getX());
//        double angleDisplacement = Math.toDegrees(robot.follower.getPose().getHeading()) - Math.toDegrees(angle);
//        if(angleDisplacement > 130 || angleDisplacement < -130){
//            return -200;
//        }

        //return (int) 45;//(finalAngle*-1);
    }
    public int shootAutoGenerator(){
        /*
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
        */
        return 25;

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
        if(robot.leftShooter.getVelocity() > 1070 && robot.rightShooter.getVelocity() > 1070){
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
        if(robot.leftShooter.getVelocity() > 1600 && robot.rightShooter.getVelocity() > 1600){
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
        if(robot.leftShooter.getVelocity() > speed/* && robot.rightShooter.getVelocity() > speed*/){
            robot.leftShooter.setVelocity(robot.leftShooter.getVelocity());
            robot.rightShooter.setVelocity(robot.rightShooter.getVelocity());
            //shooterReady = true;
        }else{
//            robot.leftShooter.setVelocity(1300);
//            robot.rightShooter.setVelocity(1300);
            robot.leftShooter.set(1);
            robot.rightShooter.set(1);
            //shooterReady = false;
        }

        if(robot.leftShooter.getVelocity() > speed-20 && robot.rightShooter.getVelocity() > speed-20){
            shooterReady = true;
        }else{
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
