package solverslib.hardware;

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

    public static GoalColor goals;

    public static OpModeType opModeType;

    public static Pose autoEndPose = new Pose(0, 0, Math.toRadians(0));


}
