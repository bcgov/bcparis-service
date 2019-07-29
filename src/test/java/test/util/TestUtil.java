package test.util;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public final class TestUtil {


	public static String readFile(final String file) {
		URL url = Resources.getResource(file);
		try {
			return Resources.toString(url, Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
