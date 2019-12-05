package org.devgateway.toolkit.persistence.dao.categories;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * @author mpostelnicu
 */
@MappedSuperclass
public class LocationPointCategory extends Category {

    protected Point locationPoint;

    @Transient
    protected double x;

    @Transient
    protected double y;

    public Point getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Point locationPoint) {
        this.locationPoint = locationPoint;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
