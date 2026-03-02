## Preview

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/1.png" alt="nahhh" width="600"/><br/>
  
</p>

---
<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/2.png" alt="nahhh" width="600"/><br/>
  
</p>

---
<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/3.png" alt="nahhh" width="300"/><br/>
  
</p>

---
<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/API_testing/images/4.png" alt="nahhh" width="600"/><br/>
  
</p>

---

Automated API Testing in Postman

API in Postman: environments, request collections, pre-request scripts, tests, sequential and parallel load, export of collection to JSON.

1. Application Startup

The application (Spring Boot) runs locally and exposes the API on port 8080.

Example startup command:

- java -jar target/lab2-calculator-service-1.0.0.jar

Base URL for local:

- http://localhost:8080

2. Environments

Two environments were created:

1. local

- protocol = http
- host = localhost
- port = 8080

2. dev

- protocol = http
- host = dev-server
- port = 8080

The baseUrl variable is automatically assembled in the collection-level pre-request script.

3. Postman Collection

Collection name:

- lab5-calculator-api-tests

A collection-level pre-request script builds baseUrl from environment variables.

Pre-request (collection level):

- protocol is taken from the environment
- host is taken from the environment
- port is taken from the environment
- baseUrl = ${protocol}://${host}:${port}

All requests use the variable:

- {{baseUrl}}

instead of a hardcoded URL.

4. Requests in the Collection

Five main requests are implemented according to the assignment:

1. Calc – ADD

- POST {{baseUrl}}/api/calc
- op = +

2. Calc – SUB

- POST {{baseUrl}}/api/calc
- op = -

3. Calc – MUL

- POST {{baseUrl}}/api/calc
- op = *

4. Calc – DIV

- POST {{baseUrl}}/api/calc
- op = /

5. Calculations – SEARCH

- GET {{baseUrl}}/api/calculations?from=...&to=...
- The controller uses ISO_DATE_TIME format for from/to parameters (OffsetDateTime).

Additional negative requests were added to verify error handling:

- Calc – DIV – div0 (division by zero)
- Calc – BAD – invalid radix
- Calc – BAD – missing op

5. Collection Variables (to avoid duplicating request body)

Collection Variables were defined to construct the request body dynamically:

- leftValue
- leftRadix
- rightValue
- rightRadix
- resultRadix

All Calc requests share the same body structure; only the op field changes.

6. Tests (Post-response Scripts)

Each request contains tests in Scripts – Post-response (in recent Postman versions this replaces the old Tests tab).

Positive tests include:

- validation of HTTP status (200)
- validation that response is JSON
- validation of response structure (id, resultValue, resultRadix, createdAt)
- validation of correct calculation result (for ADD/SUB/MUL/DIV)
- for SEARCH – validation that response is an array and that createdAt is within the [from,to] range

Negative tests include:

- div0 – expected 422
- invalid radix – expected 400
- missing op – expected 400

7. Load Testing – Sequential Execution (1000 Iterations)

Executed in Collection Runner (Functional mode), Iterations = 1000.

Results:

- Errors: 0
- Avg response time: 8 ms
- Duration: 14m 5s

Conclusion:

- Under sequential load, the service is stable
- No errors occurred
- Average response time remains low

8. Load Testing – Parallel Execution

Executed in Performance mode:

- Load profile: Fixed
- Virtual users: 10
- Test duration: 2 mins
- Environment: local

Results:

- Total requests sent: 5206
- Requests/second: 40.93
- Avg response time: 11 ms
- P90: 43 ms
- P95: 54 ms
- P99: 74 ms
- Error rate: 37.40%

Explanation of error rate:

- The request set includes negative test cases that intentionally return 4xx responses (div0, invalid radix, missing op)
- In Performance mode, such responses are counted as errors in the charts, therefore the overall Error rate is high and expected when mixing positive and negative scenarios

Conclusion:

- Under parallel load, latency increased slightly (8 ms → 11 ms avg)
- High error rate is caused by intentional negative scenarios (expected behavior)
- The service remains operational at concurrency=10, with no critical 5xx errors observed (when correctly interpreting 4xx responses for negative scenarios as valid outcomes)

9. Artifacts in Repository

The Postman collection was exported:

- postman/lab5-calculator-api-tests.postman_collection.json

Optionally exported environments:

- postman/local.postman_environment.json
- postman/dev.postman_environment.json
