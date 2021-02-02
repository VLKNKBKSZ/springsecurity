package com.vlkn.spring.security.authentication;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUserName(String username);
}
