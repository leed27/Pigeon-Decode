
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name= "\u2B50 AAAAAAAAAA \u2B50", group="Linear Opmode")
public class temporaryClass extends LinearOpMode {

    // Declare OpMode members.
    //This was made by Adhithya Yuvaraj XD

    //lets instanitate the 6 motors!
    DcMotor flywheel1, flywheel2;

    @Override
    public void runOpMode() throws InterruptedException {

        //set the motors to the postions on CHUB + UHUB
        flywheel1 = hardwareMap.get(DcMotor.class, "flyhweel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");

        //UNO REVERSE THOSE MOTORS
        /*
        frontleft.setDirection(DcMotorSimple.Direction.REVERSE);
        backleft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorright.setDirection(DcMotor.Direction.REVERSE);
        */

        //INIT PART DONE!
        waitForStart();


        //THIS RUNS ON START!
        while(opModeIsActive()){

            //Circle, Cross, Triangle, Square Stuff
            if(gamepad1.circleWasPressed()){
                flywheel1.setPower(0.03);
            }else if(gamepad1.squareWasPressed()){
                flywheel2.setPower(0.03);
            }else if(gamepad1.triangleWasPressed()){
                flywheel1.setPower(0);
                flywheel2.setPower(0);
            }

            //update update update
            telemetry.update();
        }
    }



}