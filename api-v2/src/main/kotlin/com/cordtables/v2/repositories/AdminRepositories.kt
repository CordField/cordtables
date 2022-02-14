package com.cordtables.v2.repositories

import com.cordtables.v2.models.AdminPeople
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Component

@RepositoryRestResource(path = "adminPeople")
interface AdminPeopleRepository : PagingAndSortingRepository<AdminPeople, String>
