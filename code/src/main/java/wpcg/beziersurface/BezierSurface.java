package wpcg.beziersurface;

import com.jme3.math.Vector3f;

public class BezierSurface {

    private Vector3f[][] controlPoints;


    /**
     * This Bezier Surface covers an area of 1 x 1. Therefor all x and z coordinates must be within that range.
     * Idealy the x and z values are chosen in a way that they make up an even deviation of the surface into smaller even squares.
     * The base function are two three-dimensional bezier curves with bernstein polynomials.
     * @param p00 The Origin. Should be (0, Y, 0)
     * @param p10 Should be (0.33, Y, 0)
     * @param p20 Should be (0.66, Y, 0)
     * @param p30 Should be (1, Y, 0)
     * @param p01 Should be (0, Y, 0.33)
     * @param p11 Should be (0.33, Y, 0.33)
     * @param p21 Should be (0.66, Y, 0.33)
     * @param p31 Should be (1, Y, 0.33)
     * @param p02 Should be (0, Y, 0.66)
     * @param p12 Should be (0.33, Y, 0.66)
     * @param p22 Should be (0.66, Y, 0.66)
     * @param p32 Should be (1, Y, 0.66)
     * @param p03 Should be (0, Y, 1)
     * @param p13 Should be (0.33, Y, 1)
     * @param p23 Should be (0.66, Y, 1)
     * @param p33 Should be (1, Y, 1)
     */
    public BezierSurface(Vector3f p00, Vector3f p10, Vector3f p20, Vector3f p30, Vector3f p01, Vector3f p11, Vector3f p21, Vector3f p31, Vector3f p02, Vector3f p12, Vector3f p22, Vector3f p32, Vector3f p03, Vector3f p13, Vector3f p23, Vector3f p33)
    {
        this.controlPoints = new Vector3f[4][4];
        this.controlPoints[0][0] = p00;
        this.controlPoints[0][1] = p01;
        this.controlPoints[0][2] = p02;
        this.controlPoints[0][3] = p03;

        this.controlPoints[1][0] = p10;
        this.controlPoints[1][1] = p11;
        this.controlPoints[1][2] = p12;
        this.controlPoints[1][3] = p13;

        this.controlPoints[2][0] = p20;
        this.controlPoints[2][1] = p21;
        this.controlPoints[2][2] = p22;
        this.controlPoints[2][3] = p23;

        this.controlPoints[3][0] = p30;
        this.controlPoints[3][1] = p31;
        this.controlPoints[3][2] = p32;
        this.controlPoints[3][3] = p33;

        for(int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                System.out.printf("I: %d J: %d || Value:  ", i, j);
                System.out.println(controlPoints[i][j]);
            }
        }

    }

    /**
     * Returns the coordinates corrosponding to the u and v value within the bezier surface.
     * @param u Must be between 0 and 1
     * @param v Must be between 0 and 1
     * @return Coordinates for u and v
     * @throws IllegalArgumentException
     */
    public Vector3f getCoordinates(float u, float v)
    {
        if (u < 0 || u > 1 ||v < 0 || v > 1)
        {
            throw new IllegalArgumentException("Parameters need to be between 0 and 1");
        }

        Vector3f resultVec = new Vector3f(0, 0, 0);
        for(int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                Vector3f addVec = controlPoints[i][j].mult(BezierCurve.getBernsteinDimension3Value(u, i)).mult(BezierCurve.getBernsteinDimension3Value(v, j));
                resultVec = resultVec.add(addVec);
            }
        }

        //System.out.println(resultVec.toString());
        //System.out.printf("U: %f V: %f \n", u, v);
        return resultVec;
    }

    public Vector3f[][] getControlPoints()
    {
        return this.controlPoints;
    }

}
