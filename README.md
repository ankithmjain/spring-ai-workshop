# Spring AI Workshop

This project demonstrates Spring AI integration with various language models, showcasing both simple prompt-response interactions and more advanced Retrieval-Augmented Generation (RAG) capabilities.

## Features

- Basic AI chat generation endpoints
- Streaming responses
- Guardrailed AI assistants
- RAG implementation with vector databases
- Chicago tourist guide example application

## Requirements

- Java 17+
- Spring Boot 3.x
- Gradle
- PostgreSQL with PGVector extension (for vector storage)

## Getting Started

1. Clone this repository
2. Configure your database in `application.yml` refer https://github.com/pgvector/pgvector
3. Run the application with `./gradlew bootRun`

## API Endpoints

### Chat Endpoints

- `GET /chat/gen-ai?userInput=<prompt>` - Simple generation endpoint
- `GET /chat/city-guide?message=<prompt>` - Streaming response for Chicago guide
- `GET /chat/tourist-guide-with-guard-rails?message=<prompt>` - Guide with strict guardrails
- `GET /chat/tourist-guide-with-model-details?message=<prompt>` - Full response with model metadata

### RAG Endpoints

- `GET /rag/chicago-tourist-ai-with-rag?message=<prompt>` - Returns relevant documents from vector store
- `GET /rag/chicago-ai?message=<prompt>` - Chicago AI assistant with RAG capabilities

## Configuration

The application requires proper JDBC configuration for the vector database. Check the application properties file and ensure your database connection is properly set up.

## Troubleshooting

If you encounter the error `Failed to obtain JDBC Connection`, verify your database configuration and ensure the PGVector extension is properly installed.
