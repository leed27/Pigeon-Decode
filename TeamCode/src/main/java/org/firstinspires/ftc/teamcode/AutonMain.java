package org.firstinspires.ftc.teamcode;

import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.solverslib.globals.Robot;

@Autonomous(name="dimaaaaa",group="")
public class AutonMain extends LinearOpMode {
    public ElapsedTime timer = new ElapsedTime();
    @Override
    public void runOpMode() {
        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx leftRear = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx rightRear = hardwareMap.get(DcMotorEx.class,"rightRear");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        double pwr = 1;

        leftFront.setPower(pwr);
        rightFront.setPower(pwr);
        leftRear.setPower(pwr);
        rightRear.setPower(pwr);
        /*timer.reset();
        while (timer.milliseconds() < 250) { idle(); }
         */
        sleep(250);
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }
}
