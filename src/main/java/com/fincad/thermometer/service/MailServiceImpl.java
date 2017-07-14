package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Threshold;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roberto on 21/06/17.
 */
@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;
    private Configuration freemarkerConfig;

    @Value("${spring.mail.environment}")
    private String environment;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, Configuration configuration) {
        this.mailSender = mailSender;
        this.freemarkerConfig = configuration;
    }

    private void sendEmail(String templateName, Map<String, Object> model, String to, String subject) throws Exception {
        if ("test".equalsIgnoreCase(environment)) {
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // set loading location to src/main/resources
        // You may want to use a subfolder such as /templates here
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

        Template t = freemarkerConfig.getTemplate(templateName);

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setTo(to);
        helper.setText(text, true); // set to html
        helper.setSubject(subject);

        mailSender.send(message);
    }


    @Override
    public void sendNotification(Threshold threshold) throws Exception {
        Map<String, Object> model = new HashMap<>();

        model.put("user", threshold.getEmail());
        model.put("location", threshold.getLocation());
        model.put("temperature", threshold.getTargetTemperature());

        sendEmail("notification.ftl", model, threshold.getEmail(), "Account Confirmation");
    }
}
