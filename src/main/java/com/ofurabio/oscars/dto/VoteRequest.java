package com.ofurabio.oscars.dto;

public class VoteRequest {
    private Long categoryId;
    private Long nomineeId;

    public VoteRequest(Long categoryId, Long nomineeId) {
        this.categoryId = categoryId;
        this.nomineeId = nomineeId;
    }

    public VoteRequest() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(Long nomineeId) {
        this.nomineeId = nomineeId;
    }
}
