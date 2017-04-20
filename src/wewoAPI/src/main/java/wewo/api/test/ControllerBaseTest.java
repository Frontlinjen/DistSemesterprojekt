package wewo.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import wewoAPI.ControllerBase;

public class ControllerBaseTest{
	private ByteArrayOutputStream out;
	private JsonFactory factory;
	private ObjectMapper mapper;
	
	
	@Before
	public void createStreams(){
	out = new ByteArrayOutputStream();	
	mapper = new ObjectMapper();
	factory = new JsonFactory(mapper);
	}
	
	@Test
	public void canDeserializeRequestData() throws JsonParseException, JsonProcessingException, IOException {
		ControllerBase.LambdaRequest.LambdaRequestData data = new ControllerBase.LambdaRequest.LambdaRequestData();
		data.pathParameters.put("TestPath", "PathValue");
		data.headers.put("TestHeader", "HeaderValue");
		data.queryString.put("TestQuery", "QueryValue");
		data.body = mapper.valueToTree(data);
		//mapper.writeValue
		//System.out.println(mapper.writeValueAsString(data));
		JsonParser parser = factory.createParser(mapper.writeValueAsBytes(data));
		ControllerBase.LambdaRequest requestParser = new ControllerBase.LambdaRequest(parser, mapper);
		assertTrue(requestParser.getHeader("TestHeader").equals("HeaderValue"));
		assertTrue(requestParser.getQuery("TestQuery").equals("QueryValue"));
		assertTrue(requestParser.getPath("TestPath").equals("PathValue"));
		
		ControllerBase.LambdaRequest.LambdaRequestData bodyData = requestParser.getObject(ControllerBase.LambdaRequest.LambdaRequestData.class);
		assertNotNull(bodyData);
		assertTrue(bodyData.headers.get("TestHeader").equals("HeaderValue"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> headers = requestParser.getObject("headers", HashMap.class);
		assertNotNull(headers);
		assertTrue(headers.get("TestHeader").equals("HeaderValue"));
 	}
	
	@Test
	public void canDeserializeEmptyBody() throws JsonParseException, JsonProcessingException, IOException {
		ControllerBase.LambdaRequest.LambdaRequestData data = new ControllerBase.LambdaRequest.LambdaRequestData();
		data.pathParameters.put("TestPath", "PathValue");
		data.headers.put("TestHeader", "HeaderValue");
		data.queryString.put("TestQuery", "QueryValue");
		//mapper.writeValue
		//System.out.println(mapper.writeValueAsString(data));
		JsonParser parser = factory.createParser(mapper.writeValueAsBytes(data));
		ControllerBase.LambdaRequest requestParser = new ControllerBase.LambdaRequest(parser, mapper);
		assertTrue(requestParser.getHeader("TestHeader").equals("HeaderValue"));
		assertTrue(requestParser.getQuery("TestQuery").equals("QueryValue"));
		assertTrue(requestParser.getPath("TestPath").equals("PathValue"));
		
		ControllerBase.LambdaRequest.LambdaRequestData bodyData = requestParser.getObject(ControllerBase.LambdaRequest.LambdaRequestData.class);
		assertNull(bodyData);
	}
	
	@Test
	public void canSerializeResponseData() throws IOException{
		ControllerBase.LambdaResponse response = new ControllerBase.LambdaResponse(factory);
		response.setStatusCode(9000);
		response.addHeader("Auth", "AuthValue");
		response.addHeader("User", "Nobody");
		response.addResponseObject("Object1", new Integer(22));
		response.addResponseObject("String1", new String("StringValue"));
		JsonGenerator gen = factory.createGenerator(out);
		response.dispatch(gen);
		
		JsonNode rootNode = mapper.readTree(new String(out.toByteArray()));
		JsonNode status = rootNode.get("statusCode");
		assertNotNull(status);
		assertTrue(status.asInt()==9000);
		
		JsonNode headers = rootNode.get("headers");		
		assertNotNull(headers);
		assertTrue(headers.get("Auth").asText().equals("AuthValue"));
		assertTrue(headers.get("User").asText().equals("Nobody"));
		
		System.out.println(new String(out.toByteArray()));
		
		JsonNode body = mapper.readTree(rootNode.get("body").asText());
		assertNotNull(body);
		assertTrue(body.get("Object1").asInt()==22);
		assertTrue(body.get("String1").asText().equals("StringValue"));
		
		
	}

}
