package com.example

import com.example.model.*
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        //GET

        get("/") {
            call.respondText("Ktor Server is running!")
        }

        // GET получение списка пользователей с фильтрацией по имени
        get("/users") {
            val nameFilter = call.request.queryParameters["name"]

            val users = if (nameFilter != null) {
                UserRepository.getAllUsers().filter {
                    it.name.contains(nameFilter, ignoreCase = true)
                }
            } else {
                UserRepository.getAllUsers()
            }

            call.respond(
                HttpStatusCode.OK,
                ApiResponse(
                    success = true,
                    message = "Найдено пользователей: ${users.size}",
                    data = users
                )
            )
        }

        // GET получение пользователя по ID
        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(
                        success = false,
                        message = "Некорректный ID: должен быть целым числом"
                    )
                )
                return@get
            }

            val user = UserRepository.getUserById(id)

            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(
                        success = false,
                        message = "Пользователь с ID $id не найден"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Пользователь найден",
                        data = user
                    )
                )
            }
        }

        //POST
        post("/users") {
            try {
                val request = call.receive<CreateUserRequest>()

                if (request.name.isBlank() || request.email.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(
                            success = false,
                            message = "Имя и email не могут быть пустыми"
                        )
                    )
                    return@post
                }

                val newUser = UserRepository.addUser(request.name, request.email)

                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        success = true,
                        message = "Пользователь успешно создан",
                        data = newUser
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(
                        success = false,
                        message = "Ошибка при создании пользователя: ${e.message}"
                    )
                )
            }
        }

        //DELETE
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(
                        success = false,
                        message = "Некорректный ID: должен быть целым числом"
                    )
                )
                return@delete
            }

            val deleted = UserRepository.deleteUser(id)

            if (deleted) {
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse<Unit>(
                        success = true,
                        message = "Пользователь с ID $id успешно удалён"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(
                        success = false,
                        message = "Пользователь с ID $id не найден"
                    )
                )
            }
        }
    }
}
