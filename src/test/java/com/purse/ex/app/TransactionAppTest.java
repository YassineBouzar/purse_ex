package com.purse.ex.app;

import com.purse.ex.entity.OrderLine;
import com.purse.ex.entity.Status;
import com.purse.ex.entity.Transaction;
import com.purse.ex.entity.Type;
import com.purse.ex.exception.BadRequestException;
import com.purse.ex.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.purse.ex.entity.Status.AUTHORIZED;
import static com.purse.ex.entity.Status.CAPTURED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionAppTest {
    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionApp transactionApp;
    Transaction transaction;
    @BeforeEach
    void setUp() {

        OrderLine paires = OrderLine
                .builder()
                .name("paires de gants de sk")
                .price(BigDecimal.valueOf(10.00))
                .quantity(4)
                .ref("sick")
                .build();
        OrderLine bonnet = OrderLine
                .builder()
                .name("bonnet en laine")
                .price(BigDecimal.valueOf(14.80))
                .quantity(1)
                .ref("sick")
                .build();
         transaction =Transaction
                .builder()
                .total(BigDecimal.valueOf(54.80))
                .type(Type.BANK_CARD)
                .status(Status.INIT)
                 .orderLineList(List.of(paires,bonnet))
                .build();
    }
    @Test
    void create() {
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        // Act
        Mono<Transaction> mockTransaction =transactionApp.create(transaction);

        // Assert if transaction created
        StepVerifier.create(mockTransaction)
                .expectNext(transaction)
                .expectComplete()
                .verify();

    }
    @Test
    void updateTransaction() {
        String transaction_id = UUID.randomUUID().toString();
        transaction.setId(transaction_id);

        transaction.setStatus(AUTHORIZED);

        when(transactionRepository.findById(transaction_id)).thenReturn(Mono.just(transaction));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        // call update endpoint
        Mono<Transaction> mockTransaction =transactionApp.update(transaction_id,transaction);

        assertEquals(Objects.requireNonNull(mockTransaction.block()).getStatus(),AUTHORIZED);

    }
    // this  test must have thrown a BadRequestException because we can not have CAPTURED transaction from INIT directly
    @Test
    void shouldTrowBadRequestException() {
        String transaction_id = UUID.randomUUID().toString();
        transaction.setId(transaction_id);

        Transaction underTest =Transaction.builder()
        .total(transaction.getTotal())
        .id(transaction.getId())
        .orderLineList(transaction.getOrderLineList())
        .status(CAPTURED).build();



        when(transactionRepository.findById(transaction_id)).thenReturn(Mono.just(transaction));

        StepVerifier.create(transactionApp.update(transaction_id,underTest))
                .expectError(BadRequestException.class)
                .verify();

    }
}