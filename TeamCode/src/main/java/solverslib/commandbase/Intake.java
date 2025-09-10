package solverslib.commandbase;

import static solverslib.hardware.Globals.*;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;

import solverslib.hardware.Robot;

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
