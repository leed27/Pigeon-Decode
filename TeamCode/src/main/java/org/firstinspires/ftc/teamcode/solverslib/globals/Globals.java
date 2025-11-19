package org.firstinspires.ftc.teamcode.solverslib.globals;


import com.pedropathing.geometry.Pose;

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
    public static double FAR_SPEED = 0.8;
    public static double CLOSE_SPEED = 0.5;

    public static GoalColor goals;

    public static OpModeType opModeType;

    public static Pose autoEndPose = new Pose(0, 0, Math.toRadians(0));

    //public static PIDFCoefficients FLYWHEEL_PIDF_COEFFICIENTS = new PIDFCoefficients(0.01, 0, 0, 0.00052);
    public static double LAUNCHER_MAX_VELOCITY = 2500; // Ticks/second (NOT MEASURED YET)


}
