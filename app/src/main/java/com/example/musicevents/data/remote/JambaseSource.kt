package com.example.musicevents.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("name")
    val name: String
)

@Serializable
data class City(
    @SerialName("addressLocality")
    val name: String,
    @SerialName("addressCountry")
    val county: Country
)

@Serializable
data class Location(
    @SerialName("name")
    val name: String,
    @SerialName("address")
    val city: City
)

@Serializable
data class Performer(
    @SerialName("name")
    val name: String
)

@Serializable
data class EventApi(
    @SerialName("name")
    val name: String,
    @SerialName("endDate")
    val date: String,
    @SerialName("image")
    val imageUrl: String,
    @SerialName("location")
    val location: Location,
    @SerialName("performer")
    val performer: List<Performer>,
    @SerialName("identifier")
    val id: String
)

@Serializable
data class JamBaseResponse(
    @SerialName("events")
    val events: List<EventApi>
)


class JambaseSource(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://www.jambase.com/jb-api/v1/"
    private val apiKey = "e066c7bf-35dc-426e-a839-250784bb72ac"

    suspend fun searchEvents(artistName: String): JamBaseResponse{
        val url = "${baseUrl}events?perPage=10&artistName=${artistName}&apikey=${apiKey}"
        return httpClient.get(url).body()
    }
}