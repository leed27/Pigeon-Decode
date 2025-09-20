package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Globals.*;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

@TeleOp(name = "TeleOp")
public class TeleOpMainLIB extends CommandOpMode {
    public GamepadEx driver;

    public ElapsedTime gameTimer;

    public ElapsedTime elapsedtime;
    private final Robot robot = Robot.getInstance();


    @Override
    public void initialize(){
        opModeType = OpModeType.TELEOP;


        // DO NOT REMOVE! Resetting FTCLib Command Sechduler
        super.reset();

        robot.init(hardwareMap);
        elapsedtime = new ElapsedTime();
        elapsedtime.reset();

        register(robot.intake);

        driver = new GamepadEx(gamepad1);

        /// IF THERE NEEDS TO BE MOVEMENT DURING INIT STAGE, UNCOMMENT
        //robot.initHasMovement();

        /// ALL CONTROLS

        driver.getGamepadButton(GamepadKeys.Button.TRIANGLE).whenPressed(
                new InstantCommand(() -> robot.intake.moveSpindex(1))

        );

        driver.getGamepadButton(GamepadKeys.Button.CIRCLE).whenPressed(
                new InstantCommand(() -> robot.intake.moveSpindex(2))
        );

        driver.getGamepadButton(GamepadKeys.Button.CROSS).whenPressed(
                new InstantCommand(() -> robot.intake.moveSpindex(3))
        );




        super.run();
    }

    @Override
    public void run() {
        // Keep all the has movement init for until when TeleOp starts
        // This is like the init but when the program is actually started
        if (gameTimer == null) {
            robot.initHasMovement();

            gameTimer = new ElapsedTime();
        }

        // DO NOT REMOVE! Runs FTCLib Command Scheudler
        super.run();

        telemetry.addData("Status", "Running");
        telemetry.addData("right_horizontal: ", robot.spindex.getPosition());
        telemetry.addData("loop times", elapsedtime.milliseconds());
        elapsedtime.reset();

        telemetry.update();

        // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual mode
        // Also only clearing the control hub to decrease loop times
        // This means if we start reading both hubs (which we aren't) we need to clear both
        //robot.ControlHub.clearBulkCache();
        for(LynxModule hub : robot.allHubs) {
            hub.clearBulkCache();
        }
    }

//    @Override
//    public void end() {
//        autoEndPose = robot.follower.getPose();
//    }
}
