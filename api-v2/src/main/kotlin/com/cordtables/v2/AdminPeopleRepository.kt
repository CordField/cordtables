package com.cordtables.v2

import com.cordtables.v2.common.Sensitivity
import com.fasterxml.jackson.annotation.JsonSubTypes
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.annotation.Generated

data class AdminPeople(
    val about: String?,
    val pictureFilesId: String?,
    val privateFirstName: String?,
    val privateLastName: String?,
    val publicFirstName: String?,
    val publicLastName: String?,
    val primaryLocationLocationsId: String?,
    val sensitivityClearance: Sensitivity = Sensitivity.Low,
    val timezone: String?,

    @Id val id: String?,
)

@Repository
interface AdminPeopleRepository : CrudRepository<AdminPeople, String>