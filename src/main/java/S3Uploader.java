import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Objects;

@WebServlet("/S3Uploader")
@MultipartConfig
public class S3Uploader extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();

        if (Objects.equals(fileName, "")){
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Dude! upload a file!</h3>");
            return;

        }

        S3Client s3Client = null;
        try {
            s3Client = S3Client.S3Client();
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Couldn't upload (problem with s3client), sorry!</h3>");
            return;
        }

        try {
            s3Client.uploadFile(fileName, fileContent);
            System.out.println("here");
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Couldn't upload sorry!</h3>");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Uploaded successfully!</h3>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
