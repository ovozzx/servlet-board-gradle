package com.board.service;

import com.board.dao.ReplyDao;
import com.board.vo.ReplyVO;

public class ReplyService {
	
	private ReplyDao dao = new ReplyDao();

	public int registerNewReply(ReplyVO reply) {
		
		return dao.insertNewReply(reply);
	}

}
