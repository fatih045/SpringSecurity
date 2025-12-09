package com.example.security1.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private  final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    public  void  senderVerificationMail(String to , String code){


      try {
          MimeMessage message = mailSender.createMimeMessage();

          MimeMessageHelper helper = new MimeMessageHelper(message,true);


            helper.setFrom("muhammedfatihcinar00@gmail.com");
            helper.setTo(to);
            helper.setSubject("\uD83D\uDD10 Email Verification  Code");

            String htmlContent =buildVerificationEmail(code);

            helper.setText(htmlContent, true);
            mailSender.send(message);



      } catch (MessagingException e) {
          throw new RuntimeException(e);
      }


    }

    private String buildVerificationEmail(String code) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>" +
                "        <tr>" +
                "            <td align='center'>" +
                "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);'>" +
                "                    " +
                "                    <!-- Header -->" +
                "                    <tr>" +
                "                        <td style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 20px; text-align: center;'>" +
                "                            <h1 style='color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;'>🔐 Email Verification</h1>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Content -->" +
                "                    <tr>" +
                "                        <td style='padding: 40px 30px;'>" +
                "                            <p style='color: #333333; font-size: 16px; line-height: 24px; margin: 0 0 20px 0;'>" +
                "                                Welcome! Thank you for registering." +
                "                            </p>" +
                "                            <p style='color: #666666; font-size: 14px; line-height: 22px; margin: 0 0 30px 0;'>" +
                "                                Please use the verification code below to complete your registration:" +
                "                            </p>" +
                "                            " +
                "                            <!-- Verification Code Box -->" +
                "                            <table width='100%' cellpadding='0' cellspacing='0'>" +
                "                                <tr>" +
                "                                    <td align='center' style='padding: 20px;'>" +
                "                                        <div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 10px; padding: 20px; display: inline-block;'>" +
                "                                            <span style='color: #ffffff; font-size: 32px; font-weight: bold; letter-spacing: 8px; font-family: monospace;'>" +
                code +
                "                                            </span>" +
                "                                        </div>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                            " +
                "                            <p style='color: #999999; font-size: 13px; line-height: 20px; margin: 30px 0 0 0; text-align: center;'>" +
                "                                ⏰ This code will expire in <strong style='color: #667eea;'>15 minutes</strong>" +
                "                            </p>" +
                "                            " +
                "                            <p style='color: #999999; font-size: 12px; line-height: 18px; margin: 20px 0 0 0; text-align: center;'>" +
                "                                If you didn't request this code, please ignore this email." +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Footer -->" +
                "                    <tr>" +
                "                        <td style='background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #e0e0e0;'>" +
                "                            <p style='color: #999999; font-size: 12px; margin: 0;'>" +
                "                                © 2024 Your App Name. All rights reserved." +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
    }
    }









































//@Service
//public class EmailService {
//
//    private  final JavaMailSender mailSender;
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//
//
//    public  void  senderVerificationMail(String to , String code){
//
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("muhammedfatihcinar00@gmail.com");
//        message.setTo(to);
//        message.setSubject("Email Verification  Code");
//        message.setText("Your verification code is: " + code+"\n\nThis code will expire in 15 minutes.");
//
//        mailSender.send(message);
//    }
//}