package br.com.alura.screensound.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ConsultaGemini {
    public static String obterInformacao(String texto) {
        OkHttpClient client = new OkHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", new org.json.JSONArray().put(new JSONObject()
                .put("parts", new org.json.JSONArray().put(new JSONObject().put("text", "me fale sobre o artista: " + texto)))));

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=API_KEY")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                JSONArray candidates = jsonResponse.optJSONArray("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    JSONObject firstCandidate = candidates.getJSONObject(0);
                    JSONObject content = firstCandidate.optJSONObject("content");
                    if (content != null) {
                        JSONArray parts = content.optJSONArray("parts");
                        if (parts != null && !parts.isEmpty()) {
                            return parts.getJSONObject(0).optString("text", "Texto não encontrado.");
                        }
                    }
                }
            } else {
                System.out.println("Erro na requisição: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
