package sample1;

import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;

/**
 * Api Tester
 */
public class ApiTester {
    private String baseUrl;
    private HttpRequestFactory reqFactory;
    private JsonObject result = new JsonObject();
   
    private Pattern pattern = Pattern.compile("\\$[{]([^}]+)[}]");
    private Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).build();               

    /**
     * Initialize instance.
     * 
     * @param baseUrl api base url
     */
    public ApiTester(String baseUrl) {
        this.baseUrl = baseUrl;

        try {
            NetHttpTransport.Builder builder = new NetHttpTransport.Builder().doNotValidateCertificate();
            String env_proxy = System.getenv("HTTP_PROXY");
            if (StringUtils.isNotBlank(env_proxy)) {
                URL url = new URL(env_proxy);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(url.getHost(), url.getPort()));
                builder.setProxy(proxy); 
            }
            reqFactory = builder.build().createRequestFactory();
        } catch (Exception ex) {
            System.out.println("error: " + ex);
        }
     }

    /**
     * Add JsonElement to result.
     * 
     * @param resultName result name
     * @param element JsonElement
     */
    public void addResult(String resultName, JsonElement element) {
        this.result.add(resultName, element);
    }

    /**
     * Call api and verify result.
     * 
     * @param testData input and expected data
     * @throws Exception
     */
    public void TestApi(TestData testData) throws Exception {
        if (testData.getNo() == 1) {
            result = new JsonObject();
        } 

        if (testData.isSkip()) {
            System.out.println("skip!");
            return;
        }

        // create request
        GenericUrl url = new GenericUrl(baseUrl + replaceParameter(testData.getPath()));
        HttpRequest request = reqFactory.buildRequest(testData.getMethod().toUpperCase(), url, null);
        request.setThrowExceptionOnExecuteError(false);
        JsonElement input = getJsonElement(testData.getInput());
        if (input != null) {
            System.out.println("input: " + input);
            request.setContent(ByteArrayContent.fromString("application/json", input.toString()));
        }

        // call api
        HttpResponse response = request.execute();
        System.out.println("status: " + response.getStatusCode() + " ContentType: " + response.getContentType());
        assertEquals(testData.getStatus(), response.getStatusCode());

        // verify response
        if ("application/json".equals(response.getContentType())) {
            JsonElement actual = JsonParser.parseString(response.parseAsString());
            System.out.println("actual: " + actual);
            if (testData.getCount().isPresent()) {
                int count = actual.isJsonArray() ? actual.getAsJsonArray().size() :
                actual.isJsonNull() ? 0 : 1;
                assertEquals(testData.getCount().get(), count);
            }

            if (StringUtils.isNotBlank(testData.getResultName())) {
                result.add(testData.getResultName(), actual);
            }

            JsonElement expected = getJsonElement(testData.getExpected());
            if (expected != null) {
                assertTrue(expected.equals(actual), "expected: " + expected + "\nactual: " + actual);
            }
        }
    }

    /**
     * Replace parameter in input string.
     * 
     * @param input input string
     * @return repleced string
     */
    public String replaceParameter(String input) {
        StringBuffer sb = new StringBuffer();
        DocumentContext context = JsonPath.using(conf).parse(result);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            // get JsonElement by JsonPath
            JsonElement el = context.read("$." + matcher.group(1));
            if (el != null) {
                String replacement = el.isJsonPrimitive() ? el.getAsString() : el.toString();
                // replace string
                matcher.appendReplacement(sb, replacement);
            }
        }
        matcher.appendTail(sb);    
        return sb.toString();
    }

    /**
     * Get JsonElement from json string.
     * 
     * @param json json string
     * @return JsonElement
     * @throws IOException
     */
    public JsonElement getJsonElement(String json) throws IOException {
        if (StringUtils.isNotBlank(json)) {
            if (json.endsWith(".json")) {
                File jsonFile = new File(json);
                if (!jsonFile.exists()) {
                    throw new FileNotFoundException("File not found. fileName: " + json);
                }
                json = new String(Files.readAllBytes(jsonFile.toPath()), Charset.forName("UTF-8"));
            }
            // replace parameter & parse
            return JsonParser.parseString(replaceParameter(json));
        }
        return null;
    }
}
