package loans.direct

import io.micronaut.test.annotation.MicronautTest
import loans.direct.persistence.ObjectSerialisation
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@MicronautTest
class ObjectSerialisationTest extends Specification {

    @Inject
    ObjectSerialisation mapper

    @Unroll
    def "deserialization and serialisation of json #json"() {
        expect:
        json == mapper.toJson(mapper.fromJson(json))

        where:
        json << [
                '{"@type":"OfferCreated.v1","id":{"id":1,"version":0},"side":"CREDITOR","timestamp":"2019-11-13T23:39:57.239385Z","creditor":{"id":1},"debtor":{"id":2},"amount":{"amount":100,"negative":false}}',
                '{"@type":"Rejected.v1","id":{"id":123,"version":0},"rejecting":{"id":1},"timestamp":"2019-11-13T19:17:38.973688Z"}',
                '{"@type":"Transaction.v1","id":{"id":1,"version":0},"details":{"timestamp":"2019-11-13T19:37:27.481673Z","from":{"id":1},"to":{"id":2},"amount":{"amount":100,"negative":false}}}',
                '{"@type":"Operation.v1","kind":"LOAN","details":{"timestamp":"2019-11-13T19:17:38.973688Z","from":{"id":1},"to":{"id":2},"amount":{"amount":25,"negative":false}},"balance":{"from":{"id":1},"to":{"id":2},"balance":{"amount":25,"negative":false}}}',
                '{"@type":"Operation.v2","type":"LOAN","details":{"timestamp":"2019-11-13T19:17:38.973688Z","from":{"id":1},"to":{"id":2},"amount":{"amount":25,"negative":false}},"balance":{"from":{"id":1},"to":{"id":2},"balance":{"amount":25,"negative":false}}}',
        ]
    }
}
