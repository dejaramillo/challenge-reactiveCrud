package com.reactivecrud.controller;

import com.reactivecrud.entity.Card;
import com.reactivecrud.repository.CardRepository;
import com.reactivecrud.service.CardService;

import org.junit.jupiter.api.Test;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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
    @CsvSource({"010,ctc,03-2025,06-524,0", "010,ctc,03-2025,06-524,1"})
    void post(String number, String title, String date,String code, Integer times){

        if (times == 0) {
           when(repository.findByTitle(title)).thenReturn(Mono.just(new Card()));
        }
        if (times == 1) {
            when(repository.findByTitle(title).thenReturn(Mono.empty()));
        }

        var request  = Mono.just(new Card(number,title,date,code));
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

    @Test
    void list() {
        var list = Flux.just(
                new Card("40001111111","creditCard","03-2022","06-0225")
        );

            when(repository.findAll()).thenReturn(list);
        webTestClient.get()
                .uri("/card/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("creditCard")
                .jsonPath("$[0].date").isEqualTo("03-2022")
                .jsonPath("$[0].code").isEqualTo("06-0225");

        verify(cardService).listAll();
        verify(repository).findAll();
    }

    @Test
    void getByType() {
        var list = Flux.just(
                new Card("01","premiumupdate","03-2025","06-0225")
        );

        when(repository.findByType("VISA")).thenReturn(list);
        webTestClient.get()
                .uri("/card/VISA/type")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("premiumupdate");

        verify(cardService).listByType("VISA");
        verify(repository).findByTitle("VISA");
    }

    @Test
    void get() {
        webTestClient.get()
                .uri("/card/01")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Card.class)
                .consumeWith(cardEntityExchangeResult -> {
                    var card = cardEntityExchangeResult.getResponseBody();
                    assert card == null;
                });


    }

    @Test
    void delete() {
        webTestClient.delete()
                .uri("/card/01/del")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void update() {
        var request = Mono.just(new Card("01","prem","03-2026","03-2529"));
        webTestClient.put()
                .uri("/card/up")
                .body(request, Card.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

}