package com.jstik.fancy.account.handler.operation;

import reactor.core.publisher.Mono;

public interface DMLOperationHandler<T> {

    <S extends T> Mono<S> handleInsert(Mono<S> userMono);

    <S extends T> Mono<S> handleUpdate(Mono<S> userMono);


    default DMLOperationHandler<T> andThen(DMLOperationHandler<T> after) {
        return new DMLOperationHandler<T>() {
            @Override
            public <S extends T>  Mono<S> handleInsert(Mono<S> userMono) {
                Mono<S> mono = DMLOperationHandler.this.handleInsert(userMono);
                return after.handleInsert(mono);
            }

            @Override
            public <S extends T>  Mono<S> handleUpdate(Mono<S> userMono) {
                Mono<S> mono = DMLOperationHandler.this.handleUpdate(userMono);
                return after.handleUpdate(mono);
            }
        };
    }

}
