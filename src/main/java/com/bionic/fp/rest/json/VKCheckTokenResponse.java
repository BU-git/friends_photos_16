package com.bionic.fp.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing JSON returned by VK on user token check request via URL
 * https://api.vk.com/method/secure.checkToken?v=5.40&token=<user_token>&client_secret=<app_secret>&access_token=<app_access_token>
 * Created by Schotkin Alexandr on 19.11.2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VKCheckTokenResponse {

    @JsonProperty("response")
    private Response response;

    @JsonProperty("error")
    private Error error;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @JsonProperty("success")
        private long success;

        @JsonProperty("user_id")
        private long userId;

        @JsonProperty("date")
        private long date;

        @JsonProperty("expire")
        private long expire;

        public long getSuccess() {
            return success;
        }

        public void setSuccess(long success) {
            this.success = success;
        }

        public String getUserId() {
            return userId + "";
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {

        @JsonProperty("error_code")
        private long errorCode;

        @JsonProperty("error_msg")
        private String errorMsg;

        public long getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(long errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
