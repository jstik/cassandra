package com.jstik.fancy.account.handler.operation;

import reactor.core.publisher.Mono;

public interface DMLOperationHandler<T> {

    Mono<T> handleInsert(Mono<T> userMono);

    Mono<T> handleUpdate(Mono<T> userMono);


    default DMLOperationHandler<T> andThen(DMLOperationHandler<T> after) {
        return new DMLOperationHandler<T>() {
            @Override
            public Mono<T> handleInsert(Mono<T> userMono) {
                Mono<T> mono = DMLOperationHandler.this.handleInsert(userMono);
                return after.handleInsert(mono);
            }

            @Override
            public Mono<T> handleUpdate(Mono<T> userMono) {
                Mono<T> mono = DMLOperationHandler.this.handleUpdate(userMono);
                return after.handleUpdate(mono);
            }
        };
    }

}
