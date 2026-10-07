package com.example.mobile_cue_things.enums

enum class ConstantsValidateInputs(val regex: Regex) {


    // Regex("[a-zA-Z0-9\\-_#* ]*")

    ALLOWCHARREGEXNAMEPRODUCT(Regex("[a-zA-Z0-9\\-_#* ]*")),

    ALLOWONLYPRINCIPALCHARACTERS(Regex("[a-zA-Z0-9]*"))
}