import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    // Point holder
    private final SET<Point2D> points;

    // Constructing an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // Checking whether the set is empty
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // Getting the size of the set
    public int size() {
        return points.size();
    }

    // Insert a point to a SET
    public void insert(Point2D p) {
        points.add(p);
    }

    // Draws the points in standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // Method to check whether the set contains a given point
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // Return all the points in the given rectangular
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Cannot be rect null");
        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    // Return the nearest neighbour to point p
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot be point null");
        if (points.isEmpty()) return null;
        Point2D nearestPoint = points.min();
        double distance = p.distanceSquaredTo(nearestPoint);

        for (Point2D point : points) {
            if (p.distanceSquaredTo(point) < distance) {
                nearestPoint = point;
                distance = p.distanceSquaredTo(point);
            }
        }
        return nearestPoint;
    }

    // Unit testing
    public static void main(String[] args) {
        // Initiating the rectangular
        RectHV rectangular = new RectHV(0.0, 2.0, 10.0, 2.0);

        // Creating some 2d points
        PointSET points = new PointSET();

        points.insert(new Point2D(0.25, 0.0));
        points.insert(new Point2D(0.75, 1.0));
        points.insert(new Point2D(0.75, 0.75));
        points.insert(new Point2D(0.5, 1.0));
        points.insert(new Point2D(0.25, 0.5));
        points.insert(new Point2D(1.0, 0.75));
        points.insert(new Point2D(1.0, 1.0));
        points.insert(new Point2D(1.0, 0.75));
        points.insert(new Point2D(0.25, 0.75));

        // Checking the size of the created points
        System.out.println("Size of points: " + points.size());

        // Finding the nearest point to the query point
        Point2D nearestPoint = points.nearest(new Point2D(1.0, 1.0));
        System.out.println("The nearest point is: " + nearestPoint.toString());

        // Check to see what points are in the rectangular
        Iterable<Point2D> pointsInRect = points.range(rectangular);
        for (Point2D point : pointsInRect) {
            System.out.println("Point in rectangular: " + point.toString());
        }
    }
}
