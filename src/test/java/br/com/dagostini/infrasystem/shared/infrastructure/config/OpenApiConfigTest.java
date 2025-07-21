package br.com.dagostini.infrasystem.shared.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @Mock
    private MappingJackson2HttpMessageConverter converter;

    private List<MediaType> initialMediaTypes;

    @BeforeEach
    void setUp() {
        initialMediaTypes = Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
        when(converter.getSupportedMediaTypes()).thenReturn(initialMediaTypes);
    }

    @Test
    void constructor_shouldAddOctetStreamMediaType() {
        List<MediaType> expectedMediaTypes = new ArrayList<>(initialMediaTypes);
        expectedMediaTypes.add(new MediaType("application", "octet-stream"));

        OpenApiConfig openApiConfig = new OpenApiConfig(converter);

        verify(converter, times(1)).getSupportedMediaTypes();
        verify(converter, times(1)).setSupportedMediaTypes(expectedMediaTypes);
        verifyNoMoreInteractions(converter);
    }
}