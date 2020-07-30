package wpcg.beziersurface;

import com.jme3.math.Vector3f;

public class BezierCurve {

    public static Vector3f lerp(Vector3f a, Vector3f b, float t)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter t needs to be between 0 and 1");
        }

        return  a.add(b.subtract(a)).mult(t);
    }

    public static Vector3f quadraticCurve(Vector3f a, Vector3f b, Vector3f c, float t)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter t needs to be between 0 and 1");
        }

        Vector3f v1 = lerp(a, b, t);
        Vector3f v2 = lerp(b, c, t);


        return  lerp(v1, v2, t);
    }

    public static Vector3f cubicCurve(Vector3f a, Vector3f b, Vector3f c, Vector3f d, float t)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter t needs to be between 0 and 1");
        }
        Vector3f v1 = quadraticCurve(a, b, c, t);
        Vector3f v2 = quadraticCurve(b, c, d, t);

        return  lerp(v1, v2, t);
    }

    public static float getBernsteinDimension3Value(float t, int k)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter t needs to be between 0 and 1");
        }

        if(k == 0)
        {
            return (1.0f - t) * (1.0f - t) * (1.0f - t);
        }
        else if(k == 1)
        {
            return  3.0f * (1.0f - t) * (1.0f - t) * t;
        }
        else if(k == 2)
        {
            return 3.0f * (1.0f - t) * t * t;
        }
        else if (k == 3)
        {
            return t * t * t;
        }
        else
        {
            throw new IllegalArgumentException("Parameter k needs to be between 0 and 4");
        }
    }
}
