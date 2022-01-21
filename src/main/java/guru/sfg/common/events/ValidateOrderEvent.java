package guru.sfg.common.events;

import guru.sfg.common.models.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderEvent {
    private BeerOrderDto beerOrderDto;
}
