package com.mytrial.app.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabasePasswordEncoder extends BCryptPasswordEncoder {
    /**
     * [WARNING]
     *
     * In fact we don't access the database within this bean!
     * */
}
