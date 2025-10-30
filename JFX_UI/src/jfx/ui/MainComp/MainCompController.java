package jfx.ui.MainComp;

import Out.BaseProgramInfoDTO;
import Out.FunctionInfoDTO;
import Out.FunctionSelectorChoiseDTO;
import Out.ViewResultDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.LoginComp.LoginCompController;
import jfx.ui.UTILS;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static Client_UTILS.ClientConstants.*;
import static Client_UTILS.ClientConstants.GSON;
import static Client_UTILS.ClientConstants.HTTP_CLIENT;

public class MainCompController {

    // FXML Data Members
    @FXML
    private Label usernameLabel;

    @FXML
    private Label screenNameLabel;

    @FXML
    private Label creditsLabel;

    @FXML
    private StackPane screenStackPane;

    // Cached screens and controllers (lazy loading)
    private Parent loginScreen;
    private LoginCompController loginCompController;

    private Parent emulatorScreen;
    private EmulatorScreenController emulatorScreenController;

    private Parent dashboardScreen;
    private DashboardScreenCompController dashboardScreenCompController;

    // Current screen controller
    private Object currentScreenController;


    // Credits property
    private final IntegerProperty creditsProperty = new SimpleIntegerProperty(0);

    private BaseProgramInfoDTO currentProgramInfo = null;


    @FXML
    public void initialize()
    {
        // Initialize with default values
        setUsername("Guest");
        setScreenName("Login Screen");

        // Bind credits label to property
        creditsLabel.textProperty().bind(
                Bindings.format("Credits: %d", creditsProperty)
        );

        // Set initial value
        creditsProperty.set(0);
    }
    // Method to load LoginComp screen
    public void loadLoginScreen()
    {
        try {
            // Load LoginComp.fxml if not already loaded
            if (loginScreen == null)
            {
                FXMLLoader loader = loadFXML("/jfx/ui/LoginComp/LoginComp.fxml");
                loginScreen = loader.load();
                loginCompController = loader.getController();

                loginCompController.setMainCompController(this);
            }

            // Set the screen to screenStackPane
            screenStackPane.getChildren().clear();
            screenStackPane.getChildren().add(loginScreen);

            // Update labels
            setScreenName("Login Screen");
            setUsername("Guest");
            creditsProperty.set(0);

            // Store controller
            currentScreenController = loginCompController;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Login screen: " + e.getMessage());
        }
    }

    // Method to load EmulatorScreen
    public void loadEmulatorScreen()
    {
        try {
            // Load EmulatorScreen.fxml if not already loaded (cache it)
            if (emulatorScreen == null)
            {
                FXMLLoader loader = loadFXML("/jfx/ui/EmulatorScreen/EmulatorScreen.fxml");
                emulatorScreen = loader.load();
                emulatorScreenController = loader.getController();
                emulatorScreenController.setMainCompController(this);
            }

            // Set the screen to screenStackPane
            screenStackPane.getChildren().clear();
            screenStackPane.getChildren().add(emulatorScreen);

            // Update screen name
            setScreenName("Emulator Screen");

            // Store controller
            currentScreenController = emulatorScreenController;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Emulator screen: " + e.getMessage());
        }
    }

    // Method to load DashboardScreen
    public void loadDashboardScreen() {
        try {
            // Load DashboardScreenComp.fxml if not already loaded (cache it)
            if (dashboardScreen == null) {
                FXMLLoader loader = loadFXML("/jfx/ui/DashboardScreenComp/DashboardScreenComp.fxml");
                dashboardScreen = loader.load();
                dashboardScreenCompController = loader.getController();
                dashboardScreenCompController.setMainCompController(this);
            }

            // Set the screen to screenStackPane
            screenStackPane.getChildren().clear();
            screenStackPane.getChildren().add(dashboardScreen);

            // Update screen name
            setScreenName("Dashboard Screen");

            // Store controller
            currentScreenController = dashboardScreenCompController;

            if (dashboardScreenCompController != null)
            {
                dashboardScreenCompController.startPolling();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Dashboard screen: " + e.getMessage());
        }
    }

    // Helper method to load FXML and return both Parent and Controller
    private FXMLLoader loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader;
    }

