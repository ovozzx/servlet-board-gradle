package com.board.servlet;

import com.board.vo.ReplyVO;
import com.board.service.ReplyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/reply/*")
public class ReplyServlet extends HttpServlet {
	
	private ReplyService service = new ReplyService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        
        // 등록 
        if (action.equals("/write")) {
            ReplyVO reply = new ReplyVO();
            // vo 멤버변수들을 파라미터별로 세팅
            Long boardId = Long.parseLong(req.getParameter("boardId"));
            String content = req.getParameter("content");
            reply.setBoardId(boardId);
            reply.setContent(content);
            reply.setCreateUser("");
            // 대댓글 
            
            service.registerNewReply(reply);
            resp.sendRedirect(req.getContextPath() + "/board/view?id=" + boardId);
        } 
       
    }

}
