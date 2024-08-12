/*******************************************************************************
 * Copyright (c) 2024  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors to this module:
 *     W. Gibaut
 ******************************************************************************/

package br.unicamp.cst.bindings.openai;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.io.rest.HttpCodelet;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author wander
 */
public abstract class GPTCodelet extends HttpCodelet {
    SimpleOpenAI openAI;
    HashMap<String, Object> config;

    public GPTCodelet (){
        super();
        buildOpenAI();
        config = configDefaultOpenAI();
    }

    public GPTCodelet(HashMap<String, Object> config){
        super();
        buildOpenAI();
        this.config = config;
    }

    public GPTCodelet(HashMap<String, Object> config, String openApiKey){
        super();
        buildOpenAI(openApiKey);
        this.config = config;
    }

    public GPTCodelet(String openApiKey){
        super();
        buildOpenAI(openApiKey);
        config = configDefaultOpenAI();
    }

    private void buildOpenAI (){
        openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
    }

    private void buildOpenAI (String openApiKey){
        openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv(openApiKey))
                .build();
    }


    public HashMap<String, Object> configDefaultOpenAI(){
        HashMap<String, Object> config = new HashMap<>();
        config.put("temperature", 0.0);
        config.put("maxTokens", 300);
        return config;
    }

    /*public HashMap<String, String> configOpenAI(){
        HashMap<String, String> config = new HashMap<>();

        return config;
    }*/

    public String completionOpenAI(String systemContent, String userContent){
        ChatRequest chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(ChatMessage.SystemMessage.of(systemContent))
                .message(ChatMessage.UserMessage.of(userContent))
                .temperature((Double)config.get("temperature"))
                .maxTokens((Integer)config.get("maxTokens"))
                .build();
        CompletableFuture<Chat> futureChat = openAI.chatCompletions().create(chatRequest);
        Chat chatResponse = futureChat.join();
        //System.out.println(chatResponse.firstContent());
        return chatResponse.firstContent();
    }

    public String completionOpenAI(String userContent){
        /*ChatRequest chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(ChatMessage.SystemMessage.of("You are an expert in AI."))
                .message(ChatMessage.UserMessage.of(userContent))
                .temperature(0.0)
                .maxTokens(300)
                .build();
        CompletableFuture<Chat> futureChat = openAI.chatCompletions().create(chatRequest);
        Chat chatResponse = futureChat.join();
        //System.out.println(chatResponse.firstContent());
        return chatResponse.firstContent();*/
        return completionOpenAI("You are an expert in AI.", userContent);
    }
}