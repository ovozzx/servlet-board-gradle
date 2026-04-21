package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.BoardVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardModifyViewCommand implements Command{
    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long boardId = Long.parseLong(req.getParameter("id"));
        //System.out.println("===" + boardId);
        BoardVO board = service.getDetailBoardById(boardId);
        req.setAttribute("board", board);
        req.getRequestDispatcher("/WEB-INF/jsp/board/modify.jsp").forward(req, resp);
        return "";
    }
}
