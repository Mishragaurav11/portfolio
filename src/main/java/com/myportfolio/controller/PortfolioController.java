package com.myportfolio.controller;

import com.myportfolio.model.ContactForm;
import com.myportfolio.service.EmailService;
import com.myportfolio.service.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final EmailService emailService;
    private final RateLimitService rateLimitService;

    
   
    @GetMapping("/home")
     @ResponseBody
     public String test() {
         return "Controller Working";
     }

    
    @GetMapping("/")
    public String home(Model model) {

        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }

        return "index";
    }

    
    
  /*  @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("contactForm", new ContactForm());

        return "index";
    }
*/
    
    @PostMapping("/send-mail")
    public String sendMail(
            @ModelAttribute("contactForm") ContactForm form,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // Honeypot Check
        if (form.getCompany() != null && !form.getCompany().isEmpty()) {
            return "redirect:/?spamDetected";
        }

        // Rate Limit Check
        String clientIp = request.getRemoteAddr();

        if (!rateLimitService.allowRequest(clientIp)) {
            redirectAttributes.addFlashAttribute("error",
                    "Too many requests. Please try after sometime.");
            return "redirect:/";
        }

        try {

            // Mail to you
           emailService.sendContactMailToMe(form.getName(),form.getEmail(),form.getSubject(),form.getMessage());

            // Auto reply
           emailService.sendAutoReplyToUser(form.getEmail(),form.getName());

        	//if (2 == 2) 
            redirectAttributes.addFlashAttribute("success","Message Sent Successfully!");

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute("error",
                    "Mail service temporarily unavailable. Please try later.");
        }

        return "redirect:/";
    }

    
    
    
    
    /*
    @PostMapping("/send-mail")
    public String sendMail(
    		@ModelAttribute("contactForm") ContactForm form,
            HttpServletRequest request,
            Model model) {

        // Honeypot Check (Bot Protection)
        if (form.getCompany() != null && !form.getCompany().isEmpty()) {
            return "redirect:/?spamDetected";
        }

        // Rate Limit Check
        String clientIp = request.getRemoteAddr();

        if (!rateLimitService.allowRequest(clientIp)) {
            model.addAttribute("error", "Too many requests. Please try after sometime.");
            return "index";
        }


        try {

            // Mail to you
            emailService.sendContactMailToMe(
                    form.getName(),
                    form.getEmail(),
                    form.getSubject(),
                    form.getMessage()
            );

            // Auto reply
            emailService.sendAutoReplyToUser(
                    form.getEmail(),
                    form.getName()
            );

            model.addAttribute("success", "Message Sent Successfully!");

        } catch (Exception e) {

            e.printStackTrace(); // Debug log

            model.addAttribute("error",
                    "Mail service temporarily unavailable. Please try later.");

            return "index";
        }

        return "index";
    }
*/

    
    
    
    @GetMapping("/resume")
    public ResponseEntity<InputStreamResource> downloadResume() {

        InputStream file = getClass()
                .getResourceAsStream("/static/resume.pdf");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(file));
    }
}
