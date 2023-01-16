package com.reactive.service;

import com.reactive.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public ReservationServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Reservation> getReservation(String id) {
        try {
            return reactiveMongoTemplate.findById(id, Reservation.class);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Mono<Reservation> createReservation(Mono<Reservation> reservationMono) {
        return reactiveMongoTemplate.save(reservationMono);
    }

    @Override
    public Mono<Reservation> updateReservation(String id, Mono<Reservation> reservationMono) {
        //upsert functionality
        // return reactiveMongoTemplate.save(reservationMono);

        //update just price
        return reservationMono.flatMap(reservation -> {
                    return reactiveMongoTemplate.findAndModify(
                            Query.query(Criteria.where("id").is(id)),
                            Update.update("price", reservation.getPrice()), Reservation.class).flatMap(result -> {
                        result.setPrice(reservation.getPrice());
                        return Mono.just(result);
                    });
                }
        );
    }

    @Override
    public Mono<Boolean> deleteReservation(String id) {
        return reactiveMongoTemplate.remove(
                        Query.query(Criteria.where("id").is(id)), Reservation.class)
                .flatMap(deleteResult -> Mono.just(deleteResult.wasAcknowledged())
                );
    }
}
