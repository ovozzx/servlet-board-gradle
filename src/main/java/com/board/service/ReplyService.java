package com.board.service;

import com.board.dao.ReplyDao;
import com.board.vo.ReplyVO;

public class ReplyService {
	
	private ReplyDao dao = new ReplyDao();

	public void registerNewReply(ReplyVO reply) {
		dao.insertNewReply(reply);
	}

}
