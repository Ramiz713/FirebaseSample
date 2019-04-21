package com.itis2019.firebasesample.model

import java.util.*

class Task
{
        lateinit var  name:String
        lateinit var date: Date
        constructor(
                 name: String,
                 date: Date) {
                this.name = name
                this.date = date
        }
        constructor() {}
}
