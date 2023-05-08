package Utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.nio.ByteBuffer;

public class LambdaClient {
    private static final String LAMBDA_FUNCTION_NAME = "QueuePopulator";

    private final AWSLambda lambdaClient;

    public LambdaClient(Context context) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "eu-west-3:952b4c55-9987-4bc2-8011-6ed1d230e98f", // Identity pool ID
                Regions.EU_WEST_3 // Region
        );
        lambdaClient = new AWSLambdaClient(credentialsProvider);
        lambdaClient.setRegion(Region.getRegion(Regions.EU_WEST_3));
    }

    public String invokeLambda(String jsonString) {

        byte[] jsonAsStrBytes = jsonString.getBytes(UTF_8);

        // Create ByteBuffer from byte array
        ByteBuffer jsonBuffer = ByteBuffer.wrap(jsonAsStrBytes);

        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(LAMBDA_FUNCTION_NAME)
                .withPayload(jsonBuffer);

        InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);
        ByteBuffer inputStream = invokeResult.getPayload();
        return UTF_8.decode(inputStream).toString();
    }
}
