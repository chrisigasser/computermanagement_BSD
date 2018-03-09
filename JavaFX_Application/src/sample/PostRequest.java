package sample;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;
public class PostRequest {

    private String pcname;
    private String starttime;
    private String endtime;
    private String apiURL;

    public PostRequest(String pcname, String starttime, String endtime) {
        this.pcname = pcname;
        this.starttime = starttime;
        this.endtime = endtime;
        this.apiURL = "http://localhost:3000/addTimerEntry";
    }

    public void makeHTTPPOSTRequest() {

        try {
            String       postUrl       = this.apiURL;// put in your url
            HttpClient httpClient    = HttpClientBuilder.create().build();
            HttpPost     post          = new HttpPost(postUrl);
            StringEntity postingString = null;
            postingString = new StringEntity("{\"pcname\":\"" + this.pcname + "\",\"starttime\":\"" + this.starttime + "\",\"endtime\":\"" + this.endtime + "\"}");
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);

        }catch (Exception ex) {

            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }


    }
}
