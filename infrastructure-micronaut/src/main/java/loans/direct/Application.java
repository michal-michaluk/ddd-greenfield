package loans.direct;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.Micronaut;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.time.Clock;

@Factory
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public Sql2o sql(DataSource dataSource) {
        return new Sql2o(dataSource);
    }
}
