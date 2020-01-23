package by.integrated.user.domain

import org.springframework.data.annotation.Id

data class User(
    @Id
    val id: Long?,
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)