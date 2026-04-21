package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.AttachmentVO;
import com.board.vo.BoardVO;
import com.board.vo.ReplyVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BoardDetailViewCommand implements Command{

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long boardId = Long.parseLong(req.getParameter("id"));
        //System.out.println("===" + boardId);
        BoardVO board = service.getDetailBoardById(boardId);
        List<ReplyVO> replyList = service.getAllReplys(boardId);
        List<AttachmentVO> fileList = service.getAllFilesById(boardId);
        req.setAttribute("replyList", replyList);
        req.setAttribute("fileList", fileList);
        req.setAttribute("board", board);
        req.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(req, resp);
        return "";
    }
}
