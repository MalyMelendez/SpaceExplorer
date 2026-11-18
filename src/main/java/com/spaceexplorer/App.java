
package com.spaceexplorer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONObject;

public class App {

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to Space Explorer!");
        System.out.print("Enter a date (YYYY-MM-DD): ");

        String date = input.nextLine();

        String response = getAstronomyData(date);

        JSONObject astronomyData = new JSONObject(response);

        String title = astronomyData.getString("title");
        String imageDate = astronomyData.getString("date");
        String explanation = cleanExplanation(
                astronomyData.getString("explanation"));
        String imageURL = astronomyData.getString("hdurl");

        System.out.println("\nSPACE EXPLORER");
        System.out.println("-------------------------");
        System.out.println("Title: " + title);
        System.out.println("Date: " + imageDate);
        System.out.println("Explanation: " + explanation);
        System.out.println("Image URL: " + imageURL);

        input.close();
    }

    public static String getAstronomyData(String date) throws Exception {

        String formattedDate = date.substring(2).replace("-", "");

        String url = "https://science.nasa.gov/wp-json/wp/v2/apod-basic/"
                + formattedDate;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return "Error: " + response.statusCode();
        }
    }

    public static String cleanExplanation(String explanation) {

        return explanation.replaceAll("<[^>]*>", "")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");
    }
}
