package guru.sfg.common.events;

import io.cherrytechnologies.msscsagaexecutioncoordinator.domain.BeerOrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStateEvent {
    private UUID beerOrderId;
    private BeerOrderState beerOrderState;
}
