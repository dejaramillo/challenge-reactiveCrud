package com.reactivecrud.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.HashMap;
import java.util.Map;

@Document
public class Card {


    @Id
    private String number;
    private String title;
    private Type type;
    private String code;


    public Card() {
    }

    public Card(String number, String title, String code) {
        this.number = number;
        this.title = title;
        createTypes();
        this.code = code;
        this.type = validateTypeCard(createTypes());
    }

    public String getNumber() {
        return number;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private Map<String, Type> createTypes(){
        Map<String, Type> types = new HashMap<>();
        types.put("06", type.MasterdCard);
        types.put("03",type.VISA);
        types.put("12", type.PRIME);
        return types;
    }

    private Type validateTypeCard(Map<String, Type> types){
        var validateCode = code.split("-");
        var validateMap =types.get(validateCode[0]);
        if (validateMap == null)
            throw new IllegalArgumentException("codigo invalido");
        return validateMap;
    }


    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", code='" + code + '\''    +
                '}';
    }
}
