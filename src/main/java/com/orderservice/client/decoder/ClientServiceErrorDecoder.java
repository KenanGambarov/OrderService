package com.orderservice.client.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.orderservice.client.decoder.JsonNodeFieldName.MESSAGE;
import static com.orderservice.exception.ExceptionConstants.CLIENT_ERROR;

@Slf4j
public class ClientServiceErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        var errorMessage = CLIENT_ERROR.getMessage();

        JsonNode jsonNode;
        try(var body = response.body().asInputStream()) {
            log.info("body status {}" , response.status());
            jsonNode = new ObjectMapper().readValue(body, JsonNode.class);
        }catch (Exception e){
            throw new FeignClientException(CLIENT_ERROR.getMessage(),response.status());
        }

        log.error("ClientService decode error Message: {}, Method {}, Status {} ",errorMessage,methodKey,response.status());
        if(jsonNode.has(MESSAGE.getValue()))
            errorMessage=jsonNode.get(MESSAGE.getValue()).asText();

        return new FeignClientException(errorMessage,response.status());
    }
}
