import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;

import Models.GenericCommand;

public class handler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {
                // Extract the JSON string from the HTTP request body
                Reader read = new InputStreamReader(httpExchange.getRequestBody());
                GenericCommand command = gson.fromJson(read, GenericCommand.class);
                read.close();
//                Results result = new Results();

                // TODO: execute command
                try {
                    //result = command.execute();
                    command.execute();
//                    result.setSuccess(true);
                }catch (NumberFormatException e)
                {
//                    result.setErrorInfo("Sorry, that's not a valid number format");
//                    result.setSuccess(false);
                    System.out.println("ERROR");
                }

                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //String gsonResponse = gson.toJson(result);

                PrintWriter pw = new PrintWriter(httpExchange.getResponseBody());
                //pw.write(gsonResponse);
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
