import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
        s3 = AmazonS3ClientBuilder.defaultClient();
        bucketName = "maryam-bucket-" + UUID.randomUUID();
    }

    public static S3Client S3Client(){
        if (s3Client == null)
            s3Client = new S3Client();
        return s3Client;
    }

    public void uploadFile (String fileName, InputStream inputStream) throws SQLException {
        String key = fileName;
        s3.putObject(new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata()));
        DataBase dataBase = DataBase.dataBase();
        dataBase.insertFile(fileName, LocalDate.now(), LocalTime.now());

    }

}
