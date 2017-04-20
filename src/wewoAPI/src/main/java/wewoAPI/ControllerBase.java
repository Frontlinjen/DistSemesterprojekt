package wewoAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ControllerBase {
	protected class LambdaResponse{
		private class LambdaResponseData{
			@JsonProperty("isBase64Encoded")
			public boolean base64 = false;
			public int statusCode;
			public Map<String, String> headers = new HashMap<String, String>();
			public String body;
		}
		private LambdaResponseData data;
		private JsonGenerator bodyWriter;
		private StringWriter bodyStream;
		
		
		public LambdaResponse(JsonFactory factory){
			data = new LambdaResponseData();
			try {
				bodyStream = new StringWriter();
				bodyWriter = factory.createGenerator(bodyStream);
				bodyWriter.writeStartObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void addHeader(String header, String value){
			data.headers.put(header, value);
		}
		
		public void setStatusCode(int statusCode){
			data.statusCode = statusCode;
		}

		public void addResponseObject (String ID, Object object) {
			try {
				bodyWriter.writeObjectField(ID, object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void dispatch(JsonGenerator out){
			try {
				data.body = bodyStream.toString();
				out.writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
				
	}
	protected class LambdaRequest{
		@JsonIgnoreProperties(ignoreUnknown=true)
		
		private class LambdaRequestData{
			public Map<String, String> headers = new HashMap<String, String>();
			@JsonProperty("queryStringParameters")
			public Map<String, String> queryString = new HashMap<String, String>();
			public Map<String, String> pathParameters = new HashMap<String, String>();			
			public ObjectNode body;
		}
		
		private ObjectCodec objectConverter;
		private LambdaRequestData data;
		public LambdaRequest(JsonParser parser, ObjectCodec c)
		{
			try {
				data = parser.readValueAs(LambdaRequestData.class);
				objectConverter = c;
			} catch (IOException e) {
				//TODO: Handle gracefully
				e.printStackTrace();
			}
		}
		
		public String getHeader(String name){
			return data.headers.get(name);
		}
		
		public String getQuery(String id){
			return data.queryString.get(id);
		}
		
		public String getPath(String id){
			return data.pathParameters.get(id);
		}
		
		public <T> T getObject(String name, Class<T> type){
			if(data.body != null && data.body.has(name)){
				try {
					TreeNode node = data.body.get(name);
					return objectConverter.treeToValue(node, type);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
	}
	
	private JsonFactory factory = new JsonFactory(new ObjectMapper());

	protected LambdaResponse request;
	protected LambdaRequest response;
	
	public void Something(InputStream i, OutputStream out)
	{
		request = new LambdaResponse(factory);
		try {
			response = new LambdaRequest(factory.createParser(i), new ObjectMapper());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
