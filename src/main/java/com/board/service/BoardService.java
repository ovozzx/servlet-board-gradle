package com.board.service;

import com.board.dao.BoardDao;
import com.board.vo.*;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class BoardService {

	private BoardDao dao = new BoardDao();

    public List<CategoryVO> getAllCategories() {
        return dao.selectAllCategories();
    }
    // 전체 게시글 조회
    // TODO : 모두 주석 (의도만)
    public List<BoardVO> getAllBoards(SearchVO searchVO) {
        return dao.selectAllBoards(searchVO);
    }
    
    // 상세 게시글 조회 
    public BoardVO getDetailBoardById(Long boardId) {
        // 조회수 증가
        dao.updateViewCnt(boardId);
        return dao.selectDetailBoardById(boardId);
    }

	public List<ReplyVO> getAllReplys(Long boardId) {
		return dao.selectAllReplys(boardId);
	}

    // 파일 조회
    public List<AttachmentVO> getAllFilesById(Long boardId) {
        return dao.selectAllFiles(boardId);
    }

	public int registerNewBoard(BoardVO board, List<Part> filePartList) throws IOException {

        long boardId = dao.insertNewBoard(board);
        int attachmentCnt = 0;
        for(Part filePart: filePartList){
            // 파일 선택 안했을 때
            if (filePart.getSubmittedFileName() == null) continue;
            if (filePart.getSize() == 0) continue;

            AttachmentVO attachmentVO = new AttachmentVO();
            // 파일명 추출
            String fileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();
            // 실제 저장 경로
            String uploadPath = "/Users/leesoyeong/upload";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // 파일 저장
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // 첨부파일 insert
            attachmentVO.setBoardId(boardId);
            attachmentVO.setOriginalName(fileName);
            attachmentVO.setSaveName(fileName);
            attachmentVO.setFilePath(uploadPath);
            attachmentVO.setFileExt("");
            attachmentVO.setFileSize(filePart.getSize());

            attachmentCnt += dao.insertNewAttachment(attachmentVO);
        }

        if(boardId > 0){
            return attachmentCnt;
        }else{
            return 0;
        }

	}

    public int modifyBoard(BoardVO board) {
        return dao.updateBoard(board);
    }

    // 삭제 - 비밀번호 검증 후 삭제
    public boolean verifyAndDeleteById(RequestVerifyVO requestVerifyVO){
        String password = dao.selectPasswordById(requestVerifyVO);
        if(password.equals(requestVerifyVO.getPasswordInput())){
            // 삭제
            int successCnt = dao.deleteBoardById(requestVerifyVO);
            return successCnt > 0;
        }
        return false; // 미일치
    }

    // 검색 조회
//    public List<BoardVO> getBoardsByCondition(SearchVO searchVO) {
//        return dao.selectBoardsByCondition(searchVO);
//    }

}
