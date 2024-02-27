package com.A204.dto.response;

import com.A204.domain.NocardPerson;

import java.util.List;

public record NocardPersonResponse(
        String personName,
        String personId
) {
}
