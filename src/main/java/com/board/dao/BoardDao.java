package com.board.dao;


import com.board.connection.DBUtil;
import com.board.vo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {

	public List<BoardVO> selectAllBoards(SearchVO searchVO) {
        List<BoardVO> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder();

		sql.append( "select c.category_name " +
				", b.board_id " +
				", b.title " +
				", b.content " +
				", b.create_user " +
				", b.view_count " +
				", b.create_date " +
				", b.modify_date " +
				"from t_board b " +
				"inner join t_category c " +
				"on b.category_id = c.category_id " +
				"where b.is_usable = true " );

		// sql  수정
		// 시작일
		if(searchVO.getStartDate() != null){
			sql.append("and b.create_date >= ? ");
		}
		// 종료일
		if(searchVO.getEndDate() != null){
			sql.append("and b.create_date <= ? ");
		}
		// 카테고리
		if(searchVO.getCategoryId() != 0){
			sql.append("and b.category_id = ? ");
		}
		// 키워드 => 제목만?
		if(searchVO.getKeyword() != null){
			sql.append("and b.title like ? "); // '%?%'로 하면 문자열 전체로 봐서 동작 안 함
		}

		sql.append("order by b.create_date desc ");

		// 페이지
		if(searchVO.getPage() != 0){
			sql.append("limit 10 offset ? ");
		}



        try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql.toString());
			 ) {

			// 바인딩 처리
			int cnt = 1;

			// 시작일
			if(searchVO.getStartDate() != null){
				ps.setTimestamp(cnt++, searchVO.getStartDate());
			}
			// 종료일
			if(searchVO.getEndDate() != null){
				ps.setTimestamp(cnt++, searchVO.getEndDate());
			}
			// 카테고리
			if(searchVO.getCategoryId() != 0){
				ps.setLong(cnt++, searchVO.getCategoryId());
			}
			// 키워드
			if(searchVO.getKeyword() != null){
				ps.setString(cnt++, "%" + searchVO.getKeyword() + "%");
			}
			// 페이지
			if(searchVO.getPage() != 0){
				ps.setInt(cnt++, (searchVO.getPage() - 1) * 10); // * 사이즈
			}

			try(ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					BoardVO board = new BoardVO();
					board.setBoardId(rs.getLong("BOARD_ID"));
					board.setCategoryName(rs.getString("CATEGORY_NAME"));
					board.setTitle(rs.getString("TITLE"));
					board.setContent(rs.getString("CONTENT"));
					board.setCreateUser(rs.getString("CREATE_USER"));
					board.setViewCount(rs.getInt("VIEW_COUNT"));
					board.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					board.setModifyDate(rs.getTimestamp("MODIFY_DATE"));
					list.add(board);
				}
			}


        } catch (Exception e) {
            e.printStackTrace(); // logger로 대체 (절대 사용 말기)
        }

        return list; // 위치 보완 throw
    }

	public int selectAllBoardCnt(SearchVO searchVO){

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from t_board where is_usable = true ");

		// 시작일
		if(searchVO.getStartDate() != null){
			sql.append("and create_date >= ? ");
		}
		// 종료일
		if(searchVO.getEndDate() != null){
			sql.append("and create_date <= ? ");
		}
		// 카테고리
		if(searchVO.getCategoryId() != 0){
			sql.append("and category_id = ? ");
		}
		// 키워드 => 제목만?
		if(searchVO.getKeyword() != null){
			sql.append("and title like ? "); // '%?%'로 하면 문자열 전체로 봐서 동작 안 함
		}

		try(Connection conn = DBUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString())){

			// 바인딩 처리
			int cnt = 1;

			// 시작일
			if(searchVO.getStartDate() != null){
				ps.setTimestamp(cnt++, searchVO.getStartDate());
			}
			// 종료일
			if(searchVO.getEndDate() != null){
				ps.setTimestamp(cnt++, searchVO.getEndDate());
			}
			// 카테고리
			if(searchVO.getCategoryId() != 0){
				ps.setLong(cnt++, searchVO.getCategoryId());
			}
			// 키워드
			if(searchVO.getKeyword() != null){
				ps.setString(cnt++, "%" + searchVO.getKeyword() + "%");
			}

			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()){// 결과 1행
					return rs.getInt(1); // 첫번째 컬럼
				}
			}


		}catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public BoardVO selectDetailBoardById(Long boardId) {
		BoardVO board = new BoardVO();
	    String sql = 
		        "select c.category_name " +
				", b.board_id " +
		        ", b.title " +
				", b.content " + 
		        ", b.create_user " +
		        ", b.view_count " +
		        ", b.create_date " +
		        ", b.modify_date " +
		        "from t_board b " +
		        "inner join t_category c " +
		        "on b.category_id = c.category_id " +
		        "where b.board_id = ?";


		        try (Connection conn = DBUtil.getConnection();
		        	 PreparedStatement ps = conn.prepareStatement(sql)) {

		            ps.setLong(1, boardId); // ?에 값 바인딩
		            ResultSet rs = ps.executeQuery();
		            
		        	while (rs.next()) { // if로 수정
		                board.setBoardId(rs.getLong("BOARD_ID"));
		                board.setCategoryName(rs.getString("CATEGORY_NAME"));
		                board.setTitle(rs.getString("TITLE"));
		                board.setContent(rs.getString("CONTENT"));
		                board.setCreateUser(rs.getString("CREATE_USER"));
		                board.setViewCount(rs.getInt("VIEW_COUNT"));
		                board.setCreateDate(rs.getTimestamp("CREATE_DATE"));
		                board.setModifyDate(rs.getTimestamp("MODIFY_DATE"));
		        	}

		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		
		return board;
		
	}

	public List<ReplyVO> selectAllReplys(Long boardId) {
	     List<ReplyVO> list = new ArrayList<>();
	        String sql = 
	        		"select create_user " +
	        	    ", create_date  " +
	        	    ", content " +
	        	    "from t_reply " + 
	        	    "where board_id = ? " +
					"order by create_date";


	        try (Connection conn = DBUtil.getConnection();
	            PreparedStatement ps = conn.prepareStatement(sql)){
	        	ps.setLong(1, boardId);
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	ReplyVO reply = new ReplyVO();
	            	reply.setContent(rs.getString("CONTENT"));      
	                reply.setCreateDate(rs.getTimestamp("CREATE_DATE"));
	                list.add(reply);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return list;
	}

	public List<AttachmentVO>  selectAllFiles(Long boardId){
		List<AttachmentVO> list = new ArrayList<>();
		String sql =
		    "SELECT attachment_id, board_id, original_name, save_name, file_path, file_ext, file_size, create_date, modify_date, is_usable " +
		    "FROM t_attachment " +
			"WHERE board_id = ?"; // * 사용이 편함

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setLong(1, boardId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AttachmentVO file = new AttachmentVO();
				file.setAttachmentId(rs.getLong("attachment_id"));
				file.setOriginalName(rs.getString("original_name"));
				file.setSaveName(rs.getString("save_name"));
				file.setFilePath(rs.getString("file_path"));
				list.add(file);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long insertNewBoard(BoardVO board) {
		String sql =
				"INSERT INTO t_board " +
				"(category_id, title, content, create_user, user_password, view_count, create_date, modify_date, is_usable) " +
				"VALUES(?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP, NULL, TRUE)";
				// "RETURNING board_id "; // postgres 문법

		// TODO : 문단 나누기, sql injection (공부)
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			// try-with-resources : 예외가 나도, 리소스인 Connection와 PreparedStatement를 안전하게 자동으로 닫기 위해서
			//System.out.println("=======" + board.getCategoryId());
			ps.setLong(1, board.getCategoryId());
			ps.setObject(2, board.getTitle());
			ps.setString(3, board.getContent());
			ps.setString(4, board.getCreateUser());
			ps.setString(5, board.getUserPassword());
			// executeUpdate : INSERT / UPDATE / DELETE
			//return ps.executeUpdate(); // INSERT 성공 시 1 반환
			// TODO : ResultSet 리소스 확인
			ps.executeUpdate();
			// ResultSet rs = ps.executeUpdate();
			// ResultSet rs = ps.executeQuery(); // INSERT ... RETURNING 쓸 때는
			long boardId = 0;
			try(ResultSet rs = ps.getGeneratedKeys()){
				if(rs.next()) {
					boardId = rs.getLong(1);
					// boardId = rs.getLong("board_id");
				}
			}
			//return ps.executeUpdate(); // INSERT 성공 시 1 반환
			return boardId;

		} catch (Exception e) {
			e.printStackTrace();
		}


		return 0; // 실패
	}

	public int insertNewAttachment(AttachmentVO attachmentVO) {
		String sql =
				"INSERT INTO t_attachment " +
						"(board_id, original_name, save_name, file_path, file_ext, file_size, create_date, modify_date, is_usable) " +
						"VALUES(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, NULL, true) ";


		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, attachmentVO.getBoardId());
			ps.setString(2, attachmentVO.getOriginalName());
			ps.setString(3, attachmentVO.getSaveName());
			ps.setString(4, attachmentVO.getFilePath());
			ps.setString(5, attachmentVO.getFileExt());
			ps.setLong(6, attachmentVO.getFileSize());

			return ps.executeUpdate(); // INSERT 성공 시 1 반환

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0; // 실패
	}

	public int updateBoard(BoardVO board){
		String sql = "UPDATE t_board " +
				"SET create_user = ? " +
				", user_password = ? " +
				", title = ? " +
				", content = ? " +
				", modify_date=CURRENT_TIMESTAMP " +
				"WHERE board_id = ? ";

		try(Connection conn = DBUtil.getConnection();
		    PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, board.getCreateUser());
			ps.setString(2, board.getUserPassword());
			ps.setString(3, board.getTitle());
			ps.setString(4, board.getContent());
			ps.setLong(5, board.getBoardId());
			return ps.executeUpdate(); // DB에서 변경된(영향받은) 행(row) 개수를 반환
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public String selectPasswordById(RequestVerifyVO requestVerifyVO){

		String sql =
				"select user_password " +
				"from t_board " +
				"where board_id = ?";

		String user_password = null;

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql); ){

			ps.setLong(1, requestVerifyVO.getBoardId());

			try (ResultSet rs = ps.executeQuery()) {
			// JDBC 리소스 3대장 : Connection / PreparedStatement / ResultSet
				if (rs.next()) {
					user_password = rs.getString("user_password");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user_password;
	}

	public int deleteBoardById(RequestVerifyVO requestVerifyVO){

		String sql = "UPDATE t_board " +
				"SET is_usable = false " +
				", modify_date=CURRENT_TIMESTAMP " +
				"WHERE board_id = ? ";

		try(Connection conn = DBUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setLong(1, requestVerifyVO.getBoardId());
			return ps.executeUpdate(); // DB에서 변경된(영향받은) 행(row) 개수를 반환
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public int updateViewCnt(Long boardId){
		String sql = "UPDATE t_board " +
				"SET view_count = view_count + 1 " +
				"WHERE board_id = ? ";

		try(Connection conn = DBUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);){

			ps.setLong(1, boardId);
			return ps.executeUpdate(); // DB에서 변경된(영향받은) 행(row) 개수를 반환
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

//	public List<BoardVO> selectBoardsByCondition(SearchVO searchVO) {
//		List<BoardVO> list = new ArrayList<>();
//		String searchCondition = null;
//		// TODO
//		// 등록일 - 시작일만 있는 경우
//		// 등록일 - 종료일만 있는 경우
//		// 등록일 - 시작일 & 종료일 둘다 있는 경우
//		// 카테고리 있는 경우
////		if(searchVO.getCategoryId() == 0){
////			searchCondition += "";
////		} else{
////			searchCondition += "and b.category_id = ? ";
////		}
////		// 키워드 있는 경우
////		if(searchVO.getKeyword() == null){
////			searchCondition += "";
////		} else{
////			searchCondition += "and (b.title like ? or b.create_user like ? or b.content like ?)  ";
////		}
//
//		System.out.println("===" + searchVO);
//		String sql =
//				"select c.category_name " +
//						", b.board_id " +
//						", b.title " +
//						", b.content " +
//						", b.create_user " +
//						", b.view_count " +
//						", b.create_date " +
//						", b.modify_date " +
//						"from t_board b " +
//						"inner join t_category c " +
//						"on b.category_id = c.category_id " +
//						"where b.is_usable = true " +
//						"and b.create_date between ? and ? "
//				         ;
//
//
//		try (Connection conn = DBUtil.getConnection();
//			 PreparedStatement ps = conn.prepareStatement(sql);) {
//
//			String keyword = "%" + searchVO.getKeyword() + "%";
//
//			ps.setString(1, searchVO.getStartDate());
//			ps.setString(2, searchVO.getEndDate());
////			ps.setLong(3, searchVO.getCategoryId());
////			ps.setString(4, keyword);
////			ps.setString(5, keyword);
////			ps.setString(6, keyword);
//
//			try(ResultSet rs = ps.executeQuery()){
//				while (rs.next()) {
//					BoardVO board = new BoardVO();
//					board.setBoardId(rs.getLong("BOARD_ID"));
//					board.setCategoryName(rs.getString("CATEGORY_NAME"));
//					board.setTitle(rs.getString("TITLE"));
//					board.setContent(rs.getString("CONTENT"));
//					board.setCreateUser(rs.getString("CREATE_USER"));
//					board.setViewCount(rs.getInt("VIEW_COUNT"));
//					board.setCreateDate(rs.getTimestamp("CREATE_DATE"));
//					board.setModifyDate(rs.getTimestamp("MODIFY_DATE"));
//					list.add(board);
//				}
//
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return list;
//	}

	public AttachmentVO selectAttachmentById(long fileId) {

		String sql =
				"SELECT attachment_id, board_id, original_name, save_name, file_path, file_ext, file_size, create_date, modify_date, is_usable " +
						"FROM t_attachment " +
						"WHERE attachment_id = ?";

		AttachmentVO attachment = null;

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, fileId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					attachment = new AttachmentVO();
					attachment.setAttachmentId(rs.getLong("attachment_id"));
					attachment.setBoardId(rs.getLong("board_id"));
					attachment.setOriginalName(rs.getString("original_name"));
					attachment.setSaveName(rs.getString("save_name"));
					attachment.setFilePath(rs.getString("file_path"));
					attachment.setFileExt(rs.getString("file_ext"));
					attachment.setFileSize(rs.getLong("file_size"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return attachment;
	}


}
