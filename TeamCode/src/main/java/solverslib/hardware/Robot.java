package solverslib.hardware;

import static solverslib.hardware.Globals.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.PoseTracker;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import java.util.List;

import pedroPathing.Constants;

import solverslib.commandbase.Endgame;
import solverslib.commandbase.Intake;
import solverslib.commandbase.Outtake;

public class Robot {

    public MotorEx leftFront, leftRear, rightRear, rightFront; //drivetrain wheels

    public MotorEx right_horizontal,left_horizontal; //horizontal slides

    public MotorEx right_hang, left_hang;

    public ServoImplEx rotate_floor, pinch_floor, flip_floor, right_swing, left_swing, rotate_chamber, pinch_chamber;

    //try out may be cool
    public Motor.Encoder slidesEncoder;

    public Servo light1, light2;

    public Follower follower;
    public PoseTracker poseUpdater;

    /// the next two are for optimizing loop times
    public List<LynxModule> allHubs;
    public LynxModule ControlHub;

    public Intake intake;
    public Outtake outtake;
    public Endgame endgame;

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

        right_horizontal = new MotorEx(hardwareMap, "right_horizontal");
        left_horizontal = new MotorEx(hardwareMap, "left_horizontal");

        rotate_floor = hardwareMap.get(ServoImplEx.class, "rotate_floor");
        pinch_floor = hardwareMap.get(ServoImplEx.class, "pinch_floor");
        flip_floor = hardwareMap.get(ServoImplEx.class, "flip_floor");

        right_swing = hardwareMap.get(ServoImplEx.class, "right_swing");
        left_swing = hardwareMap.get(ServoImplEx.class, "left_swing");

        rotate_chamber = hardwareMap.get(ServoImplEx.class, "rotate_chamber");
        pinch_chamber = hardwareMap.get(ServoImplEx.class, "pinch_chamber");

        right_hang = new MotorEx(hardwareMap, "right_hang");
        left_hang = new MotorEx(hardwareMap, "left_hang");

        light1 = hardwareMap.get(Servo.class, "light1");
        light2 = hardwareMap.get(Servo.class, "light2");

        rightFront.setInverted(true);
        rightRear.setInverted(true);
        leftFront.setInverted(true);
        leftRear.setInverted(true);

        right_horizontal.setInverted(false);
        left_horizontal.setInverted(true);

        right_horizontal.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);
        left_horizontal.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);

        right_hang.setInverted(true);
        left_hang.setInverted(false);

        right_hang.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);
        left_hang.setZeroPowerBehavior(MotorEx.ZeroPowerBehavior.BRAKE);

        slidesEncoder = new Motor(hardwareMap, "left_horizontal").encoder;

        follower = Constants.createFollower(hardwareMap);

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
        endgame = new Endgame();

        if(opModeType.equals(OpModeType.TELEOP)) {
            follower.startTeleopDrive();

            follower.setStartingPose(autoEndPose);
        } else{
            follower.setStartingPose(new Pose(0, 0, 0));
        }
    }

    /// RUN WHATEVER IS IN THE INIT METHODS IN THE SUBSYSTEMS!!
    public void initHasMovement() {
        intake.init();
    }
}
