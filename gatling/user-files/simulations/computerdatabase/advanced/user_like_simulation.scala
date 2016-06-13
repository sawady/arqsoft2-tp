package computerdatabase.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.Random

class UserLikeSimulation extends Simulation {

  val apiVersion = "/api/v1/"
  val domain = "http://localhost:9200"
  val headersApi = Map("Content-Type" -> "application/json")
  val productsFeeder = csv("products.csv").circular
  val shopsFeeder = csv("shops.csv").circular
  val pricesFeeder = csv("found_prices.csv").random

  object ProductsStore {
    val store = exec(http("List Products")
        .get(apiVersion + "products"))
        .feed(productsFeeder)
        .exec(http("Post Products")
          .post(apiVersion + "products")
          .headers(headersApi)
          .body(StringBody("""{
            "product_id": "${product_id}",
            "name": "${name}"
          }""")).asJSON
        )
  }

  object ShopsStore {
    val store = exec(http("List Shops")
        .get(apiVersion + "shops"))
        .feed(shopsFeeder)
        .exec(http("Post Shops")
          .post(apiVersion + "shops")
          .headers(headersApi)
          .body(StringBody("""{
            "name": "${name}",
            "address": "${address}",
            "location": "${location}",
            "latitude": ${latitude},
            "longitude": ${longitude}
          }""")).asJSON
        )
  }

  object PricesStore {
    val store = exec(http("List Shops")
        .get(apiVersion + "shops")
        .check(jsonPath("$..items[*].id").findAll.saveAs("shops")))

        .exec((session: Session) => {
            val shopIds = session("shops").as[Seq[String]]
            val rand = new Random(System.currentTimeMillis())
            val randomId = rand.nextInt(shopIds.length)
            val shopId = shopIds(randomId)
            session.set("random_shop_id", shopId)
        })
        .feed(pricesFeeder)

        .exec(http("Post Prices")
          .post(apiVersion + "found_prices")
          .headers(headersApi)
          .body(StringBody("""{
            "product_id": "${product_id}",
            "price": ${price},
            "shop_id": "${random_shop_id}"
          }""")).asJSON
        )
  }

// "datetime": "${datetime}",

  val httpConf = http
    .baseURL(domain)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val products = scenario("Products Store").exec(ProductsStore.store)
  val shops = scenario("Shops Store").exec(ShopsStore.store)
  val prices = scenario("Prices Store").exec(PricesStore.store)
  
  setUp(
    products.inject(atOnceUsers(5)),
    shops.inject(atOnceUsers(2)),
    prices.inject(rampUsers(3) over (3 seconds))
  ).protocols(httpConf)
  //setUp(products.inject(rampUsers(10) over (10 seconds))).protocols(httpConf)


}
