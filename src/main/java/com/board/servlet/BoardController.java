package com.board.servlet;


import com.board.command.*;
import com.board.exception.BusinessException;
import com.board.exception.NotFoundException;
import com.board.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/board/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 20     // 20MB
)
public class BoardController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    // TODO : 오버라이드 함수 공부
    private Map<String, Command> commandMap = new HashMap<>();

    // 커맨드 맵
    @Override
    public void init() {
        commandMap.put("GET:/list", new BoardListCommand());
        commandMap.put("GET:/view", new BoardDetailViewCommand());
        commandMap.put("GET:/write", new BoardWriteViewCommand());
        commandMap.put("GET:/modify", new BoardModifyViewCommand());
        commandMap.put("GET:/download", new BoardDownloadCommand());

        commandMap.put("POST:/write", new BoardWriteActionCommand());
        commandMap.put("POST:/modify", new BoardModifyActionCommand());
        commandMap.put("POST:/delete", new BoardDeleteActionCommand());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getPathInfo(); // 매핑 후 남는 부분
        String method = req.getMethod();

        Command command = commandMap.get(method + ":" + action); // 부모로 관리
        try {
            command.execute(req, resp);
        } catch (ValidationException e) {
            log.error("ValidationException", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            forwardAlert(req, resp, e.getMessage(), "");
        } catch (NotFoundException e) {
            log.error("NotFoundException", e);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            forwardAlert(req, resp, e.getMessage(), "/board/list");
        } catch (BusinessException e) {
            log.error("BusinessException", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            forwardAlert(req, resp, e.getMessage(), "/board/list");
        } catch (Exception e) {
            log.error("Exception", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            forwardAlert(req, resp, e.getMessage(), "/board/list");
            //throw new RuntimeException(e); // 500
        }
    }

    private void forwardAlert(HttpServletRequest req, HttpServletResponse res, String message, String redirecUrl) throws ServletException, IOException { // 공통 처리
        req.setAttribute("alertMsg", message);
        req.setAttribute("redirectUrl", redirecUrl);
        req.getRequestDispatcher("/WEB-INF/jsp/board/alert.jsp").forward(req, res);
    }
}
