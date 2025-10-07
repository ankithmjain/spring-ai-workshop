package com.demo.spring_ai_workshop.rag;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class IngestionService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;

    @Value("classpath:/docs/chicago2025travelguide.pdf")
    private Resource chicagoCityTouristPDF;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {
        try {
            var pdfReader = new PagePdfDocumentReader(chicagoCityTouristPDF);
            TextSplitter textSplitter = new TokenTextSplitter();

            var rawDocuments = textSplitter.apply(pdfReader.get());

            List<Document> cleanedDocuments = new ArrayList<>();
            String fileName = chicagoCityTouristPDF.getFilename();

            for (Document doc : rawDocuments) {
                String originalText = doc.getText();
                // Create a new Document with cleaned text and updated metadata
                Document cleanedDoc = new Document(originalText, doc.getMetadata());
                cleanedDocuments.add(cleanedDoc);
            }

            vectorStore.accept(cleanedDocuments);
            log.info("VectorStore loaded with {} cleaned documents.", cleanedDocuments.size());
        } catch (Exception e) {
            log.error("Error during ingestion process", e);
            throw new RuntimeException("Failed to ingest documents", e);
        }
    }


}
