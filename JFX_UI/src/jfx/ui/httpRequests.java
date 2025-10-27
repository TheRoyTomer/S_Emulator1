package jfx.ui;

import Client_UTILS.ClientConstants;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.FunctionSelectorChoiseDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static Client_UTILS.ClientConstants.*;

public class httpRequests
{
    private final EmulatorScreenController mainController;

    public httpRequests(EmulatorScreenController mainController)
    {
        this.mainController = mainController;
    }

    public void httpViewExpandedProgram(int degree)
    {

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/viewExpendedProgram"))
                .newBuilder()
                .addQueryParameter("degree", String.valueOf(degree))
                .build();

        Request viewRequest = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(viewRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json = null;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "viewExpendedProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        ViewResultDTO res = GSON.fromJson(json, ViewResultDTO.class);
                        Platform.runLater(() -> mainController.InstructionsUpdate(res));

                    } catch (Exception parseEx)
                    {
                        final String msg = "viewExpendedProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to view expended program: " + e.getMessage()));
            }
        });
    }

    public void httpGetInputVariables()
    {

        Request getInputVariablesRequest = new Request.Builder()
                .url(SERVER_URL + "/getInputVariablesPreExecute")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(getInputVariablesRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "getInputVariablesRequest failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        Type t = new TypeToken<List<VariableDTO>>() {}.getType();
                        List<VariableDTO> variablesRes = GSON.fromJson(json, t);
                        mainController.postGetInputVariablesRequest(variablesRes);

                    } catch (Exception parseEx)
                    {
                        final String msg = "getInputVariables JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to get Input Variables: " + e.getMessage()));
            }
        });
    }

    public void httpExecuteProgram(int degree, List<Long> inputsVal)
    {

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/executeProgram"))
                .newBuilder()
                .addQueryParameter("degree", String.valueOf(degree));

        for (Long inputVal : inputsVal) {urlBuilder.addQueryParameter("inputs", String.valueOf(inputVal));}

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0]))
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "ExecuteProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        ExecuteResultDTO res = GSON.fromJson(json, ExecuteResultDTO.class);
                        Platform.runLater(() -> mainController.handleExecuteRes(res));

                    } catch (Exception parseEx)
                    {
                        final String msg = "ExecuteProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to execute program: " + e.getMessage()));
            }
        });
    }


    public void httpStepOver(long pc)
    {

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/stepOver"))
                .newBuilder()
                .addQueryParameter("PC", String.valueOf(pc)).build();


        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0]))
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "stepOver failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        StepOverResult res = GSON.fromJson(json, StepOverResult.class);
                        Platform.runLater(() -> mainController.postStepOverAction(res));

                    } catch (Exception parseEx)
                    {
                        final String msg = "StepOver JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to step Over: " + e.getMessage()));
            }
        });
    }



    public void httpPreDebug(int degree, List<Long> inputsVal)
    {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/preDebug"))
                .newBuilder()
                .addQueryParameter("degree", String.valueOf(degree));

        for (Long inputVal : inputsVal) {urlBuilder.addQueryParameter("inputs", String.valueOf(inputVal));}

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url).get().build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "PreDebug failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        Type t = new TypeToken<List<VariableDTO>>() {}.getType();
                        List<VariableDTO> res = GSON.fromJson(json, t);
                        Platform.runLater(() -> mainController.postPreDebug(res));


                    } catch (Exception parseEx)
                    {
                        final String msg = "PreDebug JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to preDebug: " + e.getMessage()));
            }
        });
    }


    public void httpBreakPoint(long startPC,  long destPC)
    {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/breakPoint"))
                .newBuilder()
                .addQueryParameter("startPC", String.valueOf(startPC))
                .addQueryParameter("destPC", String.valueOf(destPC)).build();

        RequestBody body = RequestBody.create(new byte[0]);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "BreakPoint failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        StepOverResult res = GSON.fromJson(json, StepOverResult.class);
                        Platform.runLater(() -> mainController.postBreakPointAction(res));

                    } catch (Exception parseEx)
                    {
                        final String msg = "BreakPoint JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to BreakPoint: " + e.getMessage()));
            }
        });
    }



    public void httpResume(int pc)
    {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/resumeDebug"))
                .newBuilder()
                .addQueryParameter("PC", String.valueOf(pc)).build();

        RequestBody body = RequestBody.create(new byte[0]);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "Resume failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        ExecuteResultDTO res = GSON.fromJson(json, ExecuteResultDTO.class);
                        Platform.runLater(() -> mainController.postResume(res));

                    } catch (Exception parseEx) {
                        final String msg = "Resume JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to Resume: " + e.getMessage()));
            }
        });
    }



   /* public void httpChangeSelectedProgram(String name)
    {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/changeSelectedProgram"))
                .newBuilder()
                .addQueryParameter("Name", name).build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {

                        final String msg = "changeSelectedProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        ViewResultDTO res = GSON.fromJson(json, ViewResultDTO.class);
                        Platform.runLater(() -> mainController.postChangeSelectedProgram(res));

                    } catch (Exception parseEx) {
                        final String msg = "changeSelectedProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to changeSelectedProgram: " + e.getMessage()));
            }
        });
    }*/

      public void httpChangeSelectedProgram(String name)
    {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(SERVER_URL + "/changeSelectedProgram"))
                .newBuilder()
                .addQueryParameter("Name", name).build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0]))
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {

                        final String msg = "changeSelectedProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        ViewResultDTO res = GSON.fromJson(json, ViewResultDTO.class);
                        //Todo: change that later
                        //Platform.runLater(() -> mainController.postChangeSelectedProgram(res));

                    } catch (Exception parseEx) {
                        final String msg = "changeSelectedProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to changeSelectedProgram: " + e.getMessage()));
            }
        });
    }

    public int httpGetMaxDegree()
    {
        Request request = new Request.Builder()
                .url(SERVER_URL + "/GetMaxDegree")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute())
        {
            if (!response.isSuccessful()) {
                System.out.println("[CLIENT] GetMaxDegree failed: HTTP " + response.code());
                return -1;
            }

            String body = (response.body() != null) ? response.body().string() : "";

            try
            {
                return GSON.fromJson(body, Integer.class);
            } catch (Exception e) {
                try {
                    JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                    if (obj.has("maxDegree"))
                    {
                        return obj.get("maxDegree").getAsInt();
                    }
                } catch (Exception ignored) { }
            }

            System.out.println("[CLIENT] Invalid JSON: " + body);
            return -1;

        } catch (IOException e) {
            System.out.println("[CLIENT] Network error: " + e.getMessage());
            return -1;
        }
    }


    public void httpGetHistory(Consumer<List<StatisticDTO>> callback) {
        Request getHistoryRequest = new Request.Builder()
                .url(SERVER_URL + "/getHistory")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(getHistoryRequest).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "GetHistory failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    try {
                        Type t = new TypeToken<List<StatisticDTO>>() {}.getType();
                        List<StatisticDTO> historyLst = GSON.fromJson(json, t);

                        Platform.runLater(() -> {
                            if (callback != null) {
                                callback.accept(historyLst);
                            }
                        });

                    } catch (Exception parseEx2) {
                        final String msg = "GetHistory JSON parse error: " + parseEx2.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                    }
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call2, @NotNull IOException e) {
                Platform.runLater(() -> UTILS.showError("Failed to get history: " + e.getMessage()));
            }
        });
    }



    /*public void loadAvailablePrograms() {
        String url = ClientConstants.SERVER_URL + "/getAllPrograms";
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = ClientConstants.HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                List<FunctionSelectorChoiseDTO> programs =
                        ClientConstants.GSON.fromJson(body,
                                new TypeToken<List<FunctionSelectorChoiseDTO>>(){}.getType());

                Platform.runLater(() -> mainController.updateProgramComboBox(programs));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
