
package com.bok.krypto.external;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "price",
        "volume_24h",
        "percent_change_1h",
        "percent_change_24h",
        "percent_change_7d",
        "percent_change_30d",
        "market_cap",
        "last_updated"
})
public class USD {

    @JsonProperty("price")
    public Double price;
    @JsonProperty("volume_24h")
    public Double volume24h;
    @JsonProperty("percent_change_1h")
    public Double percentChange1h;
    @JsonProperty("percent_change_24h")
    public Double percentChange24h;
    @JsonProperty("percent_change_7d")
    public Double percentChange7d;
    @JsonProperty("percent_change_30d")
    public Double percentChange30d;
    @JsonProperty("market_cap")
    public Double marketCap;
    @JsonProperty("last_updated")
    public String lastUpdated;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public USD() {
    }

    /**
     * @param percentChange30d
     * @param marketCap
     * @param lastUpdated
     * @param percentChange24h
     * @param percentChange7d
     * @param price
     * @param volume24h
     * @param percentChange1h
     */
    public USD(Double price, Double volume24h, Double percentChange1h, Double percentChange24h, Double percentChange7d, Double percentChange30d, Double marketCap, String lastUpdated) {
        super();
        this.price = price;
        this.volume24h = volume24h;
        this.percentChange1h = percentChange1h;
        this.percentChange24h = percentChange24h;
        this.percentChange7d = percentChange7d;
        this.percentChange30d = percentChange30d;
        this.marketCap = marketCap;
        this.lastUpdated = lastUpdated;
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
        return new ToStringBuilder(this).append("price", price).append("volume24h", volume24h).append("percentChange1h", percentChange1h).append("percentChange24h", percentChange24h).append("percentChange7d", percentChange7d).append("percentChange30d", percentChange30d).append("marketCap", marketCap).append("lastUpdated", lastUpdated).append("additionalProperties", additionalProperties).toString();
    }

}
