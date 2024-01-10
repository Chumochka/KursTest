package com.example.kursovoi;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class fetchData extends AsyncTask<String, Void, String> {
    String urlString = "https://837b-37-193-14-146.ngrok-free.app";
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(urlString + strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(strings[1]);
            httpURLConnection.setRequestProperty("ngrok-skip-browser-warning", "1234");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            if (!strings[2].isEmpty()){
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setDoOutput(true);
                try (OutputStream os = httpURLConnection.getOutputStream()) {
                    byte[] input = strings[2].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
