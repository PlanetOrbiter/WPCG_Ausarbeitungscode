package wpcg.a6;

import com.jme3.math.Vector3f;
import org.junit.Test;

import static org.junit.Assert.*;

public class HermiteCurveTest
{

    @Test
    public void getAt()
    {
        float threshhold = 0.001f;
        Vector3f p0 = new Vector3f(-0.6f, 0, 1.5f);
        Vector3f p1 = new Vector3f(1, 0, 1);
        Vector3f m0 = new Vector3f(-0.03f, 0, 2);
        Vector3f m1 = new Vector3f(1.125f, 0, 2);
        HermiteCurve curve = new HermiteCurve(p0, m0, p1, m1);

        Vector3f result = curve.getAt(0.5f);
        Vector3f expected = new Vector3f(0.0556f, 0, 1.25f);
        Vector3f dif = result.subtract(expected);
        assertHelper(dif, threshhold);

        result = curve.getAt(0);
        dif = result.subtract(p0);
        assertHelper(dif, threshhold);

        result = curve.getAt(1);
        dif = result.subtract(p1);
        assertHelper(dif, threshhold);
    }

    @Test
    public void getTangentAt()
    {
        float thresshold = 0.001f;
        Vector3f p0 = new Vector3f(-0.6f, 0, 1.5f);
        Vector3f p1 = new Vector3f(1, 0, 1);
        Vector3f m0 = new Vector3f(-0.03f, 0, 2);
        Vector3f m1 = new Vector3f(1.125f, 0, 2);
        HermiteCurve curve = new HermiteCurve(p0, m0, p1, m1);

        Vector3f result = curve.getTangentAt(0.5f);
        Vector3f expected = new Vector3f(2.126f, 0, -1.75f);
        Vector3f dif = result.subtract(expected);
        assertHelper(dif, thresshold);

        result = curve.getTangentAt(0);
        dif = result.subtract(m0);
        assertHelper(dif, thresshold);

        result = curve.getTangentAt(1);
        dif = result.subtract(m1);
        assertHelper(dif, thresshold);
    }

    private void assertHelper(Vector3f dif, float threshhold)
    {
        assertTrue("X value incorrect", Math.abs(dif.x) < threshhold);
        assertTrue("Y value incorrect", Math.abs(dif.y) < threshhold);
        assertTrue("Z value incorrect", Math.abs(dif.z) < threshhold);
    }
}