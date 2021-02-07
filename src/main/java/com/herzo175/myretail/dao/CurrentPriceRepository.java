package com.herzo175.myretail.dao;

import com.herzo175.myretail.model.CurrentPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CurrentPriceRepository extends MongoRepository<CurrentPrice, Long> {}
