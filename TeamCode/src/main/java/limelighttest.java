import static solverslib.hardware.Globals.randomizationMotif;
import static solverslib.hardware.Globals.goals;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

import solverslib.hardware.Globals;

@TeleOp(name= "\u2B50 limelight test \u2B50", group="Linear Opmode")

public class limelighttest extends LinearOpMode {

    Limelight3A limelight;


    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!

        limelight.pipelineSwitch(1); // pipleline 1 is our AprilTags pipeline

//        LLResult result = limelight.getLatestResult();
//
//        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults(); // fiducials are special markers (like AprilTags)
//        for (LLResultTypes.FiducialResult fiducial : fiducials) {
//            int id = fiducial.getFiducialId(); // The ID number of the fiducial
//            if(id == 21){
//                randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
//            }else if(id == 22){
//                randomizationMotif = Globals.RandomizationMotif.GREEN_MIDDLE;
//            }else if(id == 23){
//                randomizationMotif = Globals.RandomizationMotif.GREEN_RIGHT;
//            }else{
//                //failsafe
//                randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
//            }
//        }

//        telemetry.addData("randomization:", randomizationMotif.toString());
//        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                LLResult result = limelight.getLatestResult();

                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults(); // fiducials are special markers (like AprilTags)
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    int id = fiducial.getFiducialId();
                    // The ID number of the fiducial
                    try {
                        if(id==20) {
                            goals = Globals.GoalColor.BLUE_GOAL;
                            telemetry.addData("goal color", "blue!");
                        } else if (id == 21) {
                            randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
                            telemetry.addData("randomization:", randomizationMotif.toString());
                            telemetry.update();
                        } else if (id == 22) {
                            randomizationMotif = Globals.RandomizationMotif.GREEN_MIDDLE;
                            telemetry.addData("randomization:", randomizationMotif.toString());
                            telemetry.update();
                        } else if (id == 23) {
                            randomizationMotif = Globals.RandomizationMotif.GREEN_RIGHT;
                            telemetry.addData("randomization :", randomizationMotif.toString());
                            telemetry.update();
                        } else if (id == 24){
                            goals = Globals.GoalColor.RED_GOAL;
                            telemetry.addData("goal color", "red!");
                        }else {
                            //failsafe
                            randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
                            telemetry.addData("FAILSAFE! :", randomizationMotif.toString());
                            telemetry.update();
                        }
                    } catch (NullPointerException e) {
                        randomizationMotif = Globals.RandomizationMotif.GREEN_LEFT;
                        telemetry.addData("NULL! :", randomizationMotif.toString());
                        telemetry.update();
                    }
                }

            }
        }

    }

}
