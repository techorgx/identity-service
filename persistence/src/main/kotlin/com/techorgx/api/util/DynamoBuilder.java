package com.techorgx.api.util;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DynamoBuilder {
    private final String region;
    private final String endpoint;
    private final String secretKey;
    private final String accessKey;

    public DynamoBuilder(
            @Value("${aws.dynamodb.region}") String region,
            @Value("${aws.dynamodb.endpoint:#{null}}") String endpoint,
            @Value("${aws.dynamodb.secretKey:#{null}}") String secretKey,
            @Value("${aws.dynamodb.accessKey:#{null}}") String accessKey
    ){
        this.region = region;
        this.endpoint = endpoint;
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    private AmazonDynamoDB dynamoDB = null;
    private DynamoDBMapper dynamoDBMapper = null;

    @PostConstruct
    public void init() {
        AmazonDynamoDBClientBuilder dynamoClientBuilder = AmazonDynamoDBClientBuilder.standard();

        AWSCredentialsProvider awsCredentials = WebIdentityTokenCredentialsProvider.create();

        if (accessKey != null && secretKey != null) {
            awsCredentials = new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                            accessKey,
                            secretKey
                    )
            );
        }

        dynamoClientBuilder.withCredentials(awsCredentials);

        if (endpoint != null) {
            dynamoClientBuilder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region));
        } else {
            dynamoClientBuilder.withRegion(region);
        }
        dynamoDB = dynamoClientBuilder.build();
        dynamoDBMapper = new DynamoDBMapper(dynamoClientBuilder.build());
    }

    public DynamoDBMapper getAWSDynamoDbClient() {
        return dynamoDBMapper;
    }

    public AmazonDynamoDB getAWSDynamoDB() {
        return dynamoDB;
    }
}
