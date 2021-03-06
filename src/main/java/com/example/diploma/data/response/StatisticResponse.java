package com.example.diploma.data.response;

import lombok.Data;

@Data
public class StatisticResponse {
    private long postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    private long firstPublication;
}
