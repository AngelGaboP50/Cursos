package com.example.cursos.gateway;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GatewayRoutesConfigTest {
    @Test
    void configurationClassExists() {
        assertNotNull(new GatewayRoutesConfig());
    }
}
