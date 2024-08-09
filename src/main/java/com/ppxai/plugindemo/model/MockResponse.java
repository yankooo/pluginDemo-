package com.ppxai.plugindemo.model;

import java.util.List;

public class MockResponse {
        public String getRequestId() {
                return requestId;
        }

        public void setRequestId(String requestId) {
                this.requestId = requestId;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public List<Comment> getComments() {
                return comments;
        }

        public void setComments(List<Comment> comments) {
                this.comments = comments;
        }

        private String requestId;
        private int status;
        private List<Comment> comments;
}