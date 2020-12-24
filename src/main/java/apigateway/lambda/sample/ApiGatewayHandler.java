package apigateway.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
// import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import apigateway.lambda.sample.service.MainService;
// import java.util.Map;
import java.util.HashMap;

public class ApiGatewayHandler
    implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  // メイン処理担当クラス
  private final MainService mainService = MainService.getInstance();

  @Override
  public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent event,
      Context context) {

    String responseBody = "";
    try {
      // path parameter取得
      String uid = event.getPathParameters().get("user-id");
      System.out.println("UerId: " + uid);
      responseBody = mainService.requestReceive(uid);
    } catch (Exception e) {
      e.printStackTrace();
    }

    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");
    response.setHeaders(headers);
    response.setBody(responseBody);

    // log execution details
    // LambdaLogger logger = context.getLogger();
    // Util.logEnvironment(event, context, gson);

    return response;
  }
}
