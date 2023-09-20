package com.purse.ex.controller;

import com.purse.ex.app.TransactionApp;
import com.purse.ex.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {
  private final TransactionApp transactionApp;


  /**
   * @param transaction transaction body
   * @return Mono<Transaction>
   */
  @PostMapping("/transaction")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Transaction> create(@RequestBody Transaction transaction) {
    return transactionApp.create(transaction);
  }

  /**
   * @param id transaction_id
   * @param transaction transaction body
   * @return Mono<Transaction>
   */
  @PutMapping(value = "/transaction/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Transaction> update(@PathVariable String id, @RequestBody Transaction transaction) {
    return transactionApp.update(id,transaction);
  }

  /**
   * @param id transaction_id
   * @return Mono<Transaction>
   */
  @GetMapping(value = "/transaction/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Transaction> get(@PathVariable String id) {
    return transactionApp.get(id);
  }

  /**
   * @return Flux<Transaction>
   */
  @GetMapping(value = "/transaction")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Transaction> all() {
    return transactionApp.all();
  }


}
