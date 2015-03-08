package com.report.rest;
 
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
@Path("/")
public class Service {
    @GET
    @Path("/getReportById/{id}")
    public Response getById(@PathParam("id") String id) throws FileNotFoundException, IOException, ParseException {
    	File file = new File("/users/maayankeenan/temp/" + id + ".txt");
    	JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		try{
	    	if (file.exists()) {
	    		Object obj = parser.parse(new FileReader(file));
	            jsonArray = (JSONArray) obj;
	    	}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
    	
        System.out.println("Data Received: " + id);
 
        // return HTTP response 200 in case of success
        return Response.status(200).entity(jsonArray.toJSONString()).build();
    }
    
    @SuppressWarnings("unchecked")
	@GET
    @Path("/saveReportById/{id}/{results}")
    public Response save(@PathParam("id") String id, @PathParam("results") String results) throws IOException  {
		File file = new File("/users/maayankeenan/temp/" + id + ".txt");
		FileWriter fileWriter = null;
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		JSONObject prevResult = new JSONObject();
		
		try{
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			} else {
				try {
					// file exist, read current data
					Object obj = parser.parse(new FileReader(file));
		            jsonArray = (JSONArray) obj;
		            prevResult = (JSONObject) jsonArray.get(jsonArray.size() - 1);
		            
				} catch(ParseException e){
					// do nothing use empty array
				}
			}
			
			// parse new data and add it to existing
			JSONObject jsonObj =  (JSONObject)parser.parse(results);
			jsonArray.add(jsonObj);
			
			// save to file
			fileWriter = new FileWriter(file);
			fileWriter.write(jsonArray.toJSONString());
			
			System.out.println("Done");
			System.out.println(jsonArray.toString());
			
		}catch(Exception e){
			System.out.println(e.toString());
			return Response.status(400).entity("failed").build();
		} finally {
			fileWriter.close();
		}
		
		return Response.status(200).entity(prevResult.toJSONString()).build();
        
    }
}