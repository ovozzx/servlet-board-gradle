package com.board.util;

import com.board.config.AppConfig;

public class Pagination {
    static int pageSize      = AppConfig.getInt("page.size", 10);
    static int pageGroupSize = AppConfig.getInt("page.group.size", 10);

    public static int create(int totalCount){
        int paginationCount = (int) Math.ceil((double) totalCount / pageSize);
        return paginationCount;
    }
}
