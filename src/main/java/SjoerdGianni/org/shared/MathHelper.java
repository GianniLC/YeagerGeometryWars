package SjoerdGianni.org.shared;

import com.github.hanyaeger.api.Coordinate2D;

public class MathHelper {

    /**
     * Calculate the angle in degrees between two points.
     * @param startPosition coordinates of the start position
     * @param endPosition coordinates of the end position
     * @return angle in degrees viewed from the start position towards the end position
     */
    public static double calculateAngleInDegrees(Coordinate2D startPosition, Coordinate2D endPosition){
        double dx = endPosition.getX() - startPosition.getX();
        double dy = endPosition.getY() - startPosition.getY();
        // `dx` and `dy` are passed to `Math.atan2()` in this order,
        // because the positive Y-axis direction in Yaeger is downwards.
        // Providing them in the opposite order causes the rotation direction to be inverted and 90 degrees off.
        return Math.toDegrees(Math.atan2(dx, dy));
    }
}
