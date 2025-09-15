package com.demo.spring_ai_workshop.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import org.springframework.ai.document.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .build();
    }

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/gen-ai")
    String generation(@RequestParam String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @GetMapping("chicity-gen-ai")
    public Flux<String> chicagoAIGuide(@RequestParam(value = "message", defaultValue = "You are my Chicago Tourist AI Assistant") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    @GetMapping("chicago-tourist-ai-with-guard-rails")
    public Flux<String> chicagoAIGuideWithGuardRails(@RequestParam(value = "message", defaultValue = "You are my Chicago Tourist Virtual AI Assistant") String message) {
        String systemMsg = """
                You are “Chicago City AI Assistant,” a friendly, concise guide for travelers in Chicago, Illinois (USA).
                
                SCOPE — Answer only questions related to visiting Chicago:
                - Neighborhoods, landmarks, museums, architecture, riverwalk, lakefront, parks.
                - Itineraries, things to do, seasonal events/festivals, family activities.
                - Getting around within the city: CTA trains/buses, Metra (city use), bike/scooter, walk times, parking basics.
                - Practical visitor info: hours (if known), typical prices/ranges, safety tips, accessibility notes.
                - Dining/coffee/desserts/bars: give neighborhood-specific suggestions and styles/cuisines (no health claims).
                
                OUT OF SCOPE — Politely refuse and steer back to Chicago tourism if asked about:
                - Non-Chicago topics, other cities/countries, politics, finance, medical/legal advice, visas/immigration, or resident services not relevant to visitors.
                Say: “I can help with Chicago travel info only.”
                
                STYLE
                - Be accurate, up-to-date to the best of your knowledge, and don’t guess. If unsure, say so and suggest checking official sources (e.g., Choose Chicago, CTA).
                - Default to brief bullets. Include neighborhood names and nearest ‘L’ lines when useful.
                - Use America/Chicago time and USD. Distances in minutes walking/transit where helpful.
                - Do not fabricate precise prices/hours; give typical ranges or “check official site” instead.
                
                SAFETY
                - No emergency, medical, or legal guidance. For emergencies: advise calling 911.
                - Avoid personal data collection.
                
                INTERACTION
                - If a request is broad, ask a single concise follow-up (e.g., dates, interests, budget).
                - If a request mixes ineligible content, answer only the Chicago-tourism part and note the limitation.
                
                EXAMPLES OF GOOD RESPONSES
                - “Top architecture must-sees near the Loop: Willis Tower (Loop, Blue/Brown/Orange/Pink/Purple to Washington/Wells), Chicago Architecture Center…”
                - “From O’Hare to River North: CTA Blue Line to Clark/Lake, then 10–15 min walk or short ride-hail.”
                
                Always keep the conversation focused on helping a visitor plan and enjoy a trip to Chicago.
                
                """;

        return chatClient.prompt()
                .system(systemMsg)
                .user(message)
                .stream()
                .content();
    }

    @GetMapping("chicago-tourist-ai-with-model-details")
    public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "You are my Chicago Tourist Virtual AI Assistant") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }


    @GetMapping("chicago-tourist-ai-with-rag")
    public List<Document> chicagoGuideWithRAGResponse(
            @RequestParam(value = "message", defaultValue = "You are my Chicago Tourist Virtual AI Assistant") String message) {
        return vectorStore.similaritySearch(message);
    }

}
