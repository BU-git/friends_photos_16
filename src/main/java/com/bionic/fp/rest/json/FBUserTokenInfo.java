package com.bionic.fp.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing JSON returned by FB Graph API on call to
 * "https://graph.facebook.com/debug_token?input_token=<user_token>&access_token=<app_access_token>"
 * Created by Alexandr on 16.11.2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FBUserTokenInfo {

    @JsonProperty("data")
    private TokenData data;

    public TokenData getData() {
        return data;
    }

    public void setData(TokenData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FBUserTokenInfo{" +
                "data=" + data +
                '}';
    }

    /**
     * Class representing JSON data section returned by FB Graph API on call to
     * "https://graph.facebook.com/debug_token?input_token=<user_token>&access_token=<app_access_token>"
     * Created by Schotkin Alexandr on 16.11.2015.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TokenData {

        @JsonProperty("app_id")
        private String appId;

        @JsonProperty("application")
        private String application;

        @JsonProperty("error")
        private FBError error;

        @JsonProperty("expires_at")
        private long expiresAt;

        @JsonProperty("is_valid")
        private boolean isValid;

        @JsonProperty("scopes")
        private List<String> scopes = new ArrayList<String>();

        @JsonProperty("user_id")
        private String userId;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public FBError getError() {
            return error;
        }

        public void setError(FBError error) {
            this.error = error;
        }

        public long getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(long expiresAt) {
            this.expiresAt = expiresAt;
        }

        public boolean isValid() {
            return isValid;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }

        public List<String> getScopes() {
            return scopes;
        }

        public void setScopes(List<String> scopes) {
            this.scopes = scopes;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "TokenData{" +
                    "appId='" + appId + '\'' +
                    ", application='" + application + '\'' +
                    ", error=" + error +
                    ", expiresAt=" + expiresAt +
                    ", isValid=" + isValid +
                    ", scopes=" + scopes +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }
}
