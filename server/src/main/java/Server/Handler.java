package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;

import Models.Command;
import Models.Result;

public class Handler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {
                // Extract the JSON string from the HTTP request body
                Reader read = new InputStreamReader(httpExchange.getRequestBody());
                Command command = gson.fromJson(read, Command.class);
                read.close();
                Result result = new Result();

                // execute command
                try {
                    result = command.execute();
                }catch (Exception e)
                {
                    result.setSuccess(false);
                    System.out.println("ERROR");
                    e.printStackTrace();
                }

                //Create response for client
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                String gsonResponse = gson.toJson(result);

                PrintWriter pw = new PrintWriter(httpExchange.getResponseBody());
                pw.write(gsonResponse);
                pw.close();

                success = true;
            }
            if (!success) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                httpExchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            httpExchange.getResponseBody().close();

            e.printStackTrace();
        }
    }
}
