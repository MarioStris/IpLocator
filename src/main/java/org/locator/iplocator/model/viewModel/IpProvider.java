package org.locator.iplocator.model.viewModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "IP location provider information")
public class IpProvider {

    private String name;
    private String shortCode;
}
