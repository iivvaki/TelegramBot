package com.example.demo.services;

import java.io.IOException;
import java.net.MalformedURLException;

public interface JokeService {
    String getJokes() throws IOException;
}
