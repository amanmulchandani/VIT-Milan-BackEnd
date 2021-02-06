package com.vit.community.springapplication.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/*
* The MailContentBuilder class contains a method which takes the email message
* to be sent as an input and injects it into the HTMl mail template at runtime
* using the Thymeleaf template engine and RETURNS the mail in the HTML format.
*
* The Lombok library generates the all arguments constructor, and the fields
* are automatically autowired (initialised) by Spring at runtime.
* */

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }
}