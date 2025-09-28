package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "test")
public class test extends LinearOpMode {

    Servo servo;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "servo");

        waitForStart();
        servo.setPosition(0);
        servo.setPosition(1);

        while(opModeIsActive()){

            double currentPos = servo.getPosition();
            telemetry.addLine();
            telemetry.addData("Current position: ", currentPos);
            telemetry.addData("Current position: ", 0);
            telemetry.addData("Current position: ", 1);

            telemetry.update();

        }

    }
}
