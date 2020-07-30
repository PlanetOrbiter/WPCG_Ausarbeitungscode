package wpcg.a6;

import com.jme3.math.Vector3f;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HermiteSplineTest
{

    @Test
    public void getAt()
    {
        float threshhold = 0.001f;
        List<Vector3f> points = new ArrayList<>();
        points.add(new Vector3f(1.0f, 0.0f, 2.732051f));
        points.add(new Vector3f(2.5f, 0.0f, 1.8660254f));
        points.add(new Vector3f(4.0f, 0.0f, 1.0f));
        points.add(new Vector3f(5.5f, 0.0f, 1.8660254f));
        points.add(new Vector3f(5.5f, 0.0f, 3.598076f));
        points.add(new Vector3f(4.0f, 0.0f, 2.732051f));
        points.add(new Vector3f(4.0f, 0.0f, 4.464102f));
        points.add(new Vector3f(5.5f, 0.0f, 5.330127f));
        points.add(new Vector3f(4.0f, 0.0f, 6.196152f));
        points.add(new Vector3f(2.5f, 0.0f, 7.0621777f));
        points.add(new Vector3f(1.0f, 0.0f, 6.196152f));
        points.add(new Vector3f(1.0f, 0.0f, 4.464102f));

        HermiteSpline spline = new HermiteSpline(points);

        Vector3f dif = spline.getAt(0).subtract(spline.getAt(1));
        assertHelper(dif, threshhold);

        dif = spline.getAt(0.5f).subtract(points.get(6)); // Bei 12 Punkten, also theoretischen 13 punkten, ist der 7. Punkt exakt die Mitte
        assertHelper(dif, threshhold);
    }

    @Test
    public void getTangentAt()
    {
        float threshhold = 0.001f;
        List<Vector3f> points = new ArrayList<>();
        points.add(new Vector3f(1.0f, 0.0f, 2.732051f));
        points.add(new Vector3f(2.5f, 0.0f, 1.8660254f));
        points.add(new Vector3f(4.0f, 0.0f, 1.0f));
        points.add(new Vector3f(5.5f, 0.0f, 1.8660254f));
        points.add(new Vector3f(5.5f, 0.0f, 3.598076f));
        points.add(new Vector3f(4.0f, 0.0f, 2.732051f));
        points.add(new Vector3f(4.0f, 0.0f, 4.464102f));
        points.add(new Vector3f(5.5f, 0.0f, 5.330127f));
        points.add(new Vector3f(4.0f, 0.0f, 6.196152f));
        points.add(new Vector3f(2.5f, 0.0f, 7.0621777f));
        points.add(new Vector3f(1.0f, 0.0f, 6.196152f));
        points.add(new Vector3f(1.0f, 0.0f, 4.464102f));

        HermiteSpline spline = new HermiteSpline(points);

        Vector3f dif = spline.getTangentAt(0).subtract(spline.getTangentAt(1));
        assertHelper(dif, threshhold);
    }

    private void assertHelper(Vector3f dif, float threshhold)
    {
        assertTrue("X value incorrect", Math.abs(dif.x) < threshhold);
        assertTrue("Y value incorrect", Math.abs(dif.y) < threshhold);
        assertTrue("Z value incorrect", Math.abs(dif.z) < threshhold);
    }
}