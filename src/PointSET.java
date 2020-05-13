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
    boolean isEmpty() {
        return points.isEmpty();
    }

    // Getting the size of the set
    int size() {
        return points.size();
    }

    // Insert a point to a SET
    void insert(Point2D p) {
        points.add(p);
    }

    // Draws the points in standard draw
    void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // Return all the points in the given rectangular
    Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    // Return the nearest neighbour to point p
    Point2D nearest(Point2D p) {
        if (points.isEmpty()) return null;
        Point2D nearestPoint = points.min();
        double distance = p.distanceTo(nearestPoint);

        for (Point2D point : points) {
            if (p.equals(point)) continue;
            if (p.distanceTo(point) < distance) {
                nearestPoint = point;
                distance = p.distanceTo(point);
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

        points.insert(new Point2D(-1.0, 5));
        points.insert(new Point2D(1.0, 2.0));

        // Checking the size of the created points
        System.out.println("Size of points: " + points.size());

        // Check to see what points are in the rectangular
        Iterable<Point2D> pointsInRect = points.range(rectangular);
        for (Point2D point : pointsInRect) {
            System.out.println("Point in rectangular: " + point.toString());
        }
    }
}
