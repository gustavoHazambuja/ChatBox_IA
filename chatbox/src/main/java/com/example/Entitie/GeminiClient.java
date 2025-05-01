package com.example.Entitie;

import com.google.gson.*; // Biblioteca para manipulação de JSON
import okhttp3.*; // Cliente HTTP para enviar requisições

import java.io.IOException;


// Classe responsável por fazer requisições à API do Gemini
// Envia uma pergunta e recebe uma resposta com tom sarcástico/ranzinza
public class GeminiClient {
    
    private static final String API_KEY = "AIzaSyB8wAgUd90VVrSkHv-LhYvaYCOUs242zQc"; // Chave da API
    // Endereço da API
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final OkHttpClient client; // Cliente HTTP para enviar requesições
    private final Gson gson; // Conversor de JSON


    public GeminiClient(){
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // Método principal que envia a pergunta à API e retorna a resposta gerada
    public String perguntar(String pergunta) throws IOException{

        // Monta o JSON com o texto da pergunta (incluindo instrução de tom sarcástico)
        JsonObject content = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", "Responda com sarcasmo e tom de professor ranzinza: " + pergunta);

        // Empacota a estrutura JSON esperada pela API
        JsonObject innerContent = new JsonObject();
        JsonArray parts = new JsonArray();
        parts.add(part);
        innerContent.add("parts", parts);
        contents.add(innerContent);
        content.add("contents", contents);

        // Define o corpo da requisição com o tipo "application/json"
        RequestBody body = RequestBody.create(
            content.toString(),
            MediaType.parse("application/json")
        );

        // Constrói a requisição POST com o endpoint e o corpo
        Request request = new Request.Builder()
            .url(ENDPOINT)
            .post(body)
            .build();

            // Executa a requisição e trata a resposta
           try(Response response = client.newCall(request).execute()){
                if(!response.isSuccessful()) throw new IOException("Erro: " + response);

                 // Converte a resposta JSON em objeto manipulável
                JsonObject jsonResponse = gson.fromJson(response.body().string(), JsonObject.class);
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");

                // Extrai o texto da resposta do primeiro candidato, se houver
                if(candidates != null && candidates.size() > 0){
                    JsonObject contetObj = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                    JsonArray partsArray = contetObj.getAsJsonArray("parts");
                    return partsArray.get(0).getAsJsonObject().get("text").getAsString();
                }
                else{
                    return "Nada de resposta... nem o Gemini quis falar contigo!";
                }
           } 
    }
    
}
