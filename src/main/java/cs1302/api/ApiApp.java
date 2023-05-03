package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static final String DEFAULT_IMG = "file:resources/default.jpeg";
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    String[] appId1;
    String[] appKey1;
    String[] appId2;
    String[] appKey2;

    Clipboard clipboard;

    Stage stage;
    Scene scene;
    VBox root;

    StackPane topArea;
    ImageView backgroundImage;
    HBox searchArea;
    TextField urlSearch;
    Button searchButton;

    ScrollPane scroll;

    VBox recipeInfo;
    HBox picAndLabels;
    Label yield;
    VBox nameAndLabels;
    ImageView foodPic;
    Label labels;
    Text recipeName;
    HBox textArea;
    Label ingredients;
    TextFlow nutrition;
    HBox linkAndButton;
    Label recipeLink;
    Button copyButton;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        clipboard = Clipboard.getSystemClipboard();

        root = new VBox();
        topArea = new StackPane();
        backgroundImage = new ImageView(DEFAULT_IMG);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setFitWidth(600);
        backgroundImage.setFitHeight(450);
        backgroundImage.setSmooth(true);

        searchArea = new HBox(8);
        searchArea.setAlignment(Pos.CENTER);
        urlSearch = new TextField();
        urlSearch.setPromptText("Search for a Recipe");
        urlSearch.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%); }");
        searchArea.setHgrow(urlSearch, Priority.ALWAYS);
        searchButton = new Button("Search");

        recipeName = new Text("Search for a Random Recipe in the Search Box");
        recipeName.setWrappingWidth(backgroundImage.getFitWidth());
        recipeName.setTextAlignment(TextAlignment.JUSTIFY);

        recipeInfo = new VBox(4);
        recipeInfo.setAlignment(Pos.CENTER);
        picAndLabels = new HBox(8);
        picAndLabels.setAlignment(Pos.CENTER);
        nameAndLabels = new VBox(4);
        picAndLabels.setAlignment(Pos.CENTER);
        yield = new Label();
        foodPic = new ImageView();
        foodPic.setPreserveRatio(true);
        labels = new Label();
        labels.setWrapText(true);
        labels.setTextAlignment(TextAlignment.JUSTIFY);
        labels.setMaxWidth(350);
        textArea = new HBox(8);
        ingredients = new Label();
        nutrition = new TextFlow();

        linkAndButton = new HBox(4);
        recipeLink = new Label();
        recipeLink.setWrapText(true);
        recipeLink.setTextAlignment(TextAlignment.JUSTIFY);
        recipeLink.setMaxWidth(450);
        copyButton = new Button("Copy Link");
        copyButton.setVisible(false);

        scroll = new ScrollPane(recipeInfo);

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("init() called");


        String[] id1 = {"47c19ceb", "139fca81", "6eb9f7d5"};
        String[] key1 = {"70f21e3ad8feded302fe0126077a68b3", "e1168638f71b7e5361012a08dd44ae5d",
            "4fddf9d4252b465ea4708911e576ea4b"};
        appId1 = id1;
        appKey1 = key1;

        String[] id2 = {"6fb051cc", "591fb82e", "fd78c514", "3b427cc8", "38b7429e", "37a68482"};
        String[] key2 = {"ffc9fcd9e08f0384f399e4b73c5c868e", "998637b84b6535609e2e83d8ebf2a063",
            "c04320424b88b5cad6f5993f4008ce49", "c31610f859a6b0e4441c922e6c3e53c3",
            "008b7f70bfb8147c16d7d167a91e5a84", "90947c5d004d25954a05de56937ab4c0"};
        appId2 = id2;
        appKey2 = key2;

        searchArea.getChildren().addAll(urlSearch, searchButton);
        searchArea.setMargin(urlSearch, new Insets(0, 0, 0, 10));
        searchArea.setMargin(searchButton, new Insets(0, 10, 0, 0));

        topArea.getChildren().addAll(backgroundImage, searchArea);

        textArea.getChildren().addAll(ingredients, nutrition);

        linkAndButton.getChildren().addAll(recipeLink, copyButton);

        nameAndLabels.getChildren().addAll(recipeName, yield, labels);
        picAndLabels.getChildren().addAll(foodPic, nameAndLabels);
        recipeInfo.getChildren().addAll(picAndLabels, textArea, linkAndButton);

        root.getChildren().addAll(topArea, scroll);


        Runnable thread = () -> this.getRecipe();
        searchButton.setOnAction(event -> runNow(thread));
    } // init


    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // setup scene
        scene = new Scene(root);
        scene.setFill(Color.BEIGE);

         // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.show();

    } // start

    /**
     * Method to display a random recipe on the application.
     */
    private void getRecipe() {
        String search = urlSearch.getText().replaceAll(" ", "%20");
        search = search.toLowerCase();
        int randomId = randomize(appId1.length);
        String app_id = appId1[randomId];
        String app_key = appKey1[randomId];
        String query = String.format("&q=%s&app_id=%s&app_key=%s&ingr=10", search, app_id, app_key);
        String uri = "https://api.edamam.com/api/recipes/v2?type=public" + query;
        System.out.println(uri);

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if

            String jsonString = response.body();
            EdamamResponse edamamResponse = GSON.fromJson(jsonString, EdamamResponse.class);

            int rndmDish = randomize(edamamResponse.hits.length);

            int randomId2 = randomize(appId2.length);
            String app_id2 = appId2[randomId2];
            String app_key2 = appKey2[randomId2];
            String uri2 = "https://api.edamam.com/api/nutrition-data?";

            int[] totalNutrition = new int[8];

            for (int i = 0; i < edamamResponse.hits[rndmDish].recipe.ingredientLines.length; i++) {
                String search2 = edamamResponse.hits[rndmDish].recipe.ingredientLines[i];
                search2 = search2.replaceAll(" ", "%20");
                search2 = search2.toLowerCase();
                String q2 = String.format("%sapp_id=%s&app_key=%s&nutrition-type=cooking&ingr=%s",
                    uri2, app_id2, app_key2, search2);

                HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(q2)).build();
                HttpResponse<String> resp2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());

                String jsonString2 = resp2.body();
                jsonString2 = changeVariables(jsonString2);

                EdamamNutrition edamamNut = GSON.fromJson(jsonString2, EdamamNutrition.class);

                totalNutrition = addNutritionTotals(edamamNut, totalNutrition);
            } // for

            addNutritionLabel(totalNutrition);
            Platform.runLater(() -> changeScene(edamamResponse, rndmDish));

        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } // catch

    } // getRecipe

    /**
     * Method used to add the individual nutrition values of each ingredient
     * in a recipe.
     *
     * @param edamamNut the JSon-parsed class of {@code EdamamNutrition}.
     * @param totalNutrition an array that holds the total added
     * values of the nutrition categories of each ingredient.
     *
     * @return int[] an array that holds the total added values of the
     * nutrition categories of each ingredient.
     */
    private int[] addNutritionTotals(EdamamNutrition edamamNut, int[] totalNutrition) {
        if (edamamNut.totalNutrients.energy == null) {
            totalNutrition[0] += 0;
        } else {
            totalNutrition[0] += Math.round(edamamNut.totalNutrients.energy.quantity);
        } // else

        if (edamamNut.totalNutrients.fat == null) {
            totalNutrition[1] += 0;
        } else {
            totalNutrition[1] += Math.round(edamamNut.totalNutrients.fat.quantity);
        } // else

        if (edamamNut.totalNutrients.cholesterol == null) {
            totalNutrition[2] += 0;
        } else {
            totalNutrition[2] += Math.round(edamamNut.totalNutrients.cholesterol.quantity);
        } // else

        if (edamamNut.totalNutrients.sodium == null) {
            totalNutrition[3] += 0;
        } else {
            totalNutrition[3] += Math.round(edamamNut.totalNutrients.sodium.quantity);
        } // else

        if (edamamNut.totalNutrients.carbs == null) {
            totalNutrition[4] += 0;
        } else {
            totalNutrition[4] += Math.round(edamamNut.totalNutrients.carbs.quantity);
        } // else

        if (edamamNut.totalNutrients.sugar == null) {
            totalNutrition[5] += 0;
        } else {
            totalNutrition[5] += Math.round(edamamNut.totalNutrients.sugar.quantity);
        } // else

        if (edamamNut.totalNutrients.protein == null) {
            totalNutrition[6] += 0;
        } else {
            totalNutrition[6] += Math.round(edamamNut.totalNutrients.protein.quantity);
        } // else

        if (edamamNut.totalNutrients.fiber == null) {
            totalNutrition[7] += 0;
        } else {
            totalNutrition[7] += Math.round(edamamNut.totalNutrients.fiber.quantity);
        } // else

        return totalNutrition;
    } // addNutritionTotals

    /**
     * Method used to add the nutrition facts to the scene.
     *
     * @param totNutrition an array that holds the total added
     * values of the nutrition categories of each ingredient.
     */
    private void addNutritionLabel(int[] totNutrition) {
        Text t1 = new Text("||\t\tNutrition\n");
        t1.setFont(new Font("Helvetica typeface", 18));

        String calories = "||\tCalories\t";
        int calQ = totNutrition[0];
        calories += calQ + "\n";
        Text t2 = new Text(calories);
        Font font2 = Font.font("Helvetica typeface", FontWeight.EXTRA_BOLD, 18);
        t2.setFont(font2);


        String fat = "||\tTotal Fat: ";
        int fatQ = totNutrition[1];
        fat += fatQ + " " + "g" + "\n";
        Text t3 = new Text(fat);
        t3.setFont(new Font("Helvetica typeface", 14));

        String cholesterol = "||\tCholesterol: ";
        int cholQ = totNutrition[2];
        cholesterol += cholQ + " " + "mg" + "\n";
        Text t4 = new Text(cholesterol);
        t4.setFont(new Font("Helvetica typeface", 14));

        String sodium = "||\tSodium: ";
        int sodiumQ = totNutrition[3];
        sodium += sodiumQ + " " + "mg" + "\n";
        Text t5 = new Text(sodium);
        t5.setFont(new Font("Helvetica typeface", 14));

        String carbs = "||\tTotal Carbohydrates: ";
        int carbsQ = totNutrition[4];
        carbs += carbsQ + " " + "g" + "\n";
        Text t6 = new Text(carbs);
        t6.setFont(new Font("Helvetica typeface", 14));

        String sugar = "||\tTotal Sugar: ";
        int sugarQ = totNutrition[5];
        sugar += sugarQ + " " + "g" + "\n";
        Text t7 = new Text(sugar);
        t7.setFont(new Font("Helvetica typeface", 14));

        String protein = "||\tProtein: ";
        int proteinQ = totNutrition[6];
        protein += proteinQ + " " + "g" + "\n";
        Text t8 = new Text(protein);
        t8.setFont(new Font("Helvetica typeface", 14));

        String fiber = "||\tTotal Fiber: ";
        int fiberQ = totNutrition[7];
        fiber += fiberQ + " " + "g" + "\n";
        Text t9 = new Text(fiber);
        t9.setFont(new Font("Helvetica typeface", 14));

        Platform.runLater(() -> nutrition.getChildren().clear());
        Platform.runLater(() -> nutrition.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    } // addNutritionLabel


    /**
     * Method to change the variable names of the nutrition categories
     * retrieved from the parsed Json string. This class exists to
     * satisfy checkstyle.
     *
     * @param jsonString2 the jsonString being parsed, replacing
     * variable names with names valid under checkstyle.
     * @return String the new jsonString.
     */
    private String changeVariables(String jsonString2) {
        jsonString2 = jsonString2.replaceAll("ENERC_KCAL", "energy");
        jsonString2 = jsonString2.replaceAll("FAT", "fat");
        jsonString2 = jsonString2.replaceAll("CHOCDF", "carbs");
        jsonString2 = jsonString2.replaceAll("FIBTG", "fiber");
        jsonString2 = jsonString2.replaceAll("SUGAR", "sugar");
        jsonString2 = jsonString2.replaceAll("CHOLE", "cholesterol");
        jsonString2 = jsonString2.replaceAll("NA", "sodium");
        jsonString2 = jsonString2.replaceAll("PROCNT", "protein");

        return jsonString2;
    } // changeVariables

    /**
     * Method to generate a random int.
     *
     * @param length the length of something (i.e. an array)
     * @return int random integer
     */
    private int randomize(int length) {
        Random random = new Random();
        int randomInt = random.nextInt(length);

        return randomInt;
    } // randomize

    /**
     * Method to chage the elements on the scene and assign the
     * values to previously unused variables.
     *
     * @param response the {@code EdamamResponse} class created from
     * parsing the JSon string.
     * @param random the random integer created to choose a random recipe.
     */
    private void changeScene(EdamamResponse response, int random) {
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setFitHeight(100);
        backgroundImage.setFitWidth(620);


        int ingredientLength = response.hits[random].recipe.ingredientLines.length;
        int labelLength = response.hits[random].recipe.healthLabels.length;

        foodPic.setImage(new Image(response.hits[random].recipe.image));
        foodPic.setFitWidth(150);
        foodPic.setFitHeight(150);
        foodPic.setSmooth(true);

        double yieldValue = response.hits[random].recipe.yield;
        String yieldString = Double.toString(yieldValue);
        yield.setText("Servings: " + yieldString);
        labels.setText("");
        for (int i = 0; i < labelLength; i++) {
            String labelLine = response.hits[random].recipe.healthLabels[i];
            labelLine.replaceAll("-", " ");
            labels.setText(labels.getText() + labelLine + " * ");
        } // for

        recipeName.setText(response.hits[random].recipe.label);
        recipeName.setFont(new Font(20));

        ingredients.setWrapText(true);
        ingredients.setTextAlignment(TextAlignment.JUSTIFY);
        ingredients.setMaxWidth(300);

        ingredients.setText("\tIngredients:\n");
        for (int i = 0; i < ingredientLength; i++) {
            String ingrLine = response.hits[random].recipe.ingredientLines[i];
            ingredients.setText(ingredients.getText() + "- " + ingrLine + "\n");
        } // for


        recipeLink.setText("Recipe Link: \n");
        String link = response.hits[random].recipe.url;
        recipeLink.setText(recipeLink.getText() + link);
        copyButton.setVisible(true);
        copyButton.setOnAction(event -> copy(link));
    } // changeScene

    /**
     * Method to create the functionality of the {@code copyButton}.
     *
     * @param link the link to copy to the system clipboard.
     */
    private void copy(String link) {
        ClipboardContent content = new ClipboardContent();
        content.putString(link);
        clipboard.setContent(content);
    } // copy

    /**
     * Creates and starts a new daemon thread that executes {@code target.run()}.
     *
     * @param target the object whose {@code run} method is called on when
     * thread starts.
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // target

} // ApiApp
