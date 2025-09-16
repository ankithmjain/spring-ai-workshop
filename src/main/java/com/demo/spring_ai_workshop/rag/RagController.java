package com.demo.spring_ai_workshop.rag;

import com.demo.spring_ai_workshop.chat.ChatController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RagController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/rag/chicago-tourist-ai-with-rag")
    public List<Document> chicagoGuideWithRAGResponse(@RequestParam(value = "message", defaultValue = "You are my Chicago Tourist Virtual AI Assistant") String message) {
        List<Document> rawDocs = vectorStore.similaritySearch(message);
        return rawDocs;
    }

    /**
     * Cleans extracted text by removing excess whitespace,
     * fixing common ligatures, and truncating if too long.
     */
    private String cleanText(String text) {
        if (text == null) return "";

        // Replace multiple whitespace chars with single space
        String cleaned = text.replaceAll("\\s+", " ").trim();

        // Optionally fix ligatures or OCR errors here

        return cleaned;
    }

}


