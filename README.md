# Работа 1: Разработка защищенного REST API с интеграцией в CI/CD

## Эндпоинты

### Методы для аутентификации

#### Регистрация пользователя

```http
POST /auth/signup
Content-Type: application/json

{
  "email" : "test2@gmail.com",
  "password" : "123456",
  "name" : "test2"
}
```
##### Ответ:

"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0M0BnbWFpbC5jb20iLCJpYXQiOjE3NjM1MTQwMDIsImV4cCI6MTc2MzUxNzYwMn0.sunazfqT51ArMMt87oyXqKqsY-MBSl3KcHk1iC7o_ac"

#### Аутентификация пользователя
```http
POST auth/signin
Content-Type: application/json

{
  "email" : "test2@gmail.com",
  "password" : "123456"
}
````
##### Ответ:

"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0M0BnbWFpbC5jb20iLCJpYXQiOjE3NjM1MTQwNjAsImV4cCI6MTc2MzUxNzY2MH0.2EXoSB8Fgc0tYPe19TZukQB0esCMfzU2B6txQmf8DYg"
### Методы доступные только авторизованным пользователям

#### Получение защищенных данных

```http
GET /api/data
Content-Type: application/json
Authorization: Bearer {jwt-token}


```
##### Ответ:
```http
[
    {
        "id": "1",
        "text": "Sample data 1 &lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt;"
    }
]
```
#### Создание новых данных с автоматической санитизацией

```http
POST /api/data
Content-Type: application/json
Authorization: Bearer {jwt-token}

Sample data with <script>alert('xss')</script> for testing
````
#### Ответ:

```http
Incoming content processed: Sample data with &lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt; for testing
```

#### Получение информации о пользователе

```http
GET /api/user-info
Content-Type: application/json
Authorization: Bearer {jwt-token}

```
##### Ответ:
```http
{
"status": "authenticated",
"message": "This is protected user info"
}
```

## Меры защиты


### 1. Защита от SQL-инъекций
- Все запросы к базе данных через **Spring Data JPA / Hibernate**.
- Параметризованные выражения (Prepared Statements) используются автоматически.

```Java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### 2. Защита от XSS

- **HtmlUtils.htmlEscape()** Автоматическое экранирование HTML через HtmlUtils.htmlEscape()
```Java
@GetMapping("/data")
public ResponseEntity<List<DataItem>> fetchItems() {

    String escaped = HtmlUtils.htmlEscape(
            "Sample data 1 <script>alert('xss')</script>"
    );

    List<DataItem> payload = List.of(
            new DataItem("1", escaped)
    );

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(payload);
}
```

### 3. Аутентификация по JWT

- **JWT токены**: HS256 с 256-битным секретным ключом для безопасной передачи данных аутентификации
- **Stateless подход**: не требуется хранение сессий на сервере, уменьшая нагрузку и упрощая масштабирование
- **Валидация**: Проверка подписи и срока действия токенов при каждом запросе к защищенным эндпоинтам
- **Middleware**: JwtAuthenticationFilter проверяет наличие и валидность токена в заголовке Authorization

### 4. Статический анализ и проверка зависимостей
- SpotBugs для выявления потенциальных уязвимостей / багов коде
- Snyk для анализа зависимостей на известные CVE

## Скриншоты отчетов

### SAST

![img.png](pictures/SAST.png)

### SCA

![img.png](pictures/SCA.png)