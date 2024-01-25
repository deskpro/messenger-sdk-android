package com.deskpro.messenger.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data class representing user information.
 *
 * This data class is annotated with [@Serializable](https://kotlinlang.org/docs/serialization.html)
 * and is designed to hold information about a user, such as their name, first name, last name, and email.
 *
 * @param name The full name of the user.
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @param email The email address of the user.
 */
@Serializable
data class User(
    @SerialName("name") var name: String? = null,
    @SerialName("first_name") var firstName: String? = null,
    @SerialName("last_name") var lastName: String? = null,
    @SerialName("email") var email: String? = null
)
