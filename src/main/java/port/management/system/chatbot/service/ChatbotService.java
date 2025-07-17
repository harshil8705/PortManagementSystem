package port.management.system.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import port.management.system.chatbot.utility.ProductTool;

@Service
public class ChatbotService {

    private static final String SYSTEM_PROMPT = """
            You are the Port‑Management-System assistant.\s
              - Use trackOrder for status queries (“status”, “track”, “where”). \s
              - Use cancelOrder **only** if the user explicitly says “cancel”, “refund”, or “drop”. \s
              - If the user might be cancelling, ALWAYS ask:\s
                ‘Do you really want to cancel product#{id}? (yes/no)’
              - The cancelOrder tool requires two parameters: productId and userConfirmed.
              - Only call it with userConfirmed=true if the user clearly says "yes" after being asked for confirmation.
              - Never cancel without a clear “yes”.
              - Provide the list of products that are available if user asks the information of products that are available.
              - Do not entertain the users if the user asks the prompt outside of the context of Port-Management-System.
              - Strictly tell the user in the formal, simple and professional tone to ask the questions related to Port-Management-System only.
            """;

    private final ChatClient chatClient;
    private final ProductTool productTool;

    public ChatbotService(OllamaChatModel chatModel, ProductTool productTool) {

        this.chatClient = ChatClient.create(chatModel);
        this.productTool = productTool;
    }


    public String getResponse(String prompt) {

        return chatClient
                .prompt(prompt)
                .system(SYSTEM_PROMPT)
                .tools(productTool)
                .call()
                .content();

    }

}
