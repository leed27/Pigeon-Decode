package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@Disabled
@TeleOp(name= "test", group="Linear Opmode")

public class test extends LinearOpMode {
    //private DcMotor motor, motor2;
    private Servo kicker, kicker2;


    @Override
    public void runOpMode() {
        kicker = hardwareMap.get(Servo.class, "kickerServo");
        kicker2 = hardwareMap.get(Servo.class, "kicker2Servo");
        kicker2.setDirection(Servo.Direction.REVERSE);

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
        kicker.setPosition(0.82);
        kicker2.setPosition(0.82);

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                if(gamepad1.left_bumper){
                    kicker.setPosition(kicker.getPosition()- 0.0005);
                    kicker2.setPosition(kicker2.getPosition()- 0.0005);
                }
                if(gamepad1.right_bumper){
                    kicker.setPosition(kicker.getPosition()+ 0.0005);
                    kicker2.setPosition(kicker2.getPosition()+ 0.0005);

                }

                if(gamepad1.circleWasPressed()){
                    kicker.setPosition(0.3);
                    kicker2.setPosition(0.3);
                }

                telemetry.addData("eriog", kicker.getPosition());
                telemetry.addData("eriog", kicker2.getPosition());

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
