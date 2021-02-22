package com.bok.krypto.model;
import javax.persistence.*;
@Entity
public class KryptoHistory {
    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @Column
    public String symbol;

    @Column
    public String slug;

    @Column
    public Integer numMarketPairs;

    @Column
    public String dateAdded;

    @Column
    public Double circulatingSupply;

    @Column
    public Double totalSupply;

    @Column
    public Integer cmcRank;

    @Column
    public String lastUpdated;

    @Column
    public Double price;

    @Column
    public Double volume24h;

    @Column
    public Double percentChange1h;

    @Column
    public Double percentChange24h;

    @Column
    public Double percentChange7d;

    @Column
    public Double percentChange30d;

    @Column
    public Double marketCap;
}
