# MyRetail

MyRetail is a Spring Boot API for retrieving product and pricing information.
It combines product information from the Redsky product service with pricing
information from a MongoDB database.

## Up and Running

This project uses Java 11. Java 11 or higher will be needed to compile and run the
code. In addition, Docker is used to start up a local version of the Mongo
database. A Makefile is provided to make starting the service easier.

First, build the Docker image:

```bash
make build
```

Next, start up the server and database:

```bash
make up
```

Optionally, you can start the database separately:

```bash
make services
```

You can also run the server without Docker:

```bash
make run
```

## Documentation

This API uses Swagger to document the routes. It can be accessed after
starting the server locally at `http://localhost:8080/swagger-ui.html`

In addition, design documentation is located in the `docs` folder

## Testing

This project uses Junit 4 to run unit tests. Running `make test` will
execute the test scripts

In addition, testing for the endpoints was done manually using Postman.
Automated integration testing can be added in the future. Please note that
since the database is running locally, you will need to seed the pricing
data (Otherwise it will return a 404).

`PUT http://localhost:8080/products/54456119`

Body: `application/json`:
```bash
{
    "value": 13.57,
    "currency_code": "USD"
}
```

Returns `204`

---

`GET http://localhost:8080/products/54456119`

Returns `application/json`
```bash
{
    "id": 54456119,
    "name": "Creamy Peanut Butter 40oz - Good &#38; Gather&#8482;",
    "current_price": {
        "value": 13.57,
        "currency_code": "USD"
    }
}
```
