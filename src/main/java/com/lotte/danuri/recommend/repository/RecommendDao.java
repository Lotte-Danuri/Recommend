package com.lotte.danuri.recommend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendDao {
    @Autowired
    private MongoTemplate mongoTemplate;

}
