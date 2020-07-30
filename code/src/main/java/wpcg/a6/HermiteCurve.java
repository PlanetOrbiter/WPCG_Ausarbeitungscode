package wpcg.a6;

import com.jme3.math.Vector3f;

public class HermiteCurve
{
    private Vector3f p0;
    private Vector3f p1;
    private Vector3f m0;
    private Vector3f m1;

    public HermiteCurve(Vector3f p0, Vector3f m0, Vector3f p1, Vector3f m1)
    {
        this.p0 = p0;
        this.p1 = p1;
        this.m0 = m0;
        this.m1 = m1;
    }

    public Vector3f getAt(float t) //throws IllegalArgumentException
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter needs to be between 0 and 1");
        }
        Vector3f pol0 = p0.mult((2.0f * t * t * t) - (3.0f * t * t) + 1.0f);
        Vector3f pol1 = m0.mult((t * t * t) - (2.0f * t * t) + t);
        Vector3f pol2 = p1.mult((-2.0f * t * t * t) + (3.0f * t * t));
        Vector3f pol3 = m1.mult((t * t * t) - (t * t));
        return pol0.add(pol1).add(pol2).add(pol3);
    }

    public Vector3f getTangentAt(float t)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter needs to be between 0 and 1");
        }
        Vector3f pol0 = p0.mult((6.0f * t * t) - (6.0f * t));
        Vector3f pol1 = m0.mult((3.0f * t * t) - (4.0f * t) + 1);
        Vector3f pol2 = p1.mult((-6.0f * t * t) + (6.0f * t));
        Vector3f pol3 = m1.mult((3.0f * t * t) - (2.0f * t));
        return pol0.add(pol1).add(pol2).add(pol3); // TODO normalize?
    }
}
