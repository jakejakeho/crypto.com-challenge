package org.example.security;

import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final SecurityRepository securityRepository;

    CacheLoader<String, List<Security>> optionCache = new CacheLoader<String, List<Security>>() {
        @Override
        public List<Security> load(String symbol) {
            return findAllOptionsBySymbolInner(symbol);
        }
    };

    public SecurityServiceImpl(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Override
    public List<Security> findAllOptionsBySymbol(String symbol) {
        try {
            return optionCache.load(symbol);
        } catch (Exception e) {
            log.error("Failed to retrieve {} options", symbol);
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<Security> findAllStocks() {
        return ((List<Security>) securityRepository.findAll())
                .stream()
                .filter(s -> Objects.equals(s.getSecurityType(), SecurityType.STOCK.toString()))
                .collect(Collectors.toList());
    }

    private List<Security> findAllOptionsBySymbolInner(String symbol) {
        List<Security> securities = (List<Security>) securityRepository.findAll();
        List<Security> options;
        if (!CollectionUtils.isEmpty(securities)) {
            options = new ArrayList<>();
            for (Security security : securities) {
                if ((Objects.equals(security.getSecurityType(), SecurityType.CALL.toString())
                        || Objects.equals(security.getSecurityType(), SecurityType.PUT.toString()))
                        && security.getSymbol().startsWith(symbol)) {
                    options.add(security);
                }
            }
        } else {
            options = Collections.emptyList();
        }
        return options;
    }
}
