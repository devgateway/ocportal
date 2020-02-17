package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public interface EditorValidatorRoleAssignable {

    String getUserRole();

    String getValidatorRole();

    default Set<String> getCombinedRoles() {
        return Sets.newHashSet(getUserRole(), getValidatorRole());
    }

    default String getCommaCombinedRoles() {
        return StringUtils.join(getCombinedRoles(), ",");
    }
}
