package com.example.demo.exception;

import org.springframework.mail.MailException;

public class SpringRedditException extends Throwable {
    public SpringRedditException(String exMessage, MailException e) {
        super(exMessage);

    }

    public SpringRedditException(String invalid_token) {
    }
}
