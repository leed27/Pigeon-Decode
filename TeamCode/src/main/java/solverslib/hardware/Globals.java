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

    public static RandomizationMotif randomizationMotif;

    public static OpModeType opModeType;

    public static Pose autoEndPose = new Pose(0, 0, Math.toRadians(0));


}
