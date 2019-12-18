package loans.direct.offering;

public interface OfferNegotiationRepository {
    OfferNegotiation get(OfferId id);

    void save(OfferNegotiation object);
}
