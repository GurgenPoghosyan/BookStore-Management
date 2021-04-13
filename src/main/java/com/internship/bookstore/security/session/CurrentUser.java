package com.internship.bookstore.security.session;

import com.internship.bookstore.persistence.entity.UserEntity;
import lombok.Data;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CurrentUser {

    private SessionUser sessionUser;

    public CurrentUser(UserEntity userEntity) {
        this.sessionUser = SessionUser.mapUserToSessionUser(userEntity);
    }
}
