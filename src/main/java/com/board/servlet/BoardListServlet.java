package com.board.servlet;
// http://localhost:8080/servlet-board/board/list

import com.board.dao.BoardDao;
import com.board.service.BoardService;
import com.board.vo.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebServlet("/board/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 20     // 20MB
)
public class BoardListServlet extends HttpServlet {

    private BoardService service = new BoardService();
    private BoardDao dao = new BoardDao();
    private static final String UPLOAD_DIR = "uploads";

    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	String action = req.getPathInfo(); // 매핑 후 남는 부분 
    	// System.out.println("===" + action);
    	// 전체 목록 조회
    	if(action.equals("/list")) {
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
    	}
    	// 상세 조회 
    	else if(action.contains("/view")) { // 서블릿에서는 pathvariable 직접 파싱해야 함
    		long boardId = Long.parseLong(req.getParameter("id"));
    		//System.out.println("===" + boardId);
    		BoardVO board = service.getDetailBoardById(boardId);
    		List<ReplyVO> replyList = service.getAllReplys(boardId);
            List<AttachmentVO> fileList = service.getAllFilesById(boardId);
    		req.setAttribute("replyList", replyList);
            req.setAttribute("fileList", fileList);
    		req.setAttribute("board", board);
    		req.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(req, resp);
    	}
    	// 등록 
    	else if(action.equals("/write")) { 
    		req.getRequestDispatcher("/WEB-INF/jsp/board/write.jsp").forward(req, resp);
    	}
    	// 수정 
    	else if(action.contains("/modify")) { 
    		long boardId = Long.parseLong(req.getParameter("id"));
    		//System.out.println("===" + boardId);
    		BoardVO board = service.getDetailBoardById(boardId);
    		req.setAttribute("board", board);
    		req.getRequestDispatcher("/WEB-INF/jsp/board/modify.jsp").forward(req, resp);
    	}
        // 파일 다운로드
        else if (action.contains("/download")) {

            long fileId = Long.parseLong(req.getParameter("fileId"));

            // 1. 파일 정보 조회
            AttachmentVO file = dao.selectAttachmentById(fileId);
            if (file == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 2. 실제 파일 경로
            String fullPath = file.getFilePath()
                    + File.separator + file.getSaveName();

            File target = new File(fullPath);
            if (!target.exists()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 3. 응답 헤더
            resp.setContentType("application/octet-stream");
            resp.setContentLengthLong(target.length());

            String encodedName = URLEncoder.encode(
                    file.getOriginalName(), "UTF-8").replace("+", "%20");

            resp.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + encodedName + "\""
            );

            // 4. 파일 스트림
            try (InputStream in = new FileInputStream(target);
                 OutputStream out = resp.getOutputStream()) {

                in.transferTo(out);
            }
        }




    }
    
    // doPost
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        
        // 게시글 등록 
        if (action.equals("/write")) {
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
        } 
        // 댓글 등록
        // 게시글 수정 
        else if (action.equals("/modify")) {
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
        }
        // 삭제 - 비밀번호 검증 후 삭제
        else if(action.equals("/delete/verify")){
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
        }
        // 검색
        // TODO : get으로 api 하나로 하기 (list + search)
//        else if(action.equals("/search")){
//            SearchVO searchVO = new SearchVO();
//            String startDate = req.getParameter("startDate");
//            String endDate = req.getParameter("endDate");
//            long categoryId = Long.parseLong(req.getParameter("categoryId"));
//            String keyword = req.getParameter("keyword");
//
//            searchVO.setStartDate(startDate);
//            searchVO.setEndDate(endDate);
//            searchVO.setCategoryId(categoryId);
//            searchVO.setKeyword(keyword);
//
//            List<BoardVO> boardList = service.getBoardsByCondition(searchVO);
//            req.setAttribute("boardList", boardList);
//            req.getRequestDispatcher("/WEB-INF/jsp/board/list.jsp").forward(req, resp);
//        }
        // 키워드 학습 먼저 
        // 디자인패턴 : 커맨드, 옵저버, 전략 공부
    }
}
