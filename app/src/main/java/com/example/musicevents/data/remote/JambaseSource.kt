package com.example.musicevents.data.remote

import android.annotation.SuppressLint
import com.example.musicevents.utils.Coordinates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date

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
data class Geo(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double
)

@Serializable
data class Location(
    @SerialName("name")
    val name: String,
    @SerialName("address")
    val city: City,
    @SerialName("geo")
    val geo: Geo
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
    @SerialName("startDate")
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
data class Pagination(
    @SerialName("page")
    val page: Int,
    @SerialName("nextPage")
    val nextPage: String?,
    @SerialName("previousPage")
    val previousPage: String?
)

@Serializable
data class JamBaseResponse(
    @SerialName("events")
    var events: List<EventApi>,
    @SerialName("pagination")
    val pagination: Pagination
)

@Serializable
data class Genre(
    @SerialName("name")
    val name: String,
    @SerialName("identifier")
    val identifier: String
)

@Serializable
data class GenreResponse(
    @SerialName("genres")
    val genres: List<Genre>
)

@Serializable
data class SingleEvent(
    @SerialName("event")
    val event: EventApi
)


class JambaseSource(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://www.jambase.com/jb-api/v1/"
    private val apiKey = "e066c7bf-35dc-426e-a839-250784bb72ac"
    @SuppressLint("SimpleDateFormat")
    private val date = SimpleDateFormat("yyyy-MM-dd").format(Date())


    suspend fun searchEvents(artistName: String, genre: String): JamBaseResponse{
        val url = "${baseUrl}events?eventDateFrom=${date}&perPage=10&genreSlug=$genre&artistName=${artistName}&apikey=${apiKey}"
        return httpClient.get(url).body()
    }

    suspend fun searchFromCoordinates(coordinates: Coordinates, genre: String, artistName: String): JamBaseResponse{
        val url = "${baseUrl}events?geoRadiusAmount=200&geoRadiusUnits=km&eventDateFrom=${date}&perPage=10&artistName=$artistName&genreSlug=$genre&geoLatitude=${coordinates.latitude}&geoLongitude=${coordinates.longitude}" +
                "&geoRadiusAmount=100&geoRadiusUnits=km&apikey=${apiKey}"
        return httpClient.get(url).body()
    }

    suspend fun getAllGenres(): GenreResponse{
        val url = "${baseUrl}genres?apikey=${apiKey}"
        return httpClient.get(url).body()
    }

    suspend fun getEvent(eventId: String): SingleEvent{
        val url = "${baseUrl}events/id/${eventId}?apikey=${apiKey}"
        return httpClient.get(url).body()
    }

    suspend fun searchPage(link: String): JamBaseResponse {
        return httpClient.get(link).body()
    }
}