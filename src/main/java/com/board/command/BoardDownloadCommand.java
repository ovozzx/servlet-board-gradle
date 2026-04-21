package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.AttachmentVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class BoardDownloadCommand implements Command{
    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        long fileId = Long.parseLong(req.getParameter("fileId"));

        // 1. 파일 정보 조회
        AttachmentVO file = dao.selectAttachmentById(fileId);
        if (file == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        // 2. 실제 파일 경로
        String fullPath = file.getFilePath()
                + File.separator + file.getSaveName();

        File target = new File(fullPath);
        if (!target.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        // 3. 응답 헤더
        resp.setContentType("application/octet-stream");
        resp.setContentLengthLong(target.length());

        String encodedName = URLEncoder.encode(
                file.getOriginalName(), "UTF-8").replace("+", "%20");

        resp.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + encodedName + "\""
        );

        // 4. 파일 스트림
        try (InputStream in = new FileInputStream(target);
             OutputStream out = resp.getOutputStream()) {

            in.transferTo(out);
        }
        return "";
    }
}
