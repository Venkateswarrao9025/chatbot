package com.example.springboot.dto;

import java.util.List;

public class ChatDtos {
public record ChatMessage(String role, String content) {}
  public record ChatRequest(List<ChatMessage> messages) {}
  public record ChatResponse(String reply) {}
}
