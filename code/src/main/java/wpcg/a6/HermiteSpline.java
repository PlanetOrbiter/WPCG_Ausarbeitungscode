package wpcg.a6;

import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class HermiteSpline
{
    private List<HermiteCurve> segments;
    private final int n;

    public HermiteSpline(List<Vector3f> waypointList)
    {
        n = waypointList.size();
        List<Vector3f> tangents = new ArrayList<>();
        segments = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            Vector3f tangent;
            if (i == 0)
            {
                tangent = waypointList.get(1).subtract(waypointList.get(n - 1));
            }
            else if (i == waypointList.size() - 1)
            {
                tangent = waypointList.get(0).subtract(waypointList.get(n - 2));
            }
            else
            {
                tangent = waypointList.get(i + 1).subtract(waypointList.get(i - 1));
            }
            tangent = tangent.normalize(); // Reduziert Geschwindigkeitsvariation
//            tangent.mult(0.5f); // TODO Hilft uns nicht, wir laufen auch so nicht durch Waende
            tangents.add(tangent);
        }
        for (int i = 0; i < n; i++)
        {
            HermiteCurve curve;
            if(i == n - 1)
            {
                curve = new HermiteCurve(waypointList.get(i), tangents.get(i), waypointList.get(0), tangents.get(0));
            }
            else
            {
                curve = new HermiteCurve(waypointList.get(i), tangents.get(i), waypointList.get(i + 1), tangents.get(i + 1));
            }
            segments.add(curve);
        }
    }

    public Vector3f getAt(float t) //throws IllegalArgumentException
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter needs to be between 0 and 1");
        }
        float deltaT = 1.0f / (float) n;
        int segmentIndex = (int)(t * n);
        segmentIndex = segmentIndex >= n ? n - 1 : segmentIndex;
        float lambda = (t - (segmentIndex * deltaT)) / deltaT;
        return segments.get(segmentIndex).getAt(lambda);
    }

    public Vector3f getTangentAt(float t)
    {
        if (t < 0 || t > 1)
        {
            throw new IllegalArgumentException("Parameter needs to be between 0 and 1");
        }
        float deltaT = 1.0f / (float) n;
        int segmentIndex = (int)(t * n);
        segmentIndex = segmentIndex >= n ? n - 1 : segmentIndex;
        float lambda = (t - (segmentIndex * deltaT)) / deltaT;
        return segments.get(segmentIndex).getTangentAt(lambda);
    }
}
