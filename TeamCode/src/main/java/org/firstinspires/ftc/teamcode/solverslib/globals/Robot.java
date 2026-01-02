package org.firstinspires.ftc.teamcode.solverslib.globals;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.PoseTracker;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.MotorGroup;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.Direction;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Intake;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Lights;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

public class Robot {
    public MotorEx leftFront, leftRear, rightRear, rightFront; //drivetrain wheels

    public MotorEx leftShooter, rightShooter;
    public MotorEx leftIntake, rightIntake;

    //lights go OOOOOO
    public GoBildaPrismDriver prism;
    public MotorGroup launchMotors;
    public Motor.Encoder launchEncoder;

    public ServoEx hoodServo, stopperServo;
    public Servo lightLeft, lightRight;
    public Follower follower;
    public PIDFController controller;
    public PoseTracker poseUpdater;

    /// the next two are for optimizing loop times
    public List<LynxModule> allHubs;
    public LynxModule ControlHub;

    public Intake intake;
    public Outtake outtake;
    public Lights lights;
    public PrismAnimations.Solid solid;

    public PrismAnimations.Snakes snake1, snake2;
    private static Robot instance = new Robot();
    public boolean enabled;

    public static Robot getInstance() {
        if(instance == null){
            instance = new Robot();
        }
        instance.enabled = true;
        return instance;
    }

    /// run only after robot instance has been made
    public void init(HardwareMap hardwareMap) {
        rightFront = new MotorEx(hardwareMap, "rightFront", Motor.GoBILDA.RPM_435);
        leftFront = new MotorEx(hardwareMap, "leftFront", Motor.GoBILDA.RPM_435);
        rightRear = new MotorEx(hardwareMap, "rightRear", Motor.GoBILDA.RPM_435);
        leftRear = new MotorEx(hardwareMap, "leftRear", Motor.GoBILDA.RPM_435);

        leftShooter = new MotorEx(hardwareMap, "shooterLeft");
        rightShooter = new MotorEx(hardwareMap, "shooterRight");

        rightIntake = new MotorEx(hardwareMap, "rightIntake", Motor.GoBILDA.RPM_1150);

        leftIntake = new MotorEx(hardwareMap, "leftIntake", Motor.GoBILDA.RPM_1150);
        leftIntake.setInverted(true);

        hoodServo = new ServoEx(hardwareMap, "hoodServo");
        stopperServo = new ServoEx(hardwareMap, "stopperServo");


        lightLeft = hardwareMap.get(Servo.class, "lightLeft");
        lightRight = hardwareMap.get(Servo.class, "lightRight");

        prism = hardwareMap.get(GoBildaPrismDriver.class, "prism");

        solid = new PrismAnimations.Solid(new Color (225, 30, 0));
        snake1 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, 0.2F, Direction.Forward, new Color (225, 30, 0));
        snake2 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, 0.2F, Direction.Forward, new Color (225, 30, 0));

        solid.setBrightness(100);
        solid.setStartIndex(0);
        solid.setStopIndex(12);

        snake1.setBrightness(100);
        snake1.setStartIndex(0);
        snake1.setStopIndex(5);

        snake2.setBrightness(100);
        snake2.setStartIndex(6);
        snake2.setStopIndex(11);




//
        rightFront.setInverted(true);
        rightRear.setInverted(true);
        leftFront.setInverted(true);
        leftRear.setInverted(true);

        leftShooter.setInverted(true);
        leftShooter.setRunMode(Motor.RunMode.RawPower);
        rightShooter.setRunMode(Motor.RunMode.RawPower);

        leftShooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightShooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        launchMotors = new MotorGroup(
                new MotorEx(hardwareMap, "shooterLeft") //left launch
                        .setCachingTolerance(0.01)
                        .setInverted(true),
                new MotorEx(hardwareMap, "shooterRight") //right launch
                        .setCachingTolerance(0.01)
        );
//
        launchEncoder = new Motor(hardwareMap, "shooterRight").encoder;
        //launchEncoder.setDirection(Motor.Direction.REVERSE);
//
//        slidesEncoder = new Motor(hardwareMap, "left_horizontal").encoder;
//
        follower = Constants.createFollower(hardwareMap);
        controller = new PIDFController(follower.constants.coefficientsHeadingPIDF);


        //poseUpdater = new PoseTracker(hardwareMap);

        //for optimizing loop times
        // Bulk reading enabled!
        // AUTO mode will bulk read by default and will redo and clear cache once the exact same read is done again
        // MANUAL mode will bulk read once per loop but needs to be manually cleared
        // Also in opModes only clear ControlHub cache as it is a hardware write
        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent() && LynxConstants.isEmbeddedSerialNumber(hub.getSerialNumber())) {
                ControlHub = hub;
            }

        }

        intake = new Intake();
        outtake = new Outtake();
        lights = new Lights();

        if(opModeType.equals(OpModeType.TELEOP)) {
            follower.setStartingPose(autoEndPose);
            follower.startTeleopDrive();

        } else{
            //follower.setStartingPose(new Pose(0, 0, 0));
        }
    }

    /// RUN WHATEVER IS IN THE INIT METHODS IN THE SUBSYSTEMS!!
    public void initHasMovement() {
        outtake.init();
        //intake.init();
        //kickServo.setPosition(0.5);
    }
}
