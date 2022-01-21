package guru.sfg.common.events;

import guru.sfg.common.models.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrewMoreBeerEvent {
    private BeerOrderDto beerOrderDto;
    private List<UUID> beersWithLessInventory;
}
