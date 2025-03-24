package com.spring.spring.dto.invoiceline;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.spring.utils.CustomZonedDateTimeDeserializer;
import com.spring.spring.utils.CustomZonedDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineDTO {
    private Long id;

    @JsonProperty("external_id")
    private String externalId;

    private String title;
    private String comment;
    private Integer price;

    @JsonProperty("invoice_id")
    private Long invoiceId;

    @JsonProperty("offer_id")
    private Long offerId;

    private String type;
    private Integer quantity;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("created_at")
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime updatedAt;

    @JsonProperty("deleted_at")
    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime deletedAt;
}
