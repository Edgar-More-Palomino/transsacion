package com.nttdata.transaccion.repository;
import com.nttdata.transaccion.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
}