package magicfour.garageopener;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aumas on 2016/9/28.
 */
public class WebService {
    private URL url;
    private HttpURLConnection connection;
    public WebService() {
        try {
            url = new URL("http://192.168.0.153/");
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

    }
/*
    public String IsGarageDoorOpen(){
        for(int i = 0; i < 5; i++){
            String s = MakeHTTPCall("checkdoor");
            if(s.equals("failed") == false)
                return s;	//Got a response return result
        }
        return "failed"; 	//Oh well we tried 5 times without success
    }
    public String ActivateGarageDoor(){
        for(int i=0; i < 5; i++){
            String s = MakeHTTPCall("activatedoor");
            if(s.equals("failed") == false)
                return s;	//Got a response return result
        }
        return "failed";
    }

    private String MakeHTTPCall(String method){
        HttpGet get = new HttpGet(WEBSERVICE_URL + method);
        try {
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = client.execute(get, responseHandler);
            return responseBody;
        } catch (IOException e) {							// -|
            e.printStackTrace();							//	|--> Oops something went wrong!
        } catch (Exception e) {								//	|
            e.printStackTrace();							// -|
        }
        return "failed";									//Return "failed" if we encounter an error
    }*/
}
