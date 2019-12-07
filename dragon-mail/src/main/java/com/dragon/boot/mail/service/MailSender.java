package com.dragon.boot.mail.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import com.dragon.boot.common.model.Result;
import com.dragon.boot.mail.model.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MailSender
 * @Author pengl
 * @Date 2019-10-09 16:48
 * @Description 邮件发送服务
 * @Version 1.0
 */

/**
 * # 邮件发送配置
 * spring.mail.host=mail.xxx.com
 * spring.mail.username=xx
 * spring.mail.password=xx
 * spring.mail.protocol=smtp
 * spring.mail.properties.mail.smtp.auth=true
 * spring.mail.properties.mail.smtp.port=465
 * spring.mail.properties.mail.display.sendmail=xx@qq.com
 * spring.mail.properties.mail.smtp.starttls.enable=true
 * spring.mail.properties.mail.smtp.starttls.required=true
 * spring.mail.properties.mail.smtp.ssl.enable=true
 * spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
 * spring.mail.properties.mail.smtp.socketFactory.fallback=false
 * spring.mail.default-encoding=utf-8
 * spring.mail.from=xx@qq.com
 * # 所有附件最大长度（单位字节，默认100M）
 * spring.mail.maxUploadSize=104857600
 * spring.mail.maxInMemorySize=4096
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "dragon.boot.email.enable", havingValue = "true", matchIfMissing = true)
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * @MethodName: send
     * @Author: pengl
     * @Date: 2019-10-31 13:38
     * @Description: 发送邮件
     * @Version: 1.0
     * @Param: [to, content, subject]
     * @Return: com.dragon.boot.common.model.Result
     **/
    public Result send(String to, String content, String subject) {
        return send(MailDto.builder().to(to).content(content).subject(subject).build());
    }

    /**
     * @MethodName: send
     * @Author: pengl
     * @Date: 2019-10-31 13:39
     * @Description: 发送邮件(抄送)
     * @Version: 1.0
     * @Param: [to, content, subject, cc]
     * @Return: com.dragon.boot.common.model.Result
     **/
    public Result send(String to, String content, String subject, String cc) {
        return send(MailDto.builder().to(to).content(content).subject(subject).cc(cc).build());
    }

    /**
     * @MethodName: sendTemplate
     * @Author: pengl
     * @Date: 2019-10-31 13:39
     * @Description: 发送模版邮件
     * @Version: 1.0
     * @Param: [to, model, template, subject]
     * @Return: com.dragon.boot.common.model.Result
     **/
    public Result sendTemplate(String to, Map<String, Object> model, String template, String subject) {
        return send(MailDto.builder().to(to).content(getTemplateStr(model, template)).subject(subject).build());
    }

    /**
     * @MethodName: sendTemplate
     * @Author: pengl
     * @Date: 2019-10-31 13:39
     * @Description: 发送模版邮件(带抄送)
     * @Version: 1.0
     * @Param: [to, model, template, subject, cc]
     * @Return: com.dragon.boot.common.model.Result
     **/
    public Result sendTemplate(String to, Map<String, Object> model, String template, String subject, String cc) {
        return send(MailDto.builder().to(to).content(getTemplateStr(model, template)).subject(subject).cc(cc).build());
    }

    /**
     * @MethodName: getTemplateStr
     * @Author: pengl
     * @Date: 2019-10-31 13:38
     * @Description: 解析freemark模版
     * @Version: 1.0
     * @Param: [model, template]
     * @Return: java.lang.String
     **/
    private String getTemplateStr(Map<String, Object> model, String template) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate(template), model);
        } catch (Exception e) {
            log.error("获取模版数据异常：{}", e.getMessage(), e);
        }
        return "";
    }

    /**
     * @MethodName: send
     * @Author: pengl
     * @Date: 2019-10-31 13:34
     * @Description: 发送邮件
     * @Version: 1.0
     * @Param: [mailDto]
     * @Return: com.dragon.boot.common.model.Result
     **/
    public Result send(MailDto mailDto) {

        if (StringUtils.isAnyBlank(mailDto.getTo(), mailDto.getContent())) {
            return new Result(false, 1001, "接收人或邮件内容不能为空");
        }

        String[] tos = filterEmail(mailDto.getTo().split(","));
        if (tos == null) {
            log.error("邮件发送失败，接收人邮箱格式不正确：{}", mailDto.getTo());
            return new Result(false, 1002, "");
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setText(mailDto.getContent(), true);
            helper.setSubject(mailDto.getSubject());

            //抄送
            String[] ccs = filterEmail(mailDto.getCc().split(","));
            if (ccs != null) {
                helper.setCc(ccs);
            }

            //秘抄
            String[] bccs = filterEmail(mailDto.getBcc().split(","));
            if (bccs != null) {
                helper.setBcc(bccs);
            }

            //定时发送
            if (mailDto.getSendDate() != null) {
                helper.setSentDate(mailDto.getSendDate());
            }

            //附件
            File[] files = mailDto.getFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    helper.addAttachment(file.getName(), file);
                }
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("邮件发送异常：{}", e.getMessage(), e);
            return new Result(false, 1099, "邮件发送异常：" + e.getMessage());
        }
        return new Result();
    }

    /**
     * 邮箱格式校验过滤
     *
     * @param emails
     * @return
     */
    private String[] filterEmail(String[] emails) {
        List<String> list = Arrays.asList(emails);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        list = list.stream().filter(e -> checkEmail(e)).collect(Collectors.toList());
        return list.toArray(new String[list.size()]);
    }

    private boolean checkEmail(String email) {
        return ReUtil.isMatch("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", email);
    }
}
