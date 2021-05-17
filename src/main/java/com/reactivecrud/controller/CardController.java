package com.reactivecrud.controller;

import com.reactivecrud.entity.Card;
import com.reactivecrud.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping(value = "/save")
    public Mono<Void> save(@RequestBody Mono<Card> cardMono) {
        return cardService.insert(cardMono);
    }


    @GetMapping(value = "/all")
    public Flux<Card> listCards() {
        return cardService.listAll();
    }

    @GetMapping(value = "/{type}/type")
    public Flux<Card> listByType(@PathVariable("type") String type) {
        return cardService.listByType(type);
    }

    @GetMapping(value = "/{id}")
    public  Mono<Card> getCard(@PathVariable("id") String id){
        return cardService.getCard(id);
    }

    @DeleteMapping(value = "/{id}/del")
    public Mono<Void> delete(@PathVariable("id") String id){
        return cardService.deleteCard(id);
    }

    @PutMapping(value = "/up")
    public Mono<Void> update(@RequestBody Mono<Card> personMono) {
        return cardService.insert(personMono);
    }


}
