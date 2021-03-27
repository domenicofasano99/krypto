package com.bok.krypto.repository;


import com.bok.krypto.model.Krypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface KryptoRepository extends JpaRepository<Krypto, Long> {

    Optional<Krypto> findBySymbol(String name);

    List<Krypto> findBySymbolIn(List<String> codes);

    Boolean existsBySymbol(String symbol);

    @Query("select k.price from Krypto k where k.symbol in : symbols")
    List<Projection.KryptoPrice> findKryptoPrices(@Param("symbols") List<String> symbols);

    @Query("SELECT k FROM Krypto k WHERE k.symbol = :symbol")
    Projection.KryptoPrice findPriceBySymbol(@Param("symbol") String symbol);

    public class Projection {
        public interface KryptoPrice {
            String getSymbol();

            BigDecimal getPrice();
        }
    }
}
