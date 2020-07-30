package wpcg.a3;

import com.jme3.math.Vector3f;
import wpcg.a3.halfedge.HalfEdge;
import wpcg.a3.halfedge.HalfEdgeTriangleMesh;
import wpcg.a3.halfedge.HalfEdgeVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Subdivision
{
    public static void subdivide(HalfEdgeTriangleMesh tMesh, int iterations)
    {
        for(int i = 0; i < iterations; i++)
            subdivide(tMesh);
    }
    public static void subdivide(HalfEdgeTriangleMesh mesh)
    {
        int nHalfEdges = mesh.getNumberOfHalfEdges();
        int nTriangles = mesh.getNumberOfTriangles();
        int nVertices = mesh.getNumberOfVertices();
        List<Vector3f> newPositions = new ArrayList<>();

        //STEP 1
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
        {
            HalfEdgeVertex v = mesh.getVertex(vertexIndex);
            List<HalfEdgeVertex> neighbours = getOneRing(v);
            int n = neighbours.size();
            Vector3f newPos = new Vector3f();
            if(n > 3)
            {
                float beta = 3.0f / (8 * n);
                float oldPosWeight = 1 - beta * n;
                newPos = v.getPosition().mult(oldPosWeight);
                for (HalfEdgeVertex neighbour : neighbours)
                {
                    newPos = newPos.add(neighbour.getPosition().mult(beta));
                }
            }
            else if(n == 3)
            {
                float beta = 3f / 16f;
                float oldPosWeight = 1 - beta * n;
                newPos = v.getPosition().mult(oldPosWeight);
                for (HalfEdgeVertex neighbour : neighbours)
                {
                    newPos = newPos.add(neighbour.getPosition().mult(beta));
                }
            }
            else
            {
                newPos = neighbours.get(0).getPosition().mult(1.0f / 8.0f);
                newPos = newPos.add(neighbours.get(1).getPosition().mult(1.0f / 8.0f));
                newPos = newPos.add(v.getPosition().mult(3.0f / 4.0f));
            }
            newPositions.add(newPos);
//            if(n > 2)
//            {
//                float beta = 3.0f / (8 * n);
//                float oldPosWeight = 1 - beta * n;
//                newPos = v.getPosition().mult(oldPosWeight);
//                for (HalfEdgeVertex neighbour : neighbours)
//                {
//                    newPos = newPos.add(neighbour.getPosition().mult(beta));
//                }
//            }
//            else
//            {
//                newPos = neighbours.get(0).getPosition().mult(1.0f / 8.0f);
//                newPos = newPos.add(neighbours.get(1).getPosition().mult(1.0f / 8.0f));
//                newPos = newPos.add(v.getPosition().mult(3.0f / 4.0f));
//            }
//            newPositions.add(newPos);
        }

        //STEP 2
        Map<HalfEdgeVertex, HalfEdge> map = mesh.split();
        for (int i = nVertices; i < mesh.getNumberOfVertices(); i++)
        {
            HalfEdgeVertex v = mesh.getVertex(i);
            HalfEdge edge = map.get(v);
            Vector3f newPos = new Vector3f();
            if(edge.getNext() != null && edge.getOpposite() != null)
            {
                newPos = newPos.add(edge.getStartVertex().getPosition().mult(3.0f / 8.0f));
                newPos = newPos.add(edge.getEndVertex().getPosition().mult(3.0f / 8.0f));
                newPos = newPos.add(edge.getNext().getEndVertex().getPosition().mult(1.0f / 8.0f));
                newPos = newPos.add(edge.getOpposite().getNext().getEndVertex().getPosition().mult(1.0f / 8.0f));
            }
            else
            {
                newPos = newPos.add(edge.getStartVertex().getPosition().mult(1.0f / 2.0f));
                newPos = newPos.add(edge.getEndVertex().getPosition().mult(1.0f / 2.0f));
            }
            newPositions.add(newPos);
        }

        //STEP 3
        for (int i = 0; i < nHalfEdges; i++)
        {
            mesh.removeHalfEdge(0);
        }
        for (int i = 0; i < nTriangles; i++)
        {
            mesh.removeTriangle(0);
        }
        for (int i = 0; i < newPositions.size(); i++)
        {
            mesh.getVertex(i).setPosition(newPositions.get(i));
        }
//        mesh.getVertex(3).setPosition(new Vector3f(0,0,0));

        mesh.computeTriangleNormals();
    }

    private static List<HalfEdgeVertex> getOneRing(HalfEdgeVertex v)
    {
        List<HalfEdgeVertex> list = new ArrayList<>();
        HalfEdge cur = v.getHalfEdge();
        do
        {
            list.add(cur.getEndVertex());
            if(cur.getOpposite() == null)
            {
                List<HalfEdgeVertex> otherDir = new ArrayList<>();
                otherDir.add(list.get(0));
                otherDir.add(cur.getEndVertex());
                return otherDir;
            }
            cur = cur.getOpposite().getNext();
        }
        while (!cur.equals(v.getHalfEdge()));
        return list;
    }
}
