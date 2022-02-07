package com.cordtables.v2.repositories

import com.cordtables.v2.models.AdminPeople
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PostMapping

@RepositoryRestResource(collectionResourceRel = "admin_people", path = "people")
interface AdminPeopleRepository : PagingAndSortingRepository<AdminPeople, String> {

    fun findByPrivateFirstName(@Param("privateFirstName") name: String): List<AdminPeople>

    @Modifying
    @Query("insert into admin_people(private_first_name) values (:privateFirstName)")
    fun createFromPrivateFirstName(@Param("privateFirstName") privateFirstName: String)
}
