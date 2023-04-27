package Entities;

import android.os.AsyncTask;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sagemakerruntime.AmazonSageMakerRuntimeClient;
import com.amazonaws.services.sagemakerruntime.model.InvokeEndpointRequest;
import com.amazonaws.services.sagemakerruntime.model.InvokeEndpointResult;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class EndpointRequest extends AsyncTask<String, Void, String> {

    private final AmazonSageMakerRuntimeClient sagemakerClient;

    public EndpointRequest() {

        this.sagemakerClient = new AmazonSageMakerRuntimeClient(
                new BasicAWSCredentials("ACCESS_KEY", "SECRET_KEY")
        );

        this.sagemakerClient.setRegion(Region.getRegion(Regions.EU_WEST_3));

        this.sagemakerClient.setEndpoint("biriefing-about-it-endpoint");

    }

    @Override
    protected String doInBackground(String... params) {
        String requestBodyJson = params[0];
        InvokeEndpointRequest invokeEndpointRequest = new InvokeEndpointRequest();
        byte[] bytes = requestBodyJson.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        invokeEndpointRequest.setBody(buffer);
        invokeEndpointRequest.setContentType("application/json");
        invokeEndpointRequest.setAccept("application/json");
        invokeEndpointRequest.setEndpointName("biriefing-about-it-endpoint");
        InvokeEndpointResult invokeEndpointResult = this.sagemakerClient.invokeEndpoint(invokeEndpointRequest);
        ByteBuffer returnedBuffer = invokeEndpointResult.getBody();
        return new String(returnedBuffer.array(), StandardCharsets.UTF_8);
    }

    @Override
    protected void onPostExecute(String result) {
        // Process the result here
    }
}
