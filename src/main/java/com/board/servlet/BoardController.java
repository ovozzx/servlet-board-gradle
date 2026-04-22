package com.board.servlet;


import com.board.command.*;

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
            command.execute(req, resp); // TODO:  어떤 예외?
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
