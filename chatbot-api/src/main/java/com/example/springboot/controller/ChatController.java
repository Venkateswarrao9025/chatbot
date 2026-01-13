package com.example.springboot.controller;

import com.example.springboot.dto.ChatDtos.ChatRequest;
import com.example.springboot.dto.ChatDtos.ChatResponse;
import com.example.springboot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping("/chat")
  public ChatResponse chat(@RequestBody ChatRequest request) {
    try {
      String reply = chatService.reply(request);
      System.out.println("✅ Reply generated length=" + (reply == null ? 0 : reply.length()));
      return new ChatResponse(reply);
    } catch (Exception e) {
      e.printStackTrace(); // IMPORTANT: shows the real reason in terminal
      return new ChatResponse("❌ Backend error: " + e.getMessage());
    }
  }
}
