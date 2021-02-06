package com.vit.community.springapplication.model;

import com.vit.community.springapplication.exceptions.SpringCommunityException;

import java.util.Arrays;

//Enum to distinguish whether a vote is an upvote or a downvote.

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringCommunityException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
