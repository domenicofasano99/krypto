package com.bok.krypto.external;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "data"
})
public class CoinMarketDTO {

    @JsonProperty("status")
    public Status status;
    @JsonProperty("data")
    public List<Datum> data = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public CoinMarketDTO() {
    }

    /**
     * @param data
     * @param status
     */
    public CoinMarketDTO(Status status, List<Datum> data) {
        super();
        this.status = status;
        this.data = data;
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
        return new ToStringBuilder(this).append("status", status).append("data", data).append("additionalProperties", additionalProperties).toString();
    }

}
