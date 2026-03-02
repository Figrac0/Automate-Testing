# Load Testing of Calculator REST API with Apache JMeter

## Objective

Explore load testing capabilities using Apache JMeter and analyze application behavior under varying load intensities on a REST service endpoint.

---

## Tested Application

**Endpoint:** `POST /api/calc`

```json 
{
    "a": 10,
    "b": 5,
    "operation": "add"
}
```

**Responses:**
- HTTP 200 — successful operation
- HTTP 422 — validation error (e.g., division by zero)

---

## JMeter Project Structure

```text
jmeter/
│
├── data/
│   ├── calc_ok.csv
│   └── calc_div0.csv
│
└── lab4-calculator.jmx
```

### Test Data Files

- **calc_ok.csv** — valid JSON requests for calculator operations.  
  Purpose: generate successful requests, verify service stability (expected HTTP 200).

- **calc_div0.csv** — JSON requests with division by zero.  
  Purpose: generate erroneous requests, verify error handling (expected HTTP 422).

---

## Test Plan Components

### 1. HTTP Request Defaults
- Host: `localhost`, Port: `8080`, Protocol: `http`, Implementation: `HttpClient4`

### 2. HTTP Header Manager
- Adds `Content-Type: application/json` header

### 3. HTTP Cookie Manager
- Manages cookies during testing

### 4. Thread Group
- Threads: 1 to 10,000
- Ramp-up: 20 seconds
- Loop Count: 10 iterations per user

### 5. Transaction Controllers

**TX-calc-ok** — tests valid operations  
- CSV Data Set Config (`calc_ok.csv`)  
- `POST /api/calc`  
- Response Assertion (expects 200)

**TX-calc-div0** — tests division by zero  
- CSV Data Set Config (`calc_div0.csv`)  
- `POST /api/calc`  
- Response Assertion (expects 422)

---

## Analysis Tools

- **Summary Report** — sample count, avg/min/max time, error %, throughput
- **Aggregate Report** — median, percentiles (90%, 95%, 99%), standard deviation
- **View Results Tree** — request debugging

---

## Test Results

### 1 User
- Avg response: 2–3 ms  
- No errors (except expected 422)  
- **Conclusion:** Service functions correctly under minimal load.

### 100–500 Users
- Avg response: 1–5 ms  
- Throughput scales linearly, no 5xx errors  
- **Conclusion:** Stable under hundreds of concurrent users.

### 10,000 Users
- Avg response: ~600–700 ms  
- 95th percentile > 2000 ms, max response reaches several seconds  
- Errors appear even on valid requests (error rate ~3–4%)  
- **Conclusion:** Performance degrades significantly — increased latency, higher error rate, server overload.

---

## Determining Load Capacity

- Stable operation up to several thousand concurrent users  
- Beyond threshold: latency spikes, processing errors, SLA violations  
- Load capacity is **below 10,000 concurrent users**

---

## General Conclusions

Apache JMeter effectively models REST service load:

- Under hundreds of users: linear throughput growth, stable response times, no critical errors  
- Under extreme load (10,000 users): latency increases, errors appear, service reaches performance limits  

This work confirms the importance of load testing for determining system scalability and resilience.

---

## Software Used

- Apache JMeter 5.6.3
- Java 17
- Spring Boot calculator application
- PostgreSQL (local)

## 📸 Project Preview

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/1.png" alt="nahhh" width="800"/><br/>
  
</p>

---

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/2.png.gif" alt="nahhh" width="800"/><br/>
  
</p>

---

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/3.png.gif" alt="nahhh" width="800"/><br/>
  
</p>


---

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/4.png.gif" alt="nahhh" width="800"/><br/>
  
</p>


---

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/16.png.gif" alt="nahhh" width="800"/><br/>
  
</p>


---

<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/17.png.gif" alt="nahhh" width="800"/><br/>
  
</p>

---



<p align="center">
  <img src="https://github.com/Figrac0/Automate-Testing/blob/Load_testing/assets/images/19.png.gif" alt="nahhh" width="800"/><br/>
  
</p>

---
