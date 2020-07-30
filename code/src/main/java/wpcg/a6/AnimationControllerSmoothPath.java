package wpcg.a6;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import wpcg.base.Path;
import wpcg.base.animatedmesh.AnimationController;

import java.util.List;

public class AnimationControllerSmoothPath implements AnimationController
{
    private float speed;
    private HermiteSpline spline;
    private float t;
    private Pose pose;

    public AnimationControllerSmoothPath(List<Vector3f> waypoints, float speed)
    {
        this.speed = speed;
//        waypoints.forEach(v -> System.out.println("new Vector3f(" + v.x + "f, " + v.y + "f, " + v.z + "f)")); // fuer Testdaten
        spline = new HermiteSpline(waypoints);
        t = 0;
        pose = new Pose(waypoints.get(0), new Matrix3f());
    }

    @Override
    public void move(float time)
    {
        t += time * speed;
        if(t > 1 )
        {
            t = 0;
        }
        pose.pos = spline.getAt(t);

        Vector3f z = spline.getTangentAt(t).normalize();
        Vector3f y = Vector3f.UNIT_Y;
        Vector3f x = y.cross(z).normalize();
        Matrix3f rot = new Matrix3f();
        rot.fromAxes(x, y, z);
        pose.rot = rot;
    }

    @Override
    public Pose getPose()
    {
        return pose;
    }

    @Override
    public Vector3f getDirection()
    {
        return spline.getTangentAt(t);
    }
}
