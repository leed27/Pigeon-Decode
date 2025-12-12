package org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.solverslib.globals.Globals;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Lights extends SubsystemBase {
    private final Robot robot = Robot.getInstance();

    public double RED = 0.28;
    public double ORANGE = 0.33;
    public double YELLOW = 0.39;
    public double SAGE = 0.44;
    public double GREEN = 0.5;
    public double AZURE = 0.555;
    public double BLUE = 0.611;
    public double INDIGO = 0.666;
    public double VIOLET = 0.72;
    public double WHITE = 1;
    public double OFF = 0;


    public void init(){
    }

    public static LightsState lightsState;

    public enum LightsState{
        SHOOTER_VALID,
        SWITCH_SIDE,
        VELOCITY_MET,


    }

    public void updateLights(){
        if(lightsState == LightsState.SHOOTER_VALID){
            shooterValid();
        }

        if(lightsState == LightsState.SWITCH_SIDE){
            switchSide();
        }

        if(lightsState == LightsState.VELOCITY_MET){
            velocityMet();
        }
    }

    public void shooterValid(){
        if(robot.outtake.shootAutoGenerator() == -1){
            robot.lightLeft.setPosition(RED);
            robot.lightRight.setPosition(RED);
        }else{
            robot.lightLeft.setPosition(GREEN);
            robot.lightRight.setPosition(GREEN);
        }
    }

    //no idea if this works
    public void switchSide(){
        new UninterruptibleCommand(
                new InstantCommand(
                        () -> {
                            if(goalColor == Globals.GoalColor.RED_GOAL){
                                robot.lightLeft.setPosition(RED);
                                robot.lightRight.setPosition(RED);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(OFF);
                                robot.lightRight.setPosition(OFF);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(RED);
                                robot.lightRight.setPosition(RED);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(OFF);
                                robot.lightRight.setPosition(OFF);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(RED);
                                robot.lightRight.setPosition(RED);

                            }else{
                                robot.lightLeft.setPosition(BLUE);
                                robot.lightRight.setPosition(BLUE);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(OFF);
                                robot.lightRight.setPosition(OFF);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(BLUE);
                                robot.lightRight.setPosition(BLUE);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(OFF);
                                robot.lightRight.setPosition(OFF);
                                new WaitCommand(200);
                                robot.lightLeft.setPosition(BLUE);
                                robot.lightRight.setPosition(BLUE);

                            }

                            new WaitCommand(1000);
                            lightsState = LightsState.SHOOTER_VALID;
                        }
                )
        );

    }

    public void velocityMet(){
        if(shooterReady){
            robot.lightLeft.setPosition(GREEN);
            robot.lightRight.setPosition(GREEN);
        }else{
            robot.lightLeft.setPosition(RED);
            robot.lightRight.setPosition(RED);
        }
    }








    //periodic runs in a loop
    @Override
    public void periodic(){
        updateLights();
    }
}
