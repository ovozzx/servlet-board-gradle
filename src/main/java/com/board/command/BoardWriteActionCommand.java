package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.BoardVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;

public class BoardWriteActionCommand implements Command{

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // TODO : 비밀번호 확인, 입력값 유효성 검증
        BoardVO board = new BoardVO();
        // vo 멤버변수들을 파라미터별로 세팅
        // System.out.println("===" + req.getParameter("categoryId"));
        long categoryId = Long.parseLong(req.getParameter("categoryId"));
        String createUser = req.getParameter("createUser");
        String password = req.getParameter("password");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        board.setCategoryId(categoryId);
        board.setCreateUser(createUser);
        board.setUserPassword(password);
        board.setTitle(title);
        board.setContent(content);

        // 첨부파일 (링크)
        List<Part> filePartList = new ArrayList<>();
        filePartList.add(req.getPart("attachment-one"));
        filePartList.add(req.getPart("attachment-two"));
        filePartList.add(req.getPart("attachment-three"));

        service.registerNewBoard(board, filePartList);
        resp.sendRedirect(req.getContextPath() + "/board/list");
        return "";
    }
}
