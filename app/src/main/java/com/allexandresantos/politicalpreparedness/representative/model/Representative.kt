package com.allexandresantos.politicalpreparedness.representative.model

import com.allexandresantos.politicalpreparedness.data.models.Office
import com.allexandresantos.politicalpreparedness.data.models.Official

data class Representative (
        val official: Official,
        val office: Office
)