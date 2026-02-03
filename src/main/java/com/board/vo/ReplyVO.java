package com.board.vo;

import java.sql.Timestamp;

public class ReplyVO {
	
	private Long replyId;        // REPLY_ID
    private Long boardId;        // BOARD_ID
    private Long parentReplyId;  // PARENT_REPLY_ID (null 가능)
    private String createUser;   // CREATE_USER
    private String content;      // CONTENT
    private Timestamp createDate; // CREATE_DATE
    private Timestamp modifyDate; // MODIFY_DATE
    private String useYn;        // USE_YN
    
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public Long getBoardId() {
		return boardId;
	}
	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}
	public Long getParentReplyId() {
		return parentReplyId;
	}
	public void setParentReplyId(Long parentReplyId) {
		this.parentReplyId = parentReplyId;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

    
}
