package com.board.service;

import com.board.config.AppConfig;
import com.board.dao.BoardDao;
import com.board.exception.NotFoundException;
import com.board.exception.ValidationException;
import com.board.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
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
        BoardVO board = dao.selectDetailBoardById(boardId);
        if (board == null) {
            throw new NotFoundException("존재하지 않는 게시글입니다.");
        }
        dao.updateViewCnt(boardId);   // 존재 확인 후에 조회수 증가
        return board;
    }

	public List<ReplyVO> getAllReplys(Long boardId) {
		return dao.selectAllReplys(boardId);
	}

    // 파일 조회
    public List<AttachmentVO> getAllFilesById(Long boardId) {
        return dao.selectAllFiles(boardId);
    }

	public long registerNewBoard(BoardVO board, List<Part> filePartList) throws IOException {

        if(!board.getUserPassword().equals(board.getPasswordConfirm())){
            throw new ValidationException("비밀번호가 일치하지 않습니다.");
        }

        long boardId = dao.insertNewBoard(board);

        for(Part filePart: filePartList){
            // 파일 선택 안했을 때
            if (filePart.getSubmittedFileName() == null) continue;
            if (filePart.getSize() == 0) continue;

            AttachmentVO attachmentVO = new AttachmentVO();
            // 파일명 추출
            String fileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();
            // 실제 저장 경로
            String uploadPath = AppConfig.get("upload.path");
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

            dao.insertNewAttachment(attachmentVO);
        }


        return boardId;


	}

    public void modifyBoard(BoardVO board) {
        // 비밀번호 검증 후 수정
        String password = dao.selectPasswordById(board.getBoardId());
        if(password == null){
            throw new NotFoundException("존재하지 않는 게시글입니다.");
        }
        if(!password.equals(board.getUserPassword())){
            throw new ValidationException("비밀번호가 일치하지 않습니다.");
        }
        int affected = dao.updateBoard(board);
        if (affected == 0) {
            throw new NotFoundException("존재하지 않거나 삭제된 게시글입니다.");
        }
    }

    // 삭제 - 비밀번호 검증 후 삭제
    public void deleteById(RequestVerifyVO requestVerifyVO){
        String password = dao.selectPasswordById(requestVerifyVO.getBoardId());
        if(password == null){
            throw new NotFoundException("존재하지 않는 게시글입니다.");
        }
        if(!password.equals(requestVerifyVO.getPasswordInput())){
            throw new ValidationException("비밀번호가 일치하지 않습니다.");
        }
        int affected = dao.deleteBoardById(requestVerifyVO);
        if (affected == 0) {
            throw new NotFoundException("이미 삭제된 게시글입니다.");
        }
    }


}
