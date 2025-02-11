package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

/**
 * @author mpostelnicu
 */
@MappedSuperclass
@Audited
public abstract class LocationPointCategory extends Category {

    @org.springframework.data.annotation.Transient
    protected Point locationPoint;

    @Transient
    protected Double x;

    @Transient
    protected Double y;

    public Point getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Point locationPoint) {
        this.locationPoint = locationPoint;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
