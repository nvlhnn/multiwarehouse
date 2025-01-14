package com.nvlhnn.order.service.domain.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@JsonDeserialize(builder = OrderPaidCommand.OrderPaidCommandBuilder.class)
public class OrderPaidCommand {

    @NotNull
    private final String externalId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderPaidCommandBuilder {
        @JsonProperty("external_id")
        private String externalId;

        public OrderPaidCommandBuilder externalId(String externalId) {
            this.externalId = externalId;
            return this;
        }
    }
}

