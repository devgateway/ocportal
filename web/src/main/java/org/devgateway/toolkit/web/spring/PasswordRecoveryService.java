package org.devgateway.toolkit.web.spring;

/**
 * @author Octavian Ciubotaru
 */
public interface PasswordRecoveryService {

    void resetPassword(String email);

    void changePassword(String username, String oldPassword, String newPassword);
}
