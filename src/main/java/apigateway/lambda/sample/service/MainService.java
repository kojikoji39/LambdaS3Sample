package apigateway.lambda.sample.service;

import java.io.IOException;

public class MainService {

  private static final MainService mainService =
      new MainService();

  // S3アクセスサービス
  private final SampleAccessS3Service accessS3Service = SampleAccessS3Service.getInstance();

  public static MainService getInstance() {
    return mainService;
  }
  
  public String requestReceive(String uid) throws IOException {
    accessS3Service.getDirectoryTree(uid);
    return uid;
  }

}
