package TestClient;

/**
 * Created by ferrell3 on 2/6/18.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import Models.Command;
import Models.Request;
import Models.Result;

import static java.net.HttpURLConnection.HTTP_OK;

public class TestClientCommunicator {

    private static TestClientCommunicator myInstance = new TestClientCommunicator();

    public static TestClientCommunicator getInstance() { return myInstance; }

    private String serverHost = "192.168.2.175"; //"10.24.66.130";
    private String serverPort = "8888";

    private TestClientCommunicator() {}

//    public Result send(String urlPath, Request reqInfo){
//        String url = "http://" + serverHost + ":" + serverPort + "/" + urlPath;
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonStr = gson.toJson(reqInfo);
//        return post(url, jsonStr);
//    }

    public Result sendCommand(Command command){
        String url = "http://" + serverHost + ":" + serverPort + "/";

        Gson gson = new GsonBuilder().create();
        String jsonStr = gson.toJson(command);
        return post(url, jsonStr);
    }


    private Result post(String urlAddress, String reqData)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Result response = new Result();
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //REQUEST
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();

            OutputStream reqBody = conn.getOutputStream();

            OutputStreamWriter sw = new OutputStreamWriter(reqBody);
            sw.write(reqData);
            sw.flush();

            reqBody.close();

            //RESPONSE
            if(conn.getResponseCode() == HTTP_OK)
            {
                response.setSuccess(true);
            }
            else
            {
                response.setSuccess(false);
                response.setErrorMsg("ERROR: " + conn.getResponseMessage());
            }

            Reader read = new InputStreamReader(conn.getInputStream());
            response = gson.fromJson(read, Result.class);
            read.close();
        } catch (Exception e) {
            response.setErrorMsg(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }


}
