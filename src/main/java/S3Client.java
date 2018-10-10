import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class S3Client {

    private static S3Client s3Client = null;
    private AmazonS3 s3;
    private String bucketName;

    private S3Client(){
        try {
            s3 = AmazonS3ClientBuilder.defaultClient();
        }catch (Exception e){
            throw e;
        }
        bucketName = "maryam-bucket-" + UUID.randomUUID();
    }

    public static S3Client S3Client() throws Exception {
        if (s3Client == null)
            s3Client = new S3Client();
        return s3Client;
    }

    public void uploadFile (String fileName, InputStream inputStream) throws Exception{
        String key = fileName;
        try {
            s3.putObject(new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata()));
        }catch (Exception e){
            System.out.println("Couldn't upload to s3");
            throw e;
        }
        try {
            DataBase dataBase = DataBase.dataBase();
            dataBase.insertFile(fileName, LocalDate.now(), LocalTime.now());
        }catch (SQLException e){
            System.out.println("Couldn't add record in db");
            s3.deleteObject(new DeleteObjectRequest(bucketName, key));
            throw e;
        }
    }

}
