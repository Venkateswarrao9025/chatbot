package com.example.springboot.service;

import com.example.springboot.dto.ChatDtos.ChatRequest;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
  private final LlmClient llmClient;

  public ChatService(LlmClient llmClient) {
    this.llmClient = llmClient;
  }

  public String reply(ChatRequest request) {
    return llmClient.generateReply(request);
  }
}
