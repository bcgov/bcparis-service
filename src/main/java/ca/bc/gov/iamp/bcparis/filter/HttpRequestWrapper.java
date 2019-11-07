package ca.bc.gov.iamp.bcparis.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private byte[] requestBody;

    public HttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        try {
            requestBody = IOUtils.toByteArray(request.getInputStream());
        } catch (IOException ex) {
            requestBody = new byte[0];
        }
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

    public String getRequestBodyAsString() {
        return new String(requestBody);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new RuntimeException("Not implemented");
            }

            public int read () throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

}