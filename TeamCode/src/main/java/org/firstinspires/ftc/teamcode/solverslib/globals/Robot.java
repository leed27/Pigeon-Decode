package org.firstinspires.ftc.teamcode.solverslib.globals;

import static org.firstinspires.ftc.teamcode.solverslib.globals.Globals.*;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.PoseTracker;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.MotorGroup;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Lights;
import org.firstinspires.ftc.teamcode.solverslib.commandbase.subsystems.Outtake;

import java.util.List;

public class Robot {
    public MotorEx leftFront, leftRear, rightRear, rightFront; //drivetrain wheels

    public MotorEx leftShooter, rightShooter;
    public DcMotor turretMotor;
    public MotorEx intakeMotor;

    //lights go OOOOOO
    //public GoBildaPrismDriver prism;
    public MotorGroup launchMotors;
    public Motor.Encoder launchEncoder;

    public ServoEx hoodServo, stopperServo;

    public ServoEx kickerServo;
    public ServoEx intakeServo;
    //public CRServo transferServo;

    public Follower follower;
    public static double p = 0.03;
    public static double i = 0;
    public static double d = .0023;
    public static double f = 0.000002;
    public static final PIDFController singlePIDF = new PIDFController(p,i,d, f);
    public PoseTracker poseUpdater;

    /// the next two are for optimizing loop times
    public List<LynxModule> allHubs;
    public LynxModule ControlHub;

    public Intake intake;
    public Outtake outtake;
    //public Lights lights;
    public PrismAnimations.Solid solid, transpo;

    public PrismAnimations.Snakes snake1, snake2;

    public PrismAnimations.SineWave fading, fading2;
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
        rightFront.setInverted(true);
        leftFront = new MotorEx(hardwareMap, "leftFront", Motor.GoBILDA.RPM_435);
        leftFront.setInverted(true);
        rightRear = new MotorEx(hardwareMap, "rightRear", Motor.GoBILDA.RPM_435);
        rightRear.setInverted(true);
        leftRear = new MotorEx(hardwareMap, "leftRear", Motor.GoBILDA.RPM_435);
        leftRear.setInverted(true);

        leftShooter = new MotorEx(hardwareMap, "shooterLeft");
        rightShooter = new MotorEx(hardwareMap, "shooterRight");
        rightShooter.setInverted(true);

        intakeMotor = new MotorEx(hardwareMap, "intake", Motor.GoBILDA.RPM_1150);
        intakeMotor.setInverted(false);

        //turretMotor = new MotorEx(hardwareMap, "turretMotor", Motor.GoBILDA.RPM_1150);
        turretMotor = hardwareMap.get(DcMotor.class, "turretMotor");
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //singlePIDF.setTolerance(1, 10);

        //turretMotor.setMode();

        intakeServo = new ServoEx(hardwareMap, "intakeServo");
        //limit: .75 hits the top
//        transferServo = new CRServo(hardwareMap, "transferServo");
//        transferServo.setInverted(true);

        hoodServo = new ServoEx(hardwareMap, "hoodServo");
        //limit: .5
        stopperServo = new ServoEx(hardwareMap, "stopperServo");

        //kickerServo = new ServoEx(hardwareMap, "kicker");


        //prism = hardwareMap.get(GoBildaPrismDriver.class, "prism");

//        solid = new PrismAnimations.Solid(new Color (225, 30, 0));
//        transpo = new PrismAnimations.Solid(Color.TRANSPARENT);
//        snake1 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, (float) (Math.PI/120.0F), Direction.Forward, new Color (225, 30, 0));
//        snake2 = new PrismAnimations.Snakes(3, 3, 5, Color.TRANSPARENT, (float) (Math.PI/120.0F), Direction.Forward, new Color (225, 30, 0));
//        fading = new PrismAnimations.SineWave(new Color (225, 30, 0),  Color.TRANSPARENT, 6, (float) (Math.PI/36.0F), 0.3F, Direction.Forward);
//        fading2 = new PrismAnimations.SineWave(new Color (225, 30, 0),  Color.TRANSPARENT, 6, (float) (Math.PI/36.0F), 0.3F, Direction.Forward);
//
//        solid.setBrightness(100);
//        solid.setStartIndex(0);
//        solid.setStopIndex(12);
//
//        snake1.setBrightness(100);
//        snake1.setStartIndex(0);
//        snake1.setStopIndex(5);
//
//        snake2.setBrightness(100);
//        snake2.setStartIndex(6);
//        snake2.setStopIndex(11);
//
//        fading.setBrightness(100);
//        fading2.setBrightness(100);




//        leftShooter.setInverted(true);
//        leftShooter.setRunMode(Motor.RunMode.RawPower);
//        rightShooter.setRunMode(Motor.RunMode.RawPower);
//
//        leftShooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
//        rightShooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
//
//        launchMotors = new MotorGroup(
//                new MotorEx(hardwareMap, "shooterLeft") //left launch
//                        .setCachingTolerance(0.01)
//                        .setInverted(true),
//                new MotorEx(hardwareMap, "shooterRight") //right launch
//                        .setCachingTolerance(0.01)
//        );
////
//        launchEncoder = new Motor(hardwareMap, "shooterRight").encoder;
        //launchEncoder.setDirection(Motor.Direction.REVERSE);
////
        follower = Constants.createFollower(hardwareMap);
//        controller = new PIDFController(follower.constants.coefficientsHeadingPIDF);



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
        //lights = new Lights();

        if(opModeType.equals(OpModeType.TELEOP)) {
            follower.setStartingPose(autoEndPose);
            follower.startTeleopDrive();

        }
//        else{
//            follower.setStartingPose(new Pose(0, 0, 0));
//        }
    }

    /// RUN WHATEVER IS IN THE INIT METHODS IN THE SUBSYSTEMS!!
    public void initHasMovement() {
        outtake.init();
        //intake.init();
        //kickServo.setPosition(0.5);
    }
}
