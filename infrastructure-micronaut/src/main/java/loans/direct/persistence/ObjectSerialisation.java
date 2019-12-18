package loans.direct.persistence;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import loans.direct.*;
import loans.direct.account.AccountId;
import loans.direct.offering.OfferCreated;
import loans.direct.offering.Rejected;
import loans.direct.offering.TransactionRecorded;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Singleton
public class ObjectSerialisation {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = OfferCreated.class, name = "OfferCreated.v1"),
            @JsonSubTypes.Type(value = OperationV1.class, name = "Operation.v1"),
            @JsonSubTypes.Type(value = Operation.class, name = "Operation.v2"),
            @JsonSubTypes.Type(value = AccountSnapshot.class, name = "AccountSnapshot.v1"),
            @JsonSubTypes.Type(value = Rejected.class, name = "Rejected.v1"),
            @JsonSubTypes.Type(value = TransactionRecorded.class, name = "Transaction.v1")
    })
    private static class EventsVersionedTypes {
    }


    private final ObjectMapper mapper;

    public ObjectSerialisation(ObjectMapper mapper) {
        this.mapper = mapper.copy();
        this.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.mapper.addMixIn(Event.class, EventsVersionedTypes.class);
    }

    public String toJson(Object object) {
        Objects.requireNonNull(object, "null object given to serialisation");
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("while serialisation object of type " + object.getClass() + ", value: " + object, e);
        }
    }

    public Event fromJson(String json) {
        Event event = fromJson(json, Event.class);
        if (event instanceof ObsoleteForm) {
            return ((ObsoleteForm) event).migrate();
        }
        return event;
    }

    public <T> T fromJson(String json, Class<T> type) {
        Objects.requireNonNull(json, "null json given to deserialization");
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException("while deserialization event form json:  " + json, e);
        }
    }

    @Value
    private static class AccountSnapshot implements Event {
        final AccountId id;
        private final UserId from;
        private final UserId to;
        private Money balance;
    }

    @Value
    private static class OperationV1 implements Event, ObsoleteForm {

        loans.direct.Operation.Kind kind;
        TransactionDetails details;
        Balance balance;

        @Override
        public Operation migrate() {
            return new Operation(
                    kind,
                    details,
                    balance
            );
        }
    }

    interface ObsoleteForm {
        Operation migrate();
    }
}
