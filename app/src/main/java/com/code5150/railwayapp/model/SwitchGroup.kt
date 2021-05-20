package com.code5150.railwayapp.model

import com.code5150.railwayapp.network.dto.SwitchGroupDTO

data class SwitchGroup(val id: Int, val name: String) {
    constructor(group: SwitchGroupDTO): this(group.id, group.name)

    override fun toString() = name
}
