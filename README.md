# Ktor Server

REST-сервер на Kotlin/Ktor с маршрутами GET, POST, DELETE.

## 🚀 Запуск

```bash
./gradlew run
```

Сервер поднимется на `http://localhost:8080`.

## 📋 Маршруты

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/` | Проверка живости сервера |
| GET | `/users` | Список всех пользователей |
| GET | `/users?name=Al` | Фильтрация по имени (query-параметр) |
| GET | `/users/{id}` | Получить пользователя по ID (path-параметр) |
| POST | `/users` | Создать пользователя |
| DELETE | `/users/{id}` | Удалить пользователя |

## 📥 Пример POST-запроса

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Vova", "email": "vova@example.com"}'
```

Ответ (`201 Created`):
```json
{
  "success": true,
  "message": "Пользователь успешно создан",
  "data": { "id": 3, "name": "Vova", "email": "vova@example.com" }
}
```

## ✅ HTTP-коды

- `200 OK` — успешные GET/DELETE
- `201 Created` — успешный POST
- `400 Bad Request` — ошибки валидации, некорректные параметры
- `404 Not Found` — ресурс не найден
