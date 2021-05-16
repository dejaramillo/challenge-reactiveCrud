package com.reactivecrud.service;

import com.reactivecrud.entity.Card;
import com.reactivecrud.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    public Mono<Void> insert(Mono<Card> cardMono){
        return cardMono
                .flatMap(cardRepository::save).then().log();
    }

}
