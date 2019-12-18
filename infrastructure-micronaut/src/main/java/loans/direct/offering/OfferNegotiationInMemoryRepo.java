package loans.direct.offering;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OfferNegotiationInMemoryRepo implements OfferNegotiationRepository {

    private final Map<OfferId, OfferNegotiation> instances = new ConcurrentHashMap<>();

    @Override
    public OfferNegotiation get(OfferId id) {
        OfferNegotiation negotiation = instances.get(id);
        if (negotiation == null) {
            throw new IllegalArgumentException("no object for id " + id);
        }
        return negotiation;
    }

    @Override
    public void save(OfferNegotiation object) {
        instances.putIfAbsent(object.id, object);
    }
}
