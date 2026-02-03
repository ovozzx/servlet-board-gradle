package com.board.dao;


import com.board.connection.DBUtil;
import com.board.vo.ReplyVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReplyDao {

	public int insertNewReply(ReplyVO reply) {
		String sql =
	            "INSERT INTO t_reply " +
	            "(board_id, parent_reply_id, create_user, content, create_date, modify_date, is_usable) " +
	            "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, NULL, TRUE)";

	        try (Connection conn = DBUtil.getConnection();
				 PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setLong(1, reply.getBoardId());
	            ps.setObject(2, reply.getParentReplyId()); // null 허용
	            ps.setString(3, reply.getCreateUser());
	            ps.setString(4, reply.getContent());

	            return ps.executeUpdate(); // INSERT 성공 시 1 반환

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

        return 0; // 실패
	}

}
