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
        boolean isDeleted = service.verifyAndDeleteById(requestVerifyVO);
        System.out.println("===" + isDeleted);
        if(isDeleted){
            // 삭제 완료 alert
            req.setAttribute("alertMsg", "삭제가 완료되었습니다.");
            req.setAttribute("redirectUrl", "/board/list");
            req.getRequestDispatcher("/WEB-INF/jsp/board/alert.jsp").forward(req, resp);
            //resp.sendRedirect(req.getContextPath() + "/board/view?id=" + boardId);
            // TODO : 없는 글 지울 때, 삭제 중 오류 등  => 예외처리 (검증과 삭제를 분리),  반복되는 부분 모듈화, 단일책임 원칙 찾아보기
        }else{
            // 비밀번호 오류 alert
            req.setAttribute("alertMsg", "비밀번호가 일치하지 않습니다.");
            req.setAttribute("redirectUrl", "");
            req.getRequestDispatcher("/WEB-INF/jsp/board/alert.jsp").forward(req, resp);
            //resp.sendRedirect(req.getContextPath() + "/board/view?id=" + boardId);
        }
        return "";
    }
}