    public void setUsername(String username)
    {
        usernameLabel.setText("Username: " + username);
    }

    public String getUsername()
    {
        String text = usernameLabel.getText();
        return text.replace("Username: ", "");
    }
    public void setScreenName(String screenName) {
        screenNameLabel.setText(screenName);
    }

    public String getScreenName() {
        return screenNameLabel.getText();
    }

    public BaseProgramInfoDTO getCurrentProgramInfo() {return currentProgramInfo;}

    public void setCurrentProgramInfo(BaseProgramInfoDTO currentProgramInfo)
    {
        this.currentProgramInfo = currentProgramInfo;
    }

    public void setCredits(int credits) {creditsProperty.set(credits);}

    public void removeCredits(int credits) {creditsProperty.set(creditsProperty.get() - credits);}

    public int getCredits() {return creditsProperty.get();}

    public IntegerProperty getCreditsProperty() {return creditsProperty;}

    // Getter for current screen controller
    public Object getCurrentScreenController() {
        return currentScreenController;
    }

    // Getters for cached controllers
    public LoginCompController getLoginCompController() {
        return loginCompController;
    }

    public EmulatorScreenController getEmulatorScreenController() {
        return emulatorScreenController;
    }

    public DashboardScreenCompController getDashboardScreenCompController() {
        return dashboardScreenCompController;
    }


//Todo: use that when switching DASHBOARD TO emulator
    /*public void displaySelectedProgram(List<FunctionSelectorChoiseDTO> funcInputStringsAndNames)
    {
        loadEmulatorScreen();

        emulatorScreenController.preDisplaySelectedProgram();

        final String viewUrl = SERVER_URL + "/viewProgram";
        final String degreeUrl = SERVER_URL + "/GetMaxDegree";


        Request viewRequest = new Request.Builder()
                .url(viewUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(viewRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "viewProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    ViewResultDTO res;
                    try {
                        res = GSON.fromJson(json, ViewResultDTO.class);
                    } catch (Exception parseEx) {
                        final String msg = "viewProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    Request maxDegreeRequest = new Request.Builder()
                            .url(degreeUrl)
                            .get()
                            .addHeader("Accept", "application/json")
                            .build();

                    HTTP_CLIENT.newCall(maxDegreeRequest).enqueue(new Callback()
                    {
                        @Override
                        public void onResponse(@NotNull Call call2, @NotNull Response response2) throws IOException
                        {
                            String json2 = null;
                            try {
                                int code2 = response2.code();
                                json2 = (response2.body() != null) ? response2.body().string() : "";

                                if (!response2.isSuccessful()) {
                                    final String msg = "GetMaxDegree failed: HTTP " + code2;
                                    Platform.runLater(() -> UTILS.showError(msg));
                                    return;
                                }

                                Map<String, Object> result;
                                try {
                                    result = GSON.fromJson(json2, Map.class);
                                } catch (Exception parseEx2) {
                                    final String msg = "GetMaxDegree JSON parse error: " + parseEx2.getMessage();
                                    Platform.runLater(() -> UTILS.showError(msg));
                                    return;
                                }

                                Object md = result.get("maxDegree");
                                if (md == null) {
                                    Platform.runLater(() -> UTILS.showError("GetMaxDegree: missing 'maxDegree' in response"));
                                    return;
                                }
                                int maxDegree = (md instanceof Number) ? ((Number) md).intValue() : Integer.parseInt(md.toString());

                                Platform.runLater(() -> {
                                    emulatorScreenController.postDisplaySelectedProgram(res, maxDegree);
                                });

                            } finally {
                                response2.close();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call2, @NotNull IOException e)
                        {
                            System.out.println("[CLIENT] GetMaxDegree network error: " + e.getMessage());
                            Platform.runLater(() -> UTILS.showError("Failed to get max degree: " + e.getMessage()));
                        }
                    });

                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to view original program: " + e.getMessage()));
            }
        });
    }
*/
    public void resetBreakPoint()
    {
        if (emulatorScreenController != null)
        {
            emulatorScreenController.resetBreakPoint();
        }
    }

