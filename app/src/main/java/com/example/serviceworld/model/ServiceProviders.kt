package com.example.serviceworld.model

//class ServiceProviders{
//    var name: String? = null
//    var serviceName: String? = null
//    var location: String? = null
//    var isAvailable: String? = null
//
//    constructor(){}
//
//    constructor(name: String?, serviceName: String?, location: String?, isAvailable: String?) {
//        this.name = name
//        this.serviceName = serviceName
//        this.location = location
//        this.isAvailable = isAvailable
//    }
//}

data class ServiceProviders(
    var name: String? = null,
    var serviceName: String? = null,
    var location: String? = null,
    var isAvailable: String? = "Available",
    var phone: String? = null,
    var email: String? = null
)