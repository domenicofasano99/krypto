package com.bok.krypto.external;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "symbol",
        "slug",
        "num_market_pairs",
        "date_added",
        "tags",
        "max_supply",
        "circulating_supply",
        "total_supply",
        "platform",
        "cmc_rank",
        "last_updated",
        "quote"
})
public class Datum {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("slug")
    public String slug;

    @JsonProperty("num_market_pairs")
    public Integer numMarketPairs;

    @JsonProperty("date_added")
    public String dateAdded;

    @JsonProperty("tags")
    public List<String> tags = null;

    @JsonProperty("max_supply")
    public Object maxSupply;

    @JsonProperty("circulating_supply")
    public Double circulatingSupply;

    @JsonProperty("total_supply")
    public Double totalSupply;

    @JsonProperty("platform")
    public Object platform;

    @JsonProperty("cmc_rank")
    public Integer cmcRank;

    @JsonProperty("last_updated")
    public String lastUpdated;

    @JsonProperty("quote")
    public Quote quote;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Datum() {
    }

    /**
     * @param symbol
     * @param totalSupply
     * @param cmcRank
     * @param dateAdded
     * @param circulatingSupply
     * @param platform
     * @param tags
     * @param lastUpdated
     * @param quote
     * @param numMarketPairs
     * @param name
     * @param id
     * @param maxSupply
     * @param slug
     */
    public Datum(Integer id, String name, String symbol, String slug, Integer numMarketPairs, String dateAdded, List<String> tags, Object maxSupply, Double circulatingSupply, Double totalSupply, Object platform, Integer cmcRank, String lastUpdated, Quote quote) {
        super();
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.numMarketPairs = numMarketPairs;
        this.dateAdded = dateAdded;
        this.tags = tags;
        this.maxSupply = maxSupply;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.platform = platform;
        this.cmcRank = cmcRank;
        this.lastUpdated = lastUpdated;
        this.quote = quote;
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
        return new ToStringBuilder(this).append("id", id).append("name", name).append("symbol", symbol).append("slug", slug).append("numMarketPairs", numMarketPairs).append("dateAdded", dateAdded).append("tags", tags).append("maxSupply", maxSupply).append("circulatingSupply", circulatingSupply).append("totalSupply", totalSupply).append("platform", platform).append("cmcRank", cmcRank).append("lastUpdated", lastUpdated).append("quote", quote).append("additionalProperties", additionalProperties).toString();
    }

}
