package br.com.application.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";
    String BASEPATH_PARAM = "/api/person/v1";
    String BASEPATH_PARAM_WORKOUT = "/api/workout/v1";

    String ORIGIN_EXAMPLE = "https://example.com.br";
    String ORIGIN_ERROR = "https://error.com.br";
    String ORIGIN_LOCAL = "http://localhost:8080";


}
