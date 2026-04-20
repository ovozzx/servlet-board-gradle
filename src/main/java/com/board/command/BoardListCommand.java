package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.BoardVO;
import com.board.vo.SearchVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class BoardListCommand implements Command{

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // TODO : action null, 커맨드 패턴 (맵으로 관리), 메서드 주석
        // 검색 조건 추가
        SearchVO searchVO = new SearchVO();

        // 값 안고르면 빈 문자열 옴
        // Timestamp는 yyyy-MM-dd HH:mm:ss[.fffffffff] 형태로 시간까지 필요!
        if(req.getParameter("startDate") != null && !req.getParameter("startDate").isBlank()){
            Timestamp startDate = Timestamp.valueOf(req.getParameter("startDate") + " 00:00:00");
            searchVO.setStartDate(startDate);
        }
        if(req.getParameter("endDate") != null && !req.getParameter("endDate").isBlank()){
            Timestamp endDate = Timestamp.valueOf(req.getParameter("endDate") + " 23:59:59");
            searchVO.setEndDate(endDate);
        }
        if(req.getParameter("categoryId") != "0" && req.getParameter("categoryId") != null && !req.getParameter("categoryId").isBlank() ){
            long categoryId = Long.parseLong(req.getParameter("categoryId"));
            searchVO.setCategoryId(categoryId);
        }
        if(req.getParameter("keyword") != null && !req.getParameter("keyword").isBlank()){
            String keyword = req.getParameter("keyword");
            searchVO.setKeyword(keyword);
        }

        // 페이지네이션
        if(req.getParameter("page") == null || req.getParameter("page").isBlank()){
            searchVO.setPage(1);
        } else{
            int page = Integer.parseInt(req.getParameter("page"));
            searchVO.setPage(page);
        }

        List<BoardVO> boardList = service.getAllBoards(searchVO);
        // 전체 수 / 사이즈 ==> 올림 ==> 이 개수대로 a 태그 생성
        // int allBoardCnt = boardList.size();
        int allBoardCnt = dao.selectAllBoardCnt(searchVO);
        int pageCnt = (int) Math.ceil((double) allBoardCnt / 10);

        req.setAttribute("boardList", boardList);
        req.setAttribute("allBoardCnt", allBoardCnt);
        req.setAttribute("pageCnt", pageCnt);
        req.setAttribute("now", new Date());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        req.setAttribute("tomorrow", tomorrow.toString());
        req.getRequestDispatcher("/WEB-INF/jsp/board/list.jsp").forward(req, resp);
        return "";
    }
}
