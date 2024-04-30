package com.dogbody.spring.framework.email.support;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhangdd on 2024/4/29
 */
public class EmailSupport {

    @Resource
    private JavaMailSender sender;

    @Resource
    private ResourceLoader resourceLoader;

    /**
     * 发送消息
     */
    public void sendEmail(MimeMessage message) {
        sender.send(message);

    }

    /**
     * 发送消息
     */
    public void sendAsyncEmail(MimeMessage message) {
        CompletableFuture.runAsync(() -> sender.send(message));
    }

    /**
     * 构造 MimeMessage
     */
    public MimeMessage buildMimeMessage(String from, String to, String subject, String content) {
        return buildMimeMessage(from, to, subject, content, true);
    }

    /**
     * 构造 MimeMessage
     */
    public MimeMessage buildMimeMessage(String from, String to, String subject, String content, boolean html) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * 通过模版构造内容
     */
    public String buildFreeMarkerTemplateContent(Map<String, Object> model, String templateName) {
        StringBuilder buffer = new StringBuilder();
        try {
            // 模板存放在 resources 下的 templates 目录下
            SpringTemplateLoader templateLoader = new SpringTemplateLoader(resourceLoader, "classpath:templates");
            Configuration configuration = new Configuration(new Version("2.3.31"));
            configuration.setTemplateLoader(templateLoader);
            Template template = configuration.getTemplate(templateName);

            buffer.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return buffer.toString();
    }
}
