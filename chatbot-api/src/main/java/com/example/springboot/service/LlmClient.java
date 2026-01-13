package com.example.springboot.service;

import com.example.springboot.dto.ChatDtos.ChatRequest;

public interface LlmClient {
  String generateReply(ChatRequest request);
}
