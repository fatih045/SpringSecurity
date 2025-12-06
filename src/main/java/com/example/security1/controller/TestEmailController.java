package com.example.security1.controller;


import com.example.security1.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-email")
public class TestEmailController {


    private  final EmailService emailService;
    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-mail")
    public String testEmail(@RequestParam String email) {
        try {
            String testCode = "123456";
            emailService.senderVerificationMail(email, testCode);
            return "Test email sent to " + email;
        }
        catch (Exception e){
            return "Failed to send email: " + e.getMessage();
        }
    }
}
