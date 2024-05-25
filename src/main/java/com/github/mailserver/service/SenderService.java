package com.github.mailserver.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SenderService {
    @Autowired
    private JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;

    public SenderService(ResourceLoader resourceLoader) {
        this.templateEngine= new TemplateEngine();
        this.resourceLoader = resourceLoader;
    }

    @Async
    public void sendEmail(String to, String name) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/email-template.html");
            String html = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

            Context context = new Context();
            context.setVariable("name", name);

            // Processar o template HTML com Thymeleaf
            String htmlBody = templateEngine.process(html, context);

            // Criar a mensagem MIME
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Assunto do E-mail");
            helper.setText(htmlBody, true);

            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Não foi possivel enviar o email");
        }
    }
}