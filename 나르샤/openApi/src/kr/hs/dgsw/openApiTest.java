package kr.hs.dgsw;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class openApiTest {

	public static void main(String[] args) {
		String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";
		String accessKey = "1c8d733e-e922-4d5e-b4ef-e2cf9e1d8570"; //  API Key
		String languageCode = "korean"; 
		String audioFilePath = "C:\\audio\\record.wav"; 
		String audioContents = null;

		Gson gson = new Gson();

		Map<String, Object> request = new HashMap<>();
		Map<String, String> argument = new HashMap<>();

		try {
			Path path = Paths.get(audioFilePath);
			byte[] audioBytes = Files.readAllBytes(path);
			audioContents = Base64.getEncoder().encodeToString(audioBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		argument.put("language_code", languageCode);
		argument.put("audio", audioContents);

		request.put("access_key", accessKey);
		request.put("argument", argument);

		URL url;
		Integer responseCode = null;
		String responBody = null;
		try {
			url = new URL(openApiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(gson.toJson(request).getBytes("UTF-8"));
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();
			InputStream is = con.getInputStream();
			byte[] buffer = new byte[is.available()];
			int byteRead = is.read(buffer);
			responBody = new String(buffer);

			System.out.println("[responseCode] " + responseCode);
			System.out.println("[responBody]");
			System.out.println(responBody);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
