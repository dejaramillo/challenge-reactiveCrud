package com.reactivecrud.controller;

import com.reactivecrud.entity.Card;
import com.reactivecrud.repository.CardRepository;
import com.reactivecrud.service.CardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CardController.class)
class CardControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private CardService cardService;

    @Captor
    private ArgumentCaptor<Mono<Card>> argumentCaptor;

    @MockBean
    private CardRepository repository;

    @ParameterizedTest
    @CsvSource({"ctc,06-524,0", "ctc,06-524,1"})
    void post(String number, String title, String code, Integer times){

       if (times == 0) {
            when(repository.findByCode(code)).thenReturn(Mono.just(new Card()));
        }
        if (times == 1) {
            when(repository.findByCode(code).thenReturn(Mono.empty()));
        }

        var request  = Mono.just(new Card(number,title,code));
        webTestClient.post().uri("/card/save")
                .body(request, Card.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        verify(cardService).insert(argumentCaptor.capture());
        verify(repository, times(times)).save(any());

        var card = argumentCaptor.getValue().block();

        assertEquals(number, card.getNumber());
        assertEquals(title, card.getTitle());
        assertEquals(code, card.getCode());

    }

}