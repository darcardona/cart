package posti.examples.retail.cart.adapters.mongo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import posti.examples.retail.cart.application.domain.SequenceProvider;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class MongoSequenceProvider implements SequenceProvider {
    @NonNull
    private final MongoOperations mongo;

    @Override
    public long next(String seqName) {
        Sequence counter = mongo.findAndModify(
            query(where("_id").is(seqName)),
            new Update().inc("next",1),
            options().returnNew(true).upsert(true),
            Sequence.class
        );

        return counter.getNext();
    }

    @Value
    private static class Sequence {
        @Id
        private String id;
        private long next;
    }
}
