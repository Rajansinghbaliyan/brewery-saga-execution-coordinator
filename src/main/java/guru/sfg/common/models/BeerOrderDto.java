package guru.sfg.common.models;

import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BeerOrderDto extends BaseItem {
    @Builder
    public BeerOrderDto(UUID id, Integer version, OffsetDateTime createdAt, OffsetDateTime lastModifiedAt,
                        UUID customerId, String customerRef, String orderStatusCallbackUrl, BeerOrderState status,
                        List<BeerOrderLineDto> beerOrderLines) {
        super(id, version, createdAt, lastModifiedAt);
        this.customerId = customerId;
        this.customerRef = customerRef;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
        this.status = status;
        this.beerOrderLines = beerOrderLines;
    }

    private UUID customerId;
    private String customerRef;
    private String orderStatusCallbackUrl;
    private BeerOrderState status;
    private List<BeerOrderLineDto> beerOrderLines;
}