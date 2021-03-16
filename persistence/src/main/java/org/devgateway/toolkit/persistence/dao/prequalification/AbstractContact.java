package org.devgateway.toolkit.persistence.dao.prequalification;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractChildExpandableAuditEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

/**
 * @author Octavian Ciubotaru
 */
@MappedSuperclass
public abstract class AbstractContact<P extends AbstractAuditableEntity>
        extends AbstractChildExpandableAuditEntity<P> {

    @NotBlank
    @Column(nullable = false)
    private String mailingAddress;

    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String directors;

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public static void copy(AbstractContact<?> src, AbstractContact<?> dst) {
        dst.setDirectors(src.getDirectors());
        dst.setEmail(src.getEmail());
        dst.setPhoneNumber(src.getPhoneNumber());
        dst.setMailingAddress(src.getMailingAddress());
    }

    @Override
    public String toString() {
        return new StringJoiner(" / ")
                .add(StringUtils.defaultString(directors, "<missing director>"))
                .add(StringUtils.defaultString(email, "<missing email>"))
                .add(StringUtils.defaultString(phoneNumber, "<missing phone number>"))
                .toString();
    }
}
