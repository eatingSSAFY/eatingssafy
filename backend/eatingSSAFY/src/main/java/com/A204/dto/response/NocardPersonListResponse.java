package com.A204.dto.response;

import java.util.List;

public record NocardPersonListResponse(
        List<NocardPersonResponse> nocardPersonList
) {
}
