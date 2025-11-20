package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name= "test", group="Linear Opmode")

public class test extends LinearOpMode {
    private DcMotor motor, motor2;
    private Servo lightLeft, lightRight;


    @Override
    public void runOpMode() {
//        motor = hardwareMap.get(DcMotor.class, "shooter");
//        motor2 = hardwareMap.get(DcMotor.class, "shooter2");

        lightLeft = hardwareMap.get(Servo.class, "lightLeft");
        lightRight = hardwareMap.get(Servo.class, "lightRight");


        lightLeft.setPosition(0.3);
        lightRight.setPosition(0.3);
        //motor.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                lightLeft.setPosition(0.3);
                lightRight.setPosition(0.3);
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
