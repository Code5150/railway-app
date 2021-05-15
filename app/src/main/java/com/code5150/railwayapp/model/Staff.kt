package com.code5150.railwayapp.model

import com.code5150.railwayapp.network.dto.StaffDTO

data class Staff(val name: String, val surname: String, val patronymic: String, val position: String){
    constructor(staff: StaffDTO) : this(staff.name, staff.surname, staff.patronymic, staff.position)

    override fun toString(): String = "$surname ${name[0]}.${if (patronymic.isEmpty()) "" else patronymic[0] + "."}"
}
