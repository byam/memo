// Example of Parsing Json to Java Object
// source: http://tutorials.jenkov.com/java-json/index.html

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;

public class ExampleJsonParser {

    public static void exampleJacksonObjectMapper (){

        ObjectMapper objectMapper = new ObjectMapper();

        String carJson =
                "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
                        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
                        "  \"nestedObject\" : { \"field\" : \"value\" } }";
        try {

            JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);

            System.out.println(
                    "\n brand: " + jsonNode.get("brand") +
                    "\n doors: " + jsonNode.get("doors") +
                    "\n owners: " + jsonNode.get("owners") +
                    "\n owners -> first element: " + jsonNode.get("owners").get(0) +
                    "\n nestedObject: " + jsonNode.get("nestedObject") +
                    "\n nestedObject -> field: " + jsonNode.get("nestedObject").get("field") +
                    ""
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exampleJacksonJsonParser() throws IOException {
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

        JsonFactory factory = new JsonFactory();
        JsonParser parser  = factory.createJsonParser(carJson);

        while(!parser.isClosed()){

            String fieldName = parser.getCurrentName();
            Class<? extends JsonParser> parserClass = parser.getClass();

            JsonToken jsonToken = parser.nextToken();

            System.out.println(
                    "\t[jsonToken] = " + jsonToken +
                    "\t[field]: " + fieldName +
                    "\t[text]: " + parser.getText()
            );

        }
    }

    public static void exampleGsonParser(){

        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        Car car = gson.fromJson(carJson, Car.class);

        System.out.println(car.toString());
    }

    public static class Car {

        @Expose(serialize = false, deserialize = false)
        public String brand = null;

        @Expose(serialize = true, deserialize = true)
        public int doors = 0;

        @Override
        public String toString(){
            return "brand: " + this.brand + "\tdoors: " + this.doors;
        }
    }

    public static void exampleGsonNullSerialize(){

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        Car car = new Car();
        car.brand = null;

        String json = gson.toJson(car);
        System.out.println(json);
    }

    public static class CarCreator implements InstanceCreator<Car>{
        public Car createInstance(Type type){
            Car car = new Car();
            car.brand = "Honda";
            return car;
        }
    }

    public static void exampleGsonCustomInstance(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Car.class, new CarCreator());
        Gson gson = gsonBuilder.create();

        String carJson = "{ \"doors\" : 4 }";
        Car car = gson.fromJson(carJson, Car.class);

        System.out.println(car.toString());
    }

    public static class BooleanDeserializer implements JsonDeserializer<Boolean>{

        @Override
        public Boolean deserialize(JsonElement jsonElement,
                                   Type type,
                                   JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return jsonElement.getAsInt() != 0;
        }
    }

    public class PojoWithBoolean {
        public String username = null;
        public Boolean isSuperUser = false;
    }

    public static void exampleGsonCustomDeserializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanDeserializer());
        Gson gson = gsonBuilder.create();

        String jsonSource = "{\"username\":\"abc\",\"isSuperUser\":1}";

        PojoWithBoolean pojo = gson.fromJson(jsonSource, PojoWithBoolean.class);

        System.out.println(pojo.isSuperUser);
    }

    public static void main (String[] args) {
        exampleGsonCustomDeserializer();
    }
}
