package Server_UTILS;

import Engine.EngineFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServerConstants {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
            .create();

    public static final EngineFacade testFacade = new EngineFacade();

    public static final String SESSION_FACADE_KEY = "facade";
    public static final String SESSION_AUTH_KEY   = "AUTH";
    public static final String REGISTRY_ATTR = "usernameToSession";

}