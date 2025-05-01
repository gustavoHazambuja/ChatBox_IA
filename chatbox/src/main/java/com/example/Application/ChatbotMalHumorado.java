package com.example.Application;

import java.io.IOException;
import java.util.Scanner;

import com.example.Entitie.GeminiClient;

// Responsável por receber perguntas do usuário, enviar ao Gemini e exibir as respostas
public class ChatbotMalHumorado {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        GeminiClient gemini = new GeminiClient();

        System.out.println("=== Chatbot Mal-Humorado de Estudo ===");
        System.out.println("Digite sua pergunta técnica (ou 'sair' para encerrar):");

        while(true){
            System.out.println("\nVocê");
            String pergunta = sc.nextLine();

             // Se o usuário digitar 'sair', o programa encerra
            if(pergunta.equalsIgnoreCase("sair")){
                System.out.println("Chatbot: Já vai tarde!");
                break;
            }

            try{
                // Envia a pergunta para o Gemini e obtém a resposta
                String resposta = gemini.perguntar(pergunta);

                System.out.println("\nChatbot: " + resposta);
            }catch(IOException e){
                // Caso ocorra algum erro de rede ou API, exibe uma mensagem
                System.out.println("Chatbot: Tive um chilique aqui, não consegui responder.");
            }


        }

        sc.close();
    }
}
