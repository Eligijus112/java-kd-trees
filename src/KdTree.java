import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

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

        // Rectangle of the point
        RectHV rect;

        // A boolean indicating whether the point is 'vertical' -> use y to build a BST
        // The default is false, because at the root we use the x coordinate
        boolean isVertical = false;
    }

    // Checking whether the tree is empty
    public boolean isEmpty() {
        return n == 0;
    }

    // Size of the kd tree
    public int size() {
        return n;
    }

    // Constructing a node defining rectangle
    private RectHV constructRect(Node node) {
        // Getting the parents rectangle
        RectHV rectParent = node.parent.rect;

        // Getting each of the coordinates of the parent
        double xmin = rectParent.xmin();
        double xmax = rectParent.xmax();
        double ymin = rectParent.ymin();
        double ymax = rectParent.ymax();

        // Constructing the rectangle of the node in question
        if (node.isVertical) {
            if (node.point.x() < node.parent.point.x())
                xmax = node.parent.point.x();
            else
                xmin = node.parent.point.x();
        } else {
            if (node.point.y() < node.parent.point.y())
                ymax = node.parent.point.y();
            else
                ymin = node.parent.point.y();
        }

        return new RectHV(xmin, ymin, xmax, ymax);
    }

    // A method to insert a new point into the tree
    public void insert(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        // Checking if the tree already contains the point
        if (!contains(point)) {

            // Creating an empty node
            Node nodePoint = new Node();

            // Saving the point to the Node
            nodePoint.point = point;

            // Checking if the point is the first in the system
            if (isEmpty()) {
                // Setting the new root
                root = nodePoint;

                // The parent of the root is the root itself
                root.parent = nodePoint;

                // The root rectangle is the whole unit rectangle
                root.rect = new RectHV(0, 0, 1, 1);

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
                nodePoint.isVertical = !curNode.isVertical;

                // Saving the parent
                nodePoint.parent = curNode;

                // Constructing the rectangle for the nodePoint
                nodePoint.rect = constructRect(nodePoint);

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
    }

    // Check if the kd tree contains a point
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        return contains(root, point);
    }

    // Recursive function to check if the point is in the tree
    private boolean contains(Node x, Point2D point) {
        if (x == null) return false;
        if (x.isVertical) {
            if (point.y() < x.point.y()) {
                return contains(x.left, point);
            } else if (point.y() > x.point.y() || (point.y() == x.point.y() && point.x() != x.point.x())) {
                return contains(x.right, point);
            } else {
                return true;
            }
        } else {
            if (point.x() < x.point.x()) {
                return contains(x.left, point);
            } else if (point.x() > x.point.x() || (point.x() == x.point.x() && point.y() != x.point.y())) {
                return contains(x.right, point);
            } else {
                return true;
            }
        }
    }

    // Drawing all the points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            x.point.draw();

            if (!x.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.point.x(), x.parent.point.y(), x.point.x(), x.parent.point.y());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.parent.point.x(), x.point.y(), x.parent.point.x(), x.point.y());
            }
            draw(x.left);
            draw(x.right);
        }
    }

    // Placeholders for the nearest neighbor algorithm
    private Point2D minPoint;
    private double minDist;

    // Finds the nearest neighboring point using the kd tree data structure
    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Cannot be point null");
        if (!isEmpty()) {
            // Calculating the distance to the root
            minDist = point.distanceSquaredTo(root.point);
            minPoint = root.point;

            // Recursive algorithm initiation
            nearest(root, point);

            // Returning the newly found min point
            return minPoint;
        } else {
            return null;
        }
    }

    private void nearest(Node node, Point2D point) {
        // Calculating the distance
        double d = node.point.distanceSquaredTo(point);

        // If the distance is smaller we update the global minPoint and minDist
        if (d < minDist) {
            minDist = d;
            minPoint = node.point;
        }

        // Traversing further:
        // If both the children are present
        if (node.left != null && node.right != null) {
            // Initiating the placeholders for the both distances
            double distLeft = node.left.rect.distanceSquaredTo(point);
            double distRight = node.right.rect.distanceSquaredTo(point);

            // Seeing where to go next
            if (distLeft < distRight) {
                nearest(node.left, point);
                if (distRight < minDist) {
                    nearest(node.right, point);
                }
            } else {
                nearest(node.right, point);
                if (distLeft < minDist) {
                    nearest(node.left, point);
                }
            }
        }

        // If only the left child is present
        if (node.left != null && node.right == null) {
            double distLeft = node.left.rect.distanceSquaredTo(point);
            if (distLeft < minDist) {
                nearest(node.left, point);
            }
        }

        // If only the right child is present
        if (node.right != null && node.left == null) {
            double distRight = node.right.rect.distanceSquaredTo(point);
            if (distRight < minDist) {
                nearest(node.right, point);
            }
        }
    }

    // Defining the recursive function
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot be rect null");
        // Searching for points in the query rectangle
        ArrayList<Point2D> pointsInRange = new ArrayList<>();

        if (!isEmpty()) {
            // Starting the recursion
            range(root, rect, pointsInRange);
        }

        // Returning the newly found min point
        return pointsInRange;
    }

    private void range(Node node, RectHV rect, ArrayList<Point2D> points) {
        if (node != null) {
            // Checking if the node rectangle intersects with the query rectangle
            if (node.rect.intersects(rect)) {
                // Checking if the point is in the query rectangle
                if (rect.contains(node.point)) {
                    points.add(node.point);
                }

                range(node.left, rect, points);
                range(node.right, rect, points);
            }
        }
    }

    // Unit testing
    public static void main(String[] args) {
        // Initiating a new KD tree
        KdTree tree = new KdTree();

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        // Checking the size of the kd tree
        System.out.println("Size of the KD tree: " + tree.size());

        // Checking if the tree contains a point that is in the tree
        System.out.println("Point in tree that should be there: " + tree.contains(new Point2D(0.5, 0.4)));

        // Checking if the tree contains a point that is not in the tree
        System.out.println("Point in tree that should not be there: " + tree.contains(new Point2D(0.1, 0.2)));

        // Finding the nearest point to the given point
        Point2D nearest = tree.nearest(new Point2D(0.8125, 0.75));
        System.out.println("The nearest point in the system is: " + nearest.toString());

        // Defining the query rectangle
        RectHV queryRect = new RectHV(0.325, 0.058, 0.859, 0.958);

        // Searching for the points in the query
        Iterable<Point2D> pointsInRect = tree.range(queryRect);

        // Printing out the rectangle
        System.out.println("The query rectangle dimensions: " + queryRect.toString());

        // Printing the points in the query
        for (Point2D p : pointsInRect) {
            System.out.println("Point in query rectangle: " + p.toString());
        }
    }
}
