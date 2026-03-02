# Лабораторная работа 5 – Автоматизированное тестирование API в Postman

Цель – познакомиться с возможностью автоматизированного тестирования API в Postman: environments, коллекции запросов, pre-request scripts, тесты, последовательная и параллельная нагрузка, экспорт коллекции в JSON.

## 1. Запуск приложения

Приложение (Spring Boot) запускается локально и поднимает API на порту 8080.

Пример запуска:

- java -jar target/lab2-calculator-service-1.0.0.jar

Базовый адрес для local:

- http://localhost:8080

## 2. Environments

Созданы 2 окружения:

1. local

- protocol = http
- host = localhost
- port = 8080

2. dev

- protocol = http
- host = dev-server
- port = 8080

Переменная baseUrl формируется автоматически в pre-request скрипте коллекции.

## 3. Коллекция Postman

Коллекция:

- lab5-calculator-api-tests

В коллекции реализован pre-request script, который собирает baseUrl из переменных окружения.

Pre-request (collection level):

- protocol берётся из environment
- host берётся из environment
- port берётся из environment
- baseUrl = ${protocol}://${host}:${port}

В запросах вместо захардкоженного URL используется переменная:

- {{baseUrl}}

## 4. Запросы в коллекции

Реализованы 5 основных запросов по заданию:

1. Calc – ADD

- POST {{baseUrl}}/api/calc
- op = +

2. Calc – SUB

- POST {{baseUrl}}/api/calc
- op = -

3. Calc – MUL

- POST {{baseUrl}}/api/calc
- op = \*

4. Calc – DIV

- POST {{baseUrl}}/api/calc
- op = /

5. Calculations – SEARCH

- GET {{baseUrl}}/api/calculations?from=...&to=...
- В контроллере используется формат ISO_DATE_TIME для параметров from/to (OffsetDateTime).

Дополнительно добавлены негативные запросы для проверки ошибок:

- Calc – DIV – div0 (деление на 0)
- Calc – BAD – invalid radix
- Calc – BAD – missing op

## 5. Collection Variables (чтобы не дублировать body)

В Collection Variables заданы значения для формирования тела запроса:

- leftValue
- leftRadix
- rightValue
- rightRadix
- resultRadix

Во всех Calc-запросах тело однотипное, отличается только op.

## 6. Тесты (Post-response)

Для каждого запроса добавлены тесты в Scripts – Post-response (в новых версиях Postman это заменяет вкладку Tests).

Позитивные тесты:

- проверка HTTP статуса (200)
- проверка, что ответ JSON
- проверка структуры ответа (id, resultValue, resultRadix, createdAt)
- проверка корректности результата вычисления (для ADD/SUB/MUL/DIV)
- для SEARCH – проверка, что ответ массив и что createdAt находится в диапазоне [from,to]

Негативные тесты:

- div0 – ожидается 422
- invalid radix – ожидается 400
- missing op – ожидается 400

## 7. Нагрузочное тестирование – последовательный запуск (1000 итераций)

Запуск выполнен в Collection Runner (Functional), Iterations = 1000.

Результат:

- Errors: 0
- Avg response time: 8 ms
- Duration: 14m 5s

Вывод:

- При последовательной нагрузке сервис стабилен
- Ошибок нет
- Среднее время ответа низкое

## 8. Нагрузочное тестирование – параллельный запуск

Запуск выполнен в режиме Performance:

- Load profile: Fixed
- Virtual users: 10
- Test duration: 2 mins
- Environment: local

Результат:

- Total requests sent: 5206
- Requests/second: 40.93
- Avg response time: 11 ms
- P90: 43 ms
- P95: 54 ms
- P99: 74 ms
- Error rate: 37.40%

Пояснение по error rate:

- В наборе запросов присутствуют негативные тесты, которые намеренно возвращают 4xx (div0, invalid radix, missing op)
- В Performance-режиме такие ответы учитываются как ошибки на графиках, поэтому общий Error rate высокий и является ожидаемым при смешанном наборе позитивных и негативных сценариев

Вывод:

- При параллельной нагрузке latency ожидаемо вырос (8 ms – 11 ms avg)
- Появился высокий error rate из-за намеренных негативных запросов (ожидаемое поведение)
- Сервис остаётся работоспособным под concurrency=10, критических 5xx не зафиксировано (при корректной интерпретации, что 4xx для negative-сценариев нормальны)

## 9. Артефакты в репозитории

Экспортирована коллекция Postman:

- postman/lab5-calculator-api-tests.postman_collection.json

Опционально экспортированы environments:

- postman/local.postman_environment.json
- postman/dev.postman_environment.json
