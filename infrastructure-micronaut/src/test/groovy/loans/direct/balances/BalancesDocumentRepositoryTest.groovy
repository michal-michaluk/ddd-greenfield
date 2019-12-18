package loans.direct.balances

import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import loans.direct.*
import loans.direct.users.UserDetail
import loans.direct.users.UserDetails
import spock.lang.Ignore
import spock.lang.Specification

import javax.inject.Inject
import java.time.Instant

@Ignore("add jackson key serializer for UserId")
@MicronautTest
class BalancesDocumentRepositoryTest extends Specification implements RandomUsersTesting {

    @Inject
    BalancesDocumentRepository repo
    def users = [:]

    @MockBean(UserDetails)
    UserDetails userDetails() {
        return { user -> users[user] }
    }

    def "changes are persistent"() {
        def user = exampleUser()
        given:
        repo.init(user)

        when:
        def object = repo.get(user)
        object.apply(loan(user))
        repo.save(object)

        and:
        object = repo.get(user)
        def summary = object.get()

        then:
        summary != null
    }

    Operation loan(UserId from) {
        Operation.loan(
                new TransactionDetails(Instant.now(), from, exampleCounterParty(), Money.of(35)),
                new Balance(from, to, Money.of(35))
        )
    }

    UserId exampleUser() {
        def id = someUser()
        users[id] = new UserDetail(id, "Michał", "img/empty.jpg")
        id
    }

    UserId exampleCounterParty() {
        def id = someUser()
        users[id] = new UserDetail(id, "Maciek", "img/empty.jpg")
        id
    }
}
