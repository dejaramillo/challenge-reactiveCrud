package com.reactivecrud.controller;

import com.reactivecrud.entity.Card;
import com.reactivecrud.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping(value = "/save")
    public Mono<Void> save(@RequestBody Mono<Card> cardMono){
        return cardService.insert(cardMono);
    }


}
