package com.bhavik.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class StudentRnoGenerator implements IdentifierGenerator {
    String prefix;
    public StudentRnoGenerator(RnoId rnoId) {
        this.prefix = rnoId.name();
    }
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Random random = new Random();
        String rno = prefix + "-" + random.nextInt(1000,9999);
        return rno;
    }
}
