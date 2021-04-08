package org.devgateway.toolkit.persistence.dao.categories;

import org.apache.commons.lang3.BooleanUtils;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.AccessType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Form(featureName = "supplierForm")
public class Supplier extends Category {
    @ExcelExport(name = "Address")
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String address;

    @ExcelExport(justExport = true, name = "AGPO Category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<TargetGroup> targetGroups = new ArrayList<>();

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String agpoRegistrationId;

    private Boolean nonPerforming = false;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierContact> contacts = new ArrayList<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Subcounty> subcounties = new ArrayList<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Ward> wards = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public List<TargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public String getAgpoRegistrationId() {
        return agpoRegistrationId;
    }

    public void setAgpoRegistrationId(String agpoRegistrationId) {
        this.agpoRegistrationId = agpoRegistrationId;
    }

    public List<SupplierContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<SupplierContact> contacts) {
        this.contacts = contacts;
    }

    public List<Subcounty> getSubcounties() {
        return subcounties;
    }

    public void setSubcounties(List<Subcounty> subcounties) {
        this.subcounties = subcounties;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }

    public void addContact(SupplierContact contact) {
        contact.setParent(this);
        contacts.add(contact);
    }

    public void setNonPerforming(Boolean nonPerforming) {
        this.nonPerforming = nonPerforming;
    }

    public Boolean getNonPerforming() {
        return nonPerforming;
    }

    @Override
    @AccessType(AccessType.Type.PROPERTY)
    public String getLabel() {
        return BooleanUtils.isTrue(nonPerforming) ? label + " (non-performing)" : label;
    }

    public String getRealLabel() {
        return label;
    }

    public void setRealLabel(String label) {
        this.label = label;
    }
}
