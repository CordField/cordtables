package com.cordtables.v2.models

import com.cordtables.v2.common.Sensitivity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import javax.annotation.Generated

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
    val sensitivity: Sensitivity = Sensitivity.High,
    @ReadOnlyProperty val createdAt: java.sql.Timestamp?,
    val createdByAdminPeopleId: String?,
    @ReadOnlyProperty val modifiedAt: java.sql.Timestamp?,
    val modifiedByAdminPeopleId: String?,
    val owningPersonAdminPeopleId: String?,
    val owningGroupAdminGroupsId: String?,
)