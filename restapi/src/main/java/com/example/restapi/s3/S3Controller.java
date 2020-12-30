package com.example.restapi.s3;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.QualityFilter;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.UnindexedFace;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping(value = "/aws")
public class S3Controller {
	
	private AmazonRekognition rekognitionClient;
	
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;
    
    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
    
    public String createCollection() throws Exception {
    	
    	AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    	
    	String collectionId = "NewTech";
        System.out.println("Creating collection: " +
     collectionId );
        
	    CreateCollectionRequest request = new CreateCollectionRequest()
	                .withCollectionId(collectionId);
	       
	  CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request); 
	  System.out.println("CollectionArn : " +
	     createCollectionResult.getCollectionArn());
	  System.out.println("Status code : " +
	     createCollectionResult.getStatusCode().toString());
	  
	  return collectionId;
    }

    public void uploadS3(MultipartFile file, String file_name, AWSCredentials credentials) throws Exception {
        AmazonS3 s3Client;
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
        s3Client.putObject(new PutObjectRequest(this.bucket, file_name, file.getInputStream(), metadata));
        System.out.println("uploaded");
    }

    @PostMapping("/upload")
    public void imageUpload(@RequestParam(value="user_id") String user_id, @RequestParam(value="file", required=true) MultipartFile file) throws Exception {
    	AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        uploadS3(file, user_id, credentials);

        rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();

        Image image = new Image()
                .withS3Object(new S3Object()
                .withBucket(bucket)
                .withName(user_id));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withQualityFilter(QualityFilter.AUTO)
                .withMaxFaces(1)
                //.withCollectionId(createCollection())
                .withCollectionId("NewTech")
                .withExternalImageId(user_id)
                .withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);
        
        System.out.println("Results for " + file.getName());
        System.out.println("Faces indexed:");
        
        List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for (FaceRecord faceRecord : faceRecords) {
            System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
            System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
        }
        
        List<UnindexedFace> unindexedFaces = indexFacesResult.getUnindexedFaces();
        System.out.println("Faces not indexed:");
        for (UnindexedFace unindexedFace : unindexedFaces) {
            System.out.println("  Location:" + unindexedFace.getFaceDetail().getBoundingBox().toString());
            System.out.println("  Reasons:");
            for (String reason : unindexedFace.getReasons()) {
                System.out.println("   " + reason);
            }
        }
    }
}
