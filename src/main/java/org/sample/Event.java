package org.sample;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.swiggy.dp.JsonSerializer;

import java.io.IOException;
import java.io.Serializable;

public class Event implements Serializable, JsonSerializable{
    private Long swiggyId;

    private String userName;

    public Event(Long swiggyId, String userName) {
        this.swiggyId = swiggyId;
        this.userName = userName;
    }

    public Long getSwiggyId() {
        return swiggyId;
    }

    public void setSwiggyId(Long swiggyId) {
        this.swiggyId = swiggyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {

    }
}
