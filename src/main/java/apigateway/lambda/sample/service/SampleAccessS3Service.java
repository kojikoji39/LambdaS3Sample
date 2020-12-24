package apigateway.lambda.sample.service;

import java.io.IOException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class SampleAccessS3Service {

  private static final SampleAccessS3Service accessS3Service = new SampleAccessS3Service();

  public static SampleAccessS3Service getInstance() {
    return accessS3Service;
  }

  public String getDirectoryTree(String dir) throws IOException {
    
    Regions clientRegion = Regions.DEFAULT_REGION;
    String bucketName = "bucket-suzuki"; // Lambda環境変数にしておいてもいい

    try {
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
          .withCredentials(new ProfileCredentialsProvider()).withRegion(clientRegion).build();

      System.out.println("Listing objects");

      // maxKeys is set to 2 to demonstrate the use of
      // ListObjectsV2Result.getNextContinuationToken()
      ListObjectsV2Request req =
          new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
      ListObjectsV2Result result;

      do {
        result = s3Client.listObjectsV2(req);

        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
          System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
        }
        // If there are more than maxKeys keys in the bucket, get a continuation token
        // and list the next objects.
        String token = result.getNextContinuationToken();
        System.out.println("Next Continuation Token: " + token);
        req.setContinuationToken(token);
      } while (result.isTruncated());

    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      e.printStackTrace();
    }

    // このままだと取ってきたものをログに出力するだけで返却していない
    return "";
  }
}
