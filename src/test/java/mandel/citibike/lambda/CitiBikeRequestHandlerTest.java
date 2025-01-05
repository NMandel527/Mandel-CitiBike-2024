package mandel.citibike.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CitiBikeRequestHandlerTest {
    @Test
    void handleRequest() {
        try {
            // given
            String json = Files.readString(Path.of("request.json"));

            APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
            Context context = mock(Context.class);
            when(event.getBody()).thenReturn(json);

            // when
            CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
            CitiBikeResponse citiBikeResponse = handler.handleRequest(event, context);

            // then
            assertEquals("Lenox Ave & W 146 St", citiBikeResponse.start.stationName);
            assertEquals("79 St & Roosevelt Ave", citiBikeResponse.end.stationName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
