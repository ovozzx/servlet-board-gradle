package com.board.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 20     // 20MB
)
public class UploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        Part filePart = req.getPart("uploadFile");

        // 파일명 추출
        String fileName = Paths.get(filePart.getSubmittedFileName())
                .getFileName().toString();

        // 실제 저장 경로 (톰캣 내부)
//        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        String uploadPath = "/Users/leesoyeong/upload";


        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 파일 저장
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // 결과 전달
//        req.setAttribute("title", title);
//        req.setAttribute("fileName", fileName);
//
//        req.getRequestDispatcher("/uploadResult.jsp")
//                .forward(req, resp);
    }
}
