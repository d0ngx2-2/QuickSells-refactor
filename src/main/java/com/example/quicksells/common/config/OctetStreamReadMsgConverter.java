package com.example.quicksells.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
/**
 *Swagger 멀티파트 요청 시 JSON 파트의 Content-type을 누락하여
 * application/octet-stream으로 전송되는 문제를 해결하기 위한 컨스텀 컨버터
 */
public class OctetStreamReadMsgConverter extends AbstractJackson2HttpMessageConverter {
    public OctetStreamReadMsgConverter(ObjectMapper objectMapper) {
        // 컨버터가 application/octet-steeam 타입을 담당함
        //부모 클래스인 Jackson 컨버터에게 application/octet-stream타입도 Json처럼 앍을 수 있도록 범위를 넓혀줌

        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(MediaType mediaType) {
        return false;
    }

}
