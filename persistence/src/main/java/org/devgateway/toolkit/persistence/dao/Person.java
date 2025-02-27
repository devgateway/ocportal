/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Table(indexes = {@Index(columnList = "username")},
        uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")}
)
public class Person extends AbstractAuditableEntity implements UserDetails, Labelable {
    private static final long serialVersionUID = 109780377848343674L;

    @ExcelExport
    @NotNull
    private String username;

    @ExcelExport
    @NotNull
    private String firstName;

    @ExcelExport
    @NotNull
    private String lastName;

    private String title;

    @ExcelExport
    @NotNull
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    @Transient
    @JsonIgnore
    private String plainPassword;

    @Transient
    @JsonIgnore
    private String plainPasswordCheck;

    private Boolean changePasswordNextSignIn = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Department> departments = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Size(min = 1)
    private List<Role> roles = new ArrayList<>();


    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    // flag if user/admin want to change password in profile account
    @Transient
    @JsonIgnore
    private boolean changeProfilePassword;

    private Boolean enabled = true;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(final String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPlainPasswordCheck() {
        return plainPasswordCheck;
    }

    public void setPlainPasswordCheck(final String plainPasswordCheck) {
        this.plainPasswordCheck = plainPasswordCheck;
    }

    public Boolean getChangePasswordNextSignIn() {
        return changePasswordNextSignIn;
    }

    public void setChangePasswordNextSignIn(final Boolean changePasswordNextSignIn) {
        this.changePasswordNextSignIn = changePasswordNextSignIn;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public void setAuthorities(final Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean getChangeProfilePassword() {
        return changeProfilePassword;
    }

    public void setChangeProfilePassword(final boolean changeProfilePassword) {
        this.changeProfilePassword = changeProfilePassword;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final Set<Department> departments) {
        this.departments = departments;
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public String getLabel() {
        return username;
    }

}
