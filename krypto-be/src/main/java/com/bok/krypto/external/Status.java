package com.bok.krypto.external;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "error_code",
        "error_message",
        "elapsed",
        "credit_count",
        "notice",
        "total_count"
})
public class Status {

    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("error_code")
    public Integer errorCode;
    @JsonProperty("error_message")
    public Object errorMessage;
    @JsonProperty("elapsed")
    public Integer elapsed;
    @JsonProperty("credit_count")
    public Integer creditCount;
    @JsonProperty("notice")
    public Object notice;
    @JsonProperty("total_count")
    public Integer totalCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Status() {
    }

    /**
     * @param elapsed
     * @param errorMessage
     * @param errorCode
     * @param creditCount
     * @param totalCount
     * @param timestamp
     * @param notice
     */
    public Status(String timestamp, Integer errorCode, Object errorMessage, Integer elapsed, Integer creditCount, Object notice, Integer totalCount) {
        super();
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.elapsed = elapsed;
        this.creditCount = creditCount;
        this.notice = notice;
        this.totalCount = totalCount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("timestamp", timestamp).append("errorCode", errorCode).append("errorMessage", errorMessage).append("elapsed", elapsed).append("creditCount", creditCount).append("notice", notice).append("totalCount", totalCount).append("additionalProperties", additionalProperties).toString();
    }

}
