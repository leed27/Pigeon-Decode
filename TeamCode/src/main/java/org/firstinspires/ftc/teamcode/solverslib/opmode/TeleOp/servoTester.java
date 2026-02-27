package org.firstinspires.ftc.teamcode.solverslib.opmode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@Disabled
@TeleOp(name= "servoTester", group="Linear Opmode")

public class servoTester extends LinearOpMode {
    //private DcMotor motor, motor2;
    private Servo moveAbout;

    private double servoTracker = 0;

        /* 1 -> shoots up, 0.3 -> shoots out */

    @Override
    public void runOpMode() {
        moveAbout = hardwareMap.get(Servo.class, "bob");

//        motor2 = hardwareMap.get(DcMotor.class, "shooter2");

//        lightLeft = hardwareMap.get(Servo.class, "lightLeft");
//        lightRight = hardwareMap.get(Servo.class, "lightRight");
//
//
//        lightLeft.setPosition(0.3);
//        lightRight.setPosition(0.3);
        //motor.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                if(gamepad1.left_bumper){
                    moveAbout.setPosition(0);
                    servoTracker = 0;
                }
                if(gamepad1.right_bumper){
                    moveAbout.setPosition(1);
                    servoTracker = 1;

                }

                if(gamepad1.circleWasPressed()){
                    servoTracker += 0.05;
                    moveAbout.setPosition(servoTracker);
                }

                if(gamepad1.squareWasPressed()){
                    servoTracker -= 0.05;
                    moveAbout.setPosition(servoTracker);
                }

                telemetry.addData("eriog", moveAbout.getPosition());

                telemetry.update();

//                lightLeft.setPosition(0.3);
//                lightRight.setPosition(0.3);
//                if(gamepad1.square){
//                    motor.setPower(1);
//                }else if(gamepad1.triangle){
//                    motor2.setPower(1);
//                }else if(gamepad1.left_bumper){
//                    motor.setPower(1);
//                    motor2.setPower(1);
//                }else if(gamepad1.right_bumper){
//                    motor.setPower(0.3);
//                    motor2.setPower(0.3);
//                }
//
//                else{
//                    motor.setPower(0);
//                    motor2.setPower(0);
//                }
            }
        }

    }

}
