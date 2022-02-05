package com.cordtables.v2.repositories

import com.cordtables.v2.models.AdminPeople
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminPeopleRepository : CrudRepository<AdminPeople, String>
