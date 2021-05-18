package com.code5150.railwayapp.model

import com.code5150.railwayapp.network.dto.SwitchDTO

data class Switch(val id: Int, val name: String, val switchGroupId: Int) {
    constructor(s: SwitchDTO): this(s.id, s.name, s.switchGroupId)
    override fun toString() = name
}