package com.example.springboot.service;

import com.example.springboot.dto.ChatDtos.ChatRequest;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class OpenAiLlmClient implements LlmClient {

  private final OpenAIClient client;

  public OpenAiLlmClient() {
    String apiKey = System.getenv("OPENAI_API_KEY");
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("OPENAI_API_KEY is missing. Set it as an environment variable.");
    }
    this.client = OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .build();
  }

  @Override
  public String generateReply(ChatRequest request) {
    String lastUser = request.messages().get(request.messages().size() - 1).content();

    ResponseCreateParams params = ResponseCreateParams.builder()
        .model("gpt-5.2")
        .input(lastUser)
        .build();

    Response response = client.responses().create(params);

    StringBuilder sb = new StringBuilder();

    if (response.output() != null) {
      for (var item : response.output()) {

        // message() returns Optional<ResponseOutputMessage>
        var msgOpt = item.message();
        if (msgOpt == null || msgOpt.isEmpty()) continue;

        var msg = msgOpt.get();
        if (msg.content() == null) continue;

        for (var content : msg.content()) {

          // outputText() returns Optional<ResponseOutputText>
          var outTextOpt = content.outputText();
          if (outTextOpt == null || outTextOpt.isEmpty()) continue;

          var outText = outTextOpt.get();
          if (outText.text() != null) {
            sb.append(outText.text());
          }
        }
      }
    }

    String text = sb.toString().trim();
    return text.isEmpty() ? "No text returned from model." : text;
  }
}
