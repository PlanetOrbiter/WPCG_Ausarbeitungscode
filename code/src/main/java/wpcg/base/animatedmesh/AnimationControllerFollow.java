package wpcg.base.animatedmesh;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class AnimationControllerFollow implements AnimationController
{
    private final double MOVE_THRESHOLD = Math.toRadians(10);
    private float rotSpeed = 2.5f;
    private float moveSpeed = 0.8f;

    private Node followNode;
    private Pose pose;

    public AnimationControllerFollow(Node follow)
    {
        followNode = follow;
        pose = new Pose(new Vector3f(0, 0, 0), Matrix3f.IDENTITY); // ID Matrix -> Blickrichtung entlang der Z Achse
    }

    @Override
    public void move(float time)
    {
        Vector3f dir = followNode.getWorldTranslation().subtract(pose.pos).normalize();
        Vector3f curDir = getDirection();
        double alpha = Math.atan2(curDir.z, curDir.x) - Math.atan2(dir.z, dir.x);
        if (Math.abs(alpha) > Math.PI)
        {
            alpha = 2 * Math.PI - alpha;
            alpha *= -1;
        }
        System.out.println(Math.toDegrees(alpha));
        Quaternion q = new Quaternion();
        q.fromAngles(0, (float) alpha * rotSpeed * time, 0);
        pose.rot = pose.rot.mult(q.toRotationMatrix());

        if (Math.abs(alpha) < MOVE_THRESHOLD)
        {
            pose.pos = pose.pos.add(getDirection().mult(time * moveSpeed)); //Bewegung in Blickrichtung
        }
    }

    @Override
    public Pose getPose()
    {
        return pose;
    }

    @Override
    public Vector3f getDirection()
    {
        return pose.rot.getColumn(2).normalize(); // Gibt es einen anderen Weg?
    }
}
