package br.com.application.intregrationtests.controllers.person.withyaml.complexversion.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class YAMLMapper implements ObjectMapper { // implementando uma interface do RestAssured (usaremos dois ObjectMapper)

    /*
    precisamos referenciar onde está o pacote no momento de instanciar a váriavel,
    pois o Java ainda não dá suporte ao famoso "Alias"(múltiplas referências apontando para o mesmo objeto na memória)
    */
    private final com.fasterxml.jackson.databind.ObjectMapper mapper;
    protected TypeFactory factory;

    public YAMLMapper() {
        mapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory()) // criando o objectMapper para poder converter de JSON para YAML (serializar)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        factory = TypeFactory.defaultInstance();
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        var content = context.getDataToDeserialize().asString(); // variável que vai ser responsável por desserializar
        Class type = (Class) context.getType(); // obter o tipo da classe (ex: PersonDTO)
        try {
            return mapper.readValue(content, factory.constructType(type));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e); // colocando uma mensagem na exceção que pode ocorrer caso a desserialização não ocorra corretamente
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return mapper
                    .writeValueAsString(context.getObjectToSerialize()); // serializando nosso objeto (envolvendo em um try-catch)
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing YAML content", e); // colocando uma mensagem na exceção que pode ocorrer caso a serialização não ocorra corretamente
        }
    }


}
