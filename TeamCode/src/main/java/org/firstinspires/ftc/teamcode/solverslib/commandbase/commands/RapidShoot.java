package org.firstinspires.ftc.teamcode.solverslib.commandbase.commands;



import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class RapidShoot extends ParallelCommandGroup {
    public RapidShoot() {

//        robot.outtake.shootCustom(speed +(adjustSpeed)+30);
//        robot.stopperServo.set(.47);
//        /// ONLY START THE INTAKE ONCE THE SHOOTER VELOCITY IS MET AND ROBOT IS WITHIN 5 DEGREES OF TARGET ANGLE AND NOT BUSY
//        if(robot.leftShooter.getVelocity() > speed +adjustSpeed+10 && Math.abs(robot.follower.getPose().getHeading() - targetHeading) < Math.toRadians(5) && !robot.follower.isBusy()){
//            startIntake = true;
//            //robot.intake.startNoHood();
//        }
//
//        if(startIntake){
//            robot.intake.startNoHood();
//        }else{
//            robot.intake.stopExceptShooter();
//        }
        Robot robot = Robot.getInstance();
        addCommands(
                new InstantCommand(() -> robot.outtake.shootClose()),
                new InstantCommand(() -> robot.hoodServo.set(0.5)),
                new InstantCommand(() -> robot.intake.start())
//                new ConditionalCommand(
//                        new InstantCommand(() -> robot.intake.startCustom(0.8)
//                        ),
//                        new InstantCommand(() -> robot.intake.stopExceptShooter()),
//                        () -> (robot.leftShooter.getVelocity() > 1200)
//                ).withTimeout(100)

        );
    }
}
