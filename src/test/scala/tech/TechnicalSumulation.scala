package tech

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.ThreadLocalRandom
import scala.util.Random

class TechnicalSumulation extends Simulation {

    val userAgents = Array(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    )

    // Create a feeder that randomly selects a user agent from the list
    val userAgentFeeder = Iterator.continually(Map("userAgent" -> userAgents(Random.nextInt(userAgents.length))))


    val tech = exec(
        http("Home")
            .get("/inventory")
            .header("User-Agent", "#{userAgent}")
            .check(status.is(200))
        
    )


    val httpProtocol =
        http.baseUrl("http://127.0.0.1:8000")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"
        )

    val users = scenario("Users")
                .feed(userAgentFeeder)
                .exec(tech)

    setUp(
        users.inject(constantUsersPerSec(20).during (1))
    ).protocols(httpProtocol)

}
