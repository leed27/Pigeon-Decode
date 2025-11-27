package org.firstinspires.ftc.teamcode.solverslib.globals;


import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Globals {
    public enum OpModeType {
        AUTO,
        TELEOP
    }

    public enum RandomizationMotif {
        GREEN_LEFT,
        GREEN_MIDDLE,
        GREEN_RIGHT
    }

    public enum GoalColor{
        BLUE_GOAL,
        RED_GOAL
    }


    public static RandomizationMotif randomizationMotif;
    public static boolean shooterReady = false;
    public static double test = 0;
    public static double xPos = 0;
    public static double yPos = 0;
    public static GoalColor goalColor;

    public static OpModeType opModeType;

    public static Pose autoEndPose = new Pose(15, 32, Math.toRadians(0)); //RIGHT ABOVE THE RED TAPE HP ZONE TAPE FACING BLUE

    public static Pose redParkPose = new Pose(38.5,33, Math.toRadians(90));
    public static Pose blueParkPose = redParkPose.mirror();

    public static Pose blueShootClose = new Pose(51,96, Math.toRadians(135));

    public static PIDFCoefficients FLYWHEEL_PIDF_COEFFICIENTS = new PIDFCoefficients(0.01, 0, 0, 0.00052);
    public static double LAUNCHER_MAX_VELOCITY = 2500; // Ticks/second (NOT MEASURED YET)


}
