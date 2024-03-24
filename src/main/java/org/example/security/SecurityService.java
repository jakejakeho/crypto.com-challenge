package org.example.security;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SecurityService {

    List<Security> findAllOptionsBySymbol(String symbol);
}
