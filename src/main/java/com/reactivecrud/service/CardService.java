package com.reactivecrud.service;

import com.reactivecrud.entity.Card;

import com.reactivecrud.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import static com.reactivecrud.service.CardTypeGenerate.createTypes;
import static com.reactivecrud.service.CardTypeGenerate.validateTypeCard;

@Service
public class CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }




    public Mono<Card> insert(Mono<Card> cardMono){
        return cardMono
                .flatMap(card -> {
                    card.setType(validateTypeCard(createTypes(),card.getCode()));
                    return cardRepository.save(card);
                });
    }


    public Flux<Card> listAll(){
        return cardRepository.findAll();
    }

    public Flux<Card> listByType(String type){
        return cardRepository.findByType(type);
    }

    public Mono<Card> getCard(String id){
        return cardRepository.findById(id);
    }

    public Mono<Void> deleteCard(String id){
        return cardRepository.deleteById(id);
    }

}
