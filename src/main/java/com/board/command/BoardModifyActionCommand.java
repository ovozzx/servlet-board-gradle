package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.BoardVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardModifyActionCommand implements Command{
    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        BoardVO board = new BoardVO();
        // TODO : 비밀번호 검증, 입력값 유효성, 첨부파일
        long boardId = Long.parseLong(req.getParameter("boardId"));
        String createUser = req.getParameter("createUser");
        String password = req.getParameter("password");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        board.setCreateUser(createUser);
        board.setUserPassword(password);
        board.setTitle(title);
        board.setContent(content);
        board.setBoardId(boardId);

        service.modifyBoard(board);

        resp.sendRedirect(req.getContextPath() + "/board/view?id=" + board.getBoardId());
        return "";
    }
}
