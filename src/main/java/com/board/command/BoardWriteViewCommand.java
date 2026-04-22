package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.CategoryVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BoardWriteViewCommand implements Command{

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<CategoryVO> categoryList = service.getAllCategories();
        req.setAttribute("categoryList", categoryList);
        req.getRequestDispatcher("/WEB-INF/jsp/board/write.jsp").forward(req, resp);
        return "";
    }
}
