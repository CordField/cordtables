package com.cordtables.v2.models

import com.cordtables.v2.common.Sensitivity
import org.springframework.data.annotation.Id

data class AdminPeople(
    val about: String?,
    val pictureCommonFilesId: String?,
    val privateFirstName: String?,
    val privateLastName: String?,
    val publicFirstName: String?,
    val publicLastName: String?,
    val primaryLocationCommonLocationsId: String?,
    val sensitivityClearance: Sensitivity = Sensitivity.Low,
    val timezone: String?,

    @Id val id: String?,
)