package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

public class Intake extends SubsystemBase {
    private final Robot robot = Robot.getInstance();

    public void init(){
    }

    public void moveSpindex(int slot){
        if(slot == 1){
            robot.spindex.setPosition(0.5);
        }else if(slot == 2){
            robot.spindex.setPosition(0.25);
        }else{
            robot.spindex.setPosition(0);
        }
    }


    //periodic runs in a loop
    @Override
    public void periodic(){

    }
}
