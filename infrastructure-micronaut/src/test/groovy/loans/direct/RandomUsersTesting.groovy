package loans.direct

trait RandomUsersTesting {
    Random random = new Random()

    UserId someUser() {
        new UserId(random.nextLong())
    }
}
