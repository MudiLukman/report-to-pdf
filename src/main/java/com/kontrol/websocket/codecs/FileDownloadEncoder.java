package com.kontrol.websocket.codecs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kontrol.websocket.FileDownload;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class FileDownloadEncoder implements Encoder.Text<FileDownload> {

    @Override
    public String encode(FileDownload fileDownload) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString(fileDownload);
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
            throw new EncodeException(fileDownload, ex.getMessage());
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
