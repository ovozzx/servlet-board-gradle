package com.board.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class BoardVO {
	
    private long boardId;
    private long categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String createUser;
    private String userPassword;
    private String passwordConfirm;
    private int viewCount;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private String useYn;

    
    
}