    public void displaySelectedProgram(String programName)
    {
        loadEmulatorScreen();
        emulatorScreenController.preDisplaySelectedProgram();
        changeAndDisplayProgram("/changeSelectedProgram", "programName", programName);
    }

    public void displaySelectedFunction(String functionName)
    {
        loadEmulatorScreen();
        emulatorScreenController.preDisplaySelectedProgram();
        changeAndDisplayProgram("/changeSelectedFunction", "functionName", functionName);
    }

    // Generic method - handles both programs and functions!
    private void changeAndDisplayProgram(String endpoint, String paramName, String paramValue)
    {
        final String changeUrl = SERVER_URL + endpoint;

        HttpUrl url = HttpUrl.parse(changeUrl).newBuilder()
                .addQueryParameter(paramName, paramValue)
                .build();

        Request changeRequest = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0]))
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(changeRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                try {
                    int code = response.code();
                    String json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        Platform.runLater(() -> UTILS.showError("Failed to change selection: HTTP " + code));
                        return;
                    }

                    viewAndDisplayProgram();

                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to change selection: " + e.getMessage()));
            }
        });
    }

    private void viewAndDisplayProgram()
    {
        final String viewUrl = SERVER_URL + "/viewProgram";
        final String degreeUrl = SERVER_URL + "/GetMaxDegree";

        Request viewRequest = new Request.Builder()
                .url(viewUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(viewRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        Platform.runLater(() -> UTILS.showError("viewProgram failed: HTTP " + code));
                        return;
                    }

                    ViewResultDTO res;
                    try {
                        res = GSON.fromJson(json, ViewResultDTO.class);
                    } catch (Exception parseEx) {
                        Platform.runLater(() -> UTILS.showError("viewProgram JSON parse error: " + parseEx.getMessage()));
                        return;
                    }

                    Request maxDegreeRequest = new Request.Builder()
                            .url(degreeUrl)
                            .get()
                            .addHeader("Accept", "application/json")
                            .build();

                    HTTP_CLIENT.newCall(maxDegreeRequest).enqueue(new Callback()
                    {
                        @Override
                        public void onResponse(@NotNull Call call2, @NotNull Response response2) throws IOException
                        {
                            try {
                                int code2 = response2.code();
                                String json2 = (response2.body() != null) ? response2.body().string() : "";

                                if (!response2.isSuccessful()) {
                                    Platform.runLater(() -> UTILS.showError("GetMaxDegree failed: HTTP " + code2));
                                    return;
                                }

                                Map<String, Object> result;
                                try {
                                    result = GSON.fromJson(json2, Map.class);
                                } catch (Exception parseEx2) {
                                    Platform.runLater(() -> UTILS.showError("GetMaxDegree JSON parse error: " + parseEx2.getMessage()));
                                    return;
                                }

                                Object md = result.get("maxDegree");
                                if (md == null) {
                                    Platform.runLater(() -> UTILS.showError("GetMaxDegree: missing 'maxDegree' in response"));
                                    return;
                                }
                                int maxDegree = (md instanceof Number) ? ((Number) md).intValue() : Integer.parseInt(md.toString());

                                Platform.runLater(() -> {
                                    emulatorScreenController.postDisplaySelectedProgram(res, maxDegree);
                                });

                            } finally {
                                response2.close();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call2, @NotNull IOException e)
                        {
                            Platform.runLater(() -> UTILS.showError("Failed to get max degree: " + e.getMessage()));
                        }
                    });

                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to view program: " + e.getMessage()));
            }
        });
    }

    public int getArchitectureCost(int architecture)
    {
        return switch (architecture)
        {
            case 1 -> 5;
            case 2 -> 100;
            case 3 -> 500;
            case 4 -> 1000;
            default -> 0;
        };
    }


}