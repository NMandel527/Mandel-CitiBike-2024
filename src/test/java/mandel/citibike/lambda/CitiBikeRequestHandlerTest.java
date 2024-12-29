package mandel.citibike.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import mandel.citibike.lambda.CitiBikeRequestHandler.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CitiBikeRequestHandlerTest {
    @Test
    void handleRequest() throws IOException {
        //given
        String json = Files.readString(Path.of("request.json"));

        Context context = mock(Context.class);
        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
        when(event.getBody()).thenReturn(json);
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();

        //when
        CitiBikeResponse citibikeResponse = handler.handleRequest(event, context);

        //then
        assertEquals("Lenox Ave & W 146 St", citibikeResponse.start.stationName);
        assertEquals("79 St & Roosevelt Ave", citibikeResponse.end.stationName);
    }
}
