package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.RequestVerifyVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardDeleteActionCommand implements Command{

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // baord id & password
        RequestVerifyVO requestVerifyVO = new RequestVerifyVO();
        long boardId = Long.parseLong(req.getParameter("boardId"));
        String passwordInput = req.getParameter("password");

        requestVerifyVO.setBoardId(boardId);
        requestVerifyVO.setPasswordInput(passwordInput);
        // TODO : use yn 형용사, 예약어 지양 pw
        service.deleteById(requestVerifyVO);

        req.setAttribute("alertMsg", "삭제가 완료되었습니다.");
        req.setAttribute("redirectUrl", "/board/list");
        req.getRequestDispatcher("/WEB-INF/jsp/board/alert.jsp").forward(req, resp);

        return "";
    }
}
