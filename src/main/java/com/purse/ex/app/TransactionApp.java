package com.purse.ex.app;

import com.purse.ex.entity.Status;
import com.purse.ex.entity.Transaction;
import com.purse.ex.exception.BadRequestException;
import com.purse.ex.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

import static com.purse.ex.entity.Status.INIT;

@Component
@RequiredArgsConstructor
public class TransactionApp {
    private final TransactionRepository transactionRepository;

    public Mono<Transaction> create(Transaction transaction){

        transaction.setTotal(total(transaction));

        transaction.setStatus(INIT);

        return transactionRepository.save(transaction);
    }


    public Mono<Transaction> update(String id , Transaction transaction){
        return get(id)
                .flatMap(update -> {
                    update.setType(transaction.getType());
                    update.setTotal(total(transaction));
                    if (!transaction.getOrderLineList().equals(update.getOrderLineList())){
                        if (!update.getStatus().equals(INIT)){
                            return Mono.error(new BadRequestException("Transaction is "+ update.getStatus() +" status you can not update the order "));
                        }
                        update.setOrderLineList(transaction.getOrderLineList());
                    }
                    update.setStatus(status(update.getStatus(),transaction.getStatus()));
                    return transactionRepository.save(update);
                });

    }


    public Mono<Transaction> get(String id) {
        return transactionRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new BadRequestException("Transaction not found")));

    }

    public Flux<Transaction> all() {
        return transactionRepository
                .findAll();
    }
    private BigDecimal total(Transaction transaction) {
        BigDecimal bigDecimal = transaction.getOrderLineList()
                .stream()
                .map(order -> order.getPrice()
                        .multiply(new BigDecimal(order.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(null);

        if (!Objects.equals(bigDecimal, transaction.getTotal())){
            throw new BadRequestException("Invalid total entry !");
        }
        return bigDecimal;
    }

    public Status status(Status current, Status incoming) {
        if (current == incoming ||
                (current == Status.INIT && incoming == Status.AUTHORIZED) ||
                (current == Status.AUTHORIZED && incoming == Status.CAPTURED)) {
            return incoming;
        }
        throw new BadRequestException("Invalid transaction status");
    }

}
