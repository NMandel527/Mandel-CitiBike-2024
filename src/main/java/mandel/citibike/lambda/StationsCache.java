package mandel.citibike.lambda;

import com.google.gson.Gson;
import mandel.citibike.*;
import mandel.citibike.json.*;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.*;
import java.time.*;

public class StationsCache {
    private static final String BUCKET = "mandel.citibike";
    private static final String KEY = "info.json";
    private final S3Client s3Client;
    private final CitiBikeService citiBikeService;
    private Stations stations;
    private Instant lastModified;

    public StationsCache() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.citiBikeService = new CitiBikeServiceFactory().getService();
    }

    public Stations getStations() {
        boolean lessThanOneHour = s3LastModified();
        if (stations != null && lastModified != null && lessThanOneHour) {
            return stations;
        } else if ((!lessThanOneHour || lastModified == null)) {
            downloadAndWrite();
        } else {
            readFromS3();
        }
        return stations;
    }

    private boolean s3LastModified() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
            return Duration.between(lastModified, Instant.now()).toHours() <= 1;
        } catch (Exception e) {
            return true;
        }
    }

    private void readFromS3() {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        InputStream in = s3Client.getObject(getObjectRequest);
        Gson gson = new Gson();
        stations = gson.fromJson(new InputStreamReader(in), Stations.class);
    }

    private void downloadAndWrite() {
        stations = citiBikeService.getStationInformation().blockingGet();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        Gson gson = new Gson();
        s3Client.putObject(putObjectRequest, RequestBody.fromString(gson.toJson(stations)));
        lastModified = Instant.now();
    }
}