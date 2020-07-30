package wpcg.a5.kdtree;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import java.util.List;
import java.util.Vector;

import static wpcg.a5.kdtree.KDTreeNode.SplitDirection.*;

public class KDTreeBuilder
{
    public static KDTreeNode<Geometry> buildKDTree(List<KDTreeData<Geometry>> content)
    {
        Vector<Vector2f> bounds = getBounds(content);
        KDTreeNode<Geometry> tree = buildKDTree(content, X, bounds.get(0), bounds.get(1));
        return tree;
    }

    private static KDTreeNode<Geometry> buildKDTree(List<KDTreeData<Geometry>> content, KDTreeNode.SplitDirection dir, Vector2f ll, Vector2f ur)
    {
        if(content == null || content.isEmpty())
        {
            return null;
        }
        if(content.size() == 1)
        {
            return new KDTreeNode<Geometry>(content.get(0), dir, ll, ur);
        }
        content.sort(KDTreeData.getComparator(dir));
        int medIdx = content.size() / 2;
        KDTreeNode<Geometry> node = new KDTreeNode<Geometry>(content.get(medIdx), dir, ll, ur);
        List<KDTreeData<Geometry>> left = content.subList(0, medIdx);
        List<KDTreeData<Geometry>> right = content.subList(medIdx + 1, content.size());

        Vector2f nodePoint = node.getData().getP();

        if(dir == X)
        {
            node.setNeg(buildKDTree(left, dir.next(), ll, new Vector2f(nodePoint.x, ur.y)));
            node.setPos(buildKDTree(right, dir.next(), new Vector2f(nodePoint.x, ll.y), ur));
        }
        else
        {
            node.setNeg(buildKDTree(left, dir.next(), ll, new Vector2f(ur.x, nodePoint.y)));
            node.setPos(buildKDTree(right, dir.next(), new Vector2f(ll.x, nodePoint.y), ur));
        }

        return node;
    }

    private static Vector<Vector2f> getBounds(List<KDTreeData<Geometry>> content)
    {
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (KDTreeData<Geometry> e : content)
        {
            Vector2f p = e.getP();
            if(p.x < minX)
                minX = p.x;
            else if(p.x > maxX)
                maxX = p.x;
            if(p.y < minY)
                minY = p.y;
            else if(p.y > maxY)
                maxY = p.y;
        }
        Vector<Vector2f> ret = new Vector<Vector2f>(2);
        ret.add(new Vector2f(minX, minY));
        ret.add(new Vector2f(maxX, maxY));
        return ret;
    }
}
