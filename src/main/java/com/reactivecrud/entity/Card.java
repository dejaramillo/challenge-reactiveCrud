package com.reactivecrud.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Document
public class Card {


    @Id
    private String number;
    private String title;
    private String date;
    private Type type;
    private String code;




    public Card(String number, String title, String date, String code) {
        this.number = number;
        this.title = title;
        this.date = cardDate(date);
        this.code = code;
        createTypes();
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

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    private String cardDate(String date){
      var reformatDate =   date.split("-");
      return date(Integer.parseInt(reformatDate[0]),Integer.parseInt(reformatDate[1]));
    }

    public String date(int month, int year) {
        String format = "";
        LocalDate date;
        int day = 1;
        try {
            date = LocalDate.of(year, month, day);
            if(date.isBefore(LocalDate.now())){
                throw new IllegalArgumentException("No valid the date of card");
            }
        } catch (DateTimeException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return format = generateFormat(date);
    }

    private String generateFormat(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
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
