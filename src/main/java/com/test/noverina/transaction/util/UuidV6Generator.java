package com.test.noverina.transaction.util;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
public class UuidV6Generator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return  Generators.timeBasedReorderedGenerator().generate().toString();
    }

    public String generate() {
        return  Generators.timeBasedReorderedGenerator().generate().toString();
    }
}
