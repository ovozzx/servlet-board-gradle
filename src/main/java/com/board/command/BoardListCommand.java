package com.board.command;

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.BoardVO;
import com.board.vo.CategoryVO;
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
        // TODO : action null, 메서드 주석, 리팩터링 (공통 묶기), 페이지네이션
        // 검색 조건 추가

        // 파라미터
        String startDate =  req.getParameter("startDate");
        String endDate =  req.getParameter("endDate");
        String keyword = req.getParameter("keyword");
        String categoryIdReq = req.getParameter("categoryId");

        // 검색 기간 최근 한달 디폴트 설정
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minusMonths(1);

        if(startDate == null || startDate.isBlank()){
            startDate = oneMonthAgo.toString();
        }

        if(endDate == null || endDate.isBlank()){
            endDate = today.toString();
        }

        // NPE 방지
        if(keyword == null) keyword = "";
        if(categoryIdReq == null || categoryIdReq.isBlank()) categoryIdReq = "0";
        long categoryId = Long.parseLong(categoryIdReq);

        SearchVO searchVO = new SearchVO();

        // 값 안고르면 빈 문자열 옴
        // Timestamp는 yyyy-MM-dd HH:mm:ss[.fffffffff] 형태로 시간까지 필요!

        Timestamp startDateConverted = Timestamp.valueOf(startDate + " 00:00:00");
        searchVO.setStartDate(startDateConverted);

        Timestamp endDateConverted = Timestamp.valueOf(endDate + " 23:59:59");
        searchVO.setEndDate(endDateConverted);

        if(categoryId != 0){
            searchVO.setCategoryId(categoryId);
        }
        if(keyword != null && !keyword.isBlank()){
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
        List<CategoryVO> categoryList = service.getAllCategories();

        // 전체 수 / 사이즈 ==> 올림 ==> 이 개수대로 a 태그 생성
        // int allBoardCnt = boardList.size();
        int allBoardCnt = dao.selectAllBoardCnt(searchVO);
        int pageCnt = (int) Math.ceil((double) allBoardCnt / 10);

        req.setAttribute("categoryList", categoryList);
        req.setAttribute("boardList", boardList);
        req.setAttribute("allBoardCnt", allBoardCnt);
        req.setAttribute("pageCnt", pageCnt);
        req.setAttribute("now", new Date());
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);
        req.setAttribute("keyword", keyword);
        req.setAttribute("categoryId", categoryId);


        req.getRequestDispatcher("/WEB-INF/jsp/board/list.jsp").forward(req, resp);
        return "";
    }
}
