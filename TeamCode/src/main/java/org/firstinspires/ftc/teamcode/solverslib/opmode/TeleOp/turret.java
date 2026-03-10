package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.opModeType;

//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.solverslib.globals.Globals;
import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

//import org.firstinspires.ftc.teamcode.tuning.example.ExampleRobot;

//@Photon
//@Config
@TeleOp
public class turret extends CommandOpMode {

    // D, 0.000_
    // F, 0.000_
    // I, 0
    // maxPowerConstant, 1.0
    // P, 0.00_

    public static double p = 0.007;
    public static double i = 0;
    public static double d = .0008;
    public static double f = 0.0001;

    public static int setPoint = -100;
    public static double maxPowerConstant = 1.0;

    private static final PIDFController singlePIDF = new PIDFController(p,i,d, f);
    private final Robot robot = Robot.getInstance();

    public ElapsedTime timer = new ElapsedTime();

    int motorPos;

    @Override
    public void initialize() {

        opModeType = Globals.OpModeType.TELEOP;

        super.reset();
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot.init(hardwareMap);



        telemetry.addData("encoder position", motorPos);
        telemetry.addData("setPoint", setPoint);
        telemetry.addData("max power", (f * motorPos) + maxPowerConstant);

        super.run();
    }

    @Override
    public void run() {
        super.run();


        timer.reset();

        motorPos = robot.turretMotor.getCurrentPosition();

        singlePIDF.setP(p);
        singlePIDF.setI(i);
        singlePIDF.setD(d);
        singlePIDF.setF(f);

        singlePIDF.setSetPoint(setPoint);

        double maxPower = (f * motorPos) + maxPowerConstant;
        double power = Range.clip(singlePIDF.calculate(motorPos, setPoint), -maxPower, maxPower);

        robot.turretMotor.setPower(power);

        for (LynxModule hub : robot.allHubs) {
            hub.clearBulkCache();
        }

        telemetry.addData("encoder position", motorPos);
        telemetry.addData("setPoint", setPoint);
        telemetry.addData("motorPower", power);
        telemetry.addData("max power", maxPower);
        telemetry.addData("loop time (ms)", timer.milliseconds());

        telemetry.update();
    }
}