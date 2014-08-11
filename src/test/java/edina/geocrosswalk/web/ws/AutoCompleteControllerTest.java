package edina.geocrosswalk.web.ws;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edina.geocrosswalk.service.IFormatProviderService;
import edina.geocrosswalk.service.ISpatialService;
import edina.geocrosswalk.service.plugins.GXWFormat;

public class AutoCompleteControllerTest {
    private NameIndexSearchController controller;
    private ISpatialService mockSpatialService;
    private IFormatProviderService mockFormatService;
    private NameStartsWithSearchCommand command;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private String name = "edin";
    private String country = "GB";
    private String output = "This is the output";
    private List<String> names;
    private ModelAndView mav = new ModelAndView();
    private BindException errors;
    private String contentType = "text/xml;charset=utf-8";
    
    @Before
    public void onSetUp() {
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        controller = new NameIndexSearchController();
        command = new NameStartsWithSearchCommand();
        command.setFormat(GXWFormat.GXW);
        command.setName("edin"); 
        command.setCountry(country);
        names = new ArrayList<String>();
    }
    
    @Test
    public void testHandle() throws Exception {
        mockSpatialService = createMock(ISpatialService.class);
        mockFormatService = createMock(IFormatProviderService.class);
        controller.setSpatialService(mockSpatialService);
        controller.setFormatProviderService(mockFormatService);
        List<String> autoCompleteNames = mockSpatialService.getAutoCompleteNames(this.name, country);
        expect(autoCompleteNames).andReturn(names).once();
        expect(mockFormatService.getOutputForAutoComplete(names, GXWFormat.GXW, null)).andReturn(output).once();
        expect(mockFormatService.getContentTypeForFormat(GXWFormat.GXW, null)).andReturn(contentType).once();
        replay(mockSpatialService);
        replay(mockFormatService);
        mav = controller.handle(mockRequest, mockResponse, command, errors);
        assertNotNull(mav.getModelMap().get("format"));
        assertNotNull(mav.getModelMap().get("resultDocument"));
        verify(mockSpatialService);
        verify(mockFormatService);
    }
}
