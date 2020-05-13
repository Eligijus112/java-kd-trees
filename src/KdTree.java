import edu.princeton.cs.algs4.Point2D;

public class KdTree {
    // Number of points in the tree
    private int n;

    // Defining the root node
    private Node root;

    // Defining the subclass that will hold all the information
    private static class Node {
        // Placeholder for the left node (default being null)
        Node left = null;

        // Placeholder for the right node (default being null)
        Node right = null;

        // Parent node
        Node parent = null;

        // Point2D in the node
        Point2D point;

        // A boolean indicating whether the point is 'vertical' -> use y to build a BST
        // The default is false, because at the root we use the x coordinate
        boolean isVertical = false;
    }

    // A method to insert a new point into the tree
    public void insert(Point2D point) {
        // Creating an empty node
        Node nodePoint = new Node();

        // Saving the point to the Node
        nodePoint.point = point;

        // Checking if the point is the first in the system
        if (n == 0) {
            // Setting the new root
            root = nodePoint;

            // The parent of the root is the root itself
            root.parent = nodePoint;
        } else {
            // If there already is a root we traverse the tree according to the 2d tree rules
            Node curNode = root;
            Node curNodeParent = root.parent;

            // Extracting the node which we will need to compare
            while (curNode != null) {
                // Saving the trailing node
                curNodeParent = curNode;

                // Initiating the comparator int
                int cmp = 0;

                // Getting the type of comparison
                if (curNode.isVertical) {
                    cmp = Double.compare(point.y(), curNode.point.y());
                } else {
                    cmp = Double.compare(point.x(), curNode.point.x());
                }

                // Deciding where to look - to the left or to the right of the tree
                if (cmp < 0) {
                    curNode = curNode.left;
                } else {
                    curNode = curNode.right;
                }
            }
            // Going back to the parent
            curNode = curNodeParent;

            // Deciding where to put the point in
            int cmp;
            if (curNode.isVertical) {
                cmp = Double.compare(point.y(), curNode.point.y());
            } else {
                cmp = Double.compare(point.x(), curNode.point.x());
            }

            // Flipping the boolean for coordinate checking
            nodePoint.isVertical = !nodePoint.isVertical;

            // Saving the parent
            nodePoint.parent = curNode;

            // Saving the pointer to the new Node
            if (cmp < 0) {
                curNode.left = nodePoint;
            } else {
                curNode.right = nodePoint;
            }
        }
        // Increment the number of nodes in the system by 1
        n++;
    }

    // Unit testing
    public static void main(String[] args) {
        // Initiating a new KD tree
        KdTree tree = new KdTree();

        // Inserting new points to the tree
        tree.insert(new Point2D(-1.0, 5));
        tree.insert(new Point2D(1.0, 2.0));
        tree.insert(new Point2D(-2.0, 1.0));
        tree.insert(new Point2D(-5.0, 6.0));
    }
}
