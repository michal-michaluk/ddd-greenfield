package loans.direct.offering;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import loans.direct.Event;

import javax.inject.Singleton;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OfferNegotiationAsJsonInDocumentStoreRepo implements OfferNegotiationRepository {

    private final Map<OfferId, String> keyValueStore = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;
    private final Clock clock;

    public OfferNegotiationAsJsonInDocumentStoreRepo(ObjectMapper mapper, Clock clock) {
        this.mapper = mapper.copy()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .addMixIn(OfferNegotiation.class, TransientFields.class);

        this.clock = clock;
    }

    @Override
    public OfferNegotiation get(OfferId id) {
        String json = keyValueStore.get(id);
        if (json == null) {
            throw new IllegalArgumentException("no object for id " + id);
        }
        try {
            OfferNegotiation negotiation = mapper.readValue(json, OfferNegotiation.class);
            negotiation.events = new ArrayList<>();
            negotiation.clock = clock;
            return negotiation;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(OfferNegotiation object) {
        try {
            keyValueStore.put(object.id, mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TransientFields {
        @JsonIgnore
        List<Event> events;
        @JsonIgnore
        Clock clock;
    }
}
