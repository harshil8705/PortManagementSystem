package port.management.system.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import port.management.system.chatbot.service.ChatbotService;

@RestController
@RequestMapping("/api")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping("/public/ask-ai")
    public String generateResponse(String prompt) {

        return chatbotService.getResponse(prompt);

    }

}
